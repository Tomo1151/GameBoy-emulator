package com.syntck.ppu;

import static com.syntck.Functions.wrappingAdd;
import static com.syntck.Functions.wrappingSub;

public class PPU {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int BACKGROUND_SIZE = 256; // 背景の幅
  public static final int VRAM_BEGIN = 0x8000;
  public static final int VRAM_END   = 0x9FFF;
  public static final int VRAM_SIZE  = VRAM_END - VRAM_BEGIN + 1;
  
  public static final int SPRITE_OFFSET_X = 8; // スプライトのX座標オフセット
  public static final int SPRITE_OFFSET_Y = 16; // スプライトのY座標オフセット
  public static final int WINDOW_OFFSET_X = 7; // ウィンドウのX座標オフセット
  public static final int WINDOW_OFFSET_Y = 0; // ウィンドウのY座標オフセット

  // レジスタ
  public int ly; // 0xFF44 LYレジスタ (現在のスキャンラインのY座標)
  public int lyc; // 0xFF45 LYCレジスタ (LYと比較するY座標)
  public LCDControlRegisters controls; // 0xFF40 LCD制御レジスタ (LCDの制御)
  public LCDStatusRegisters status; // 0xFF41 LCDステータスレジスタ (LCDの状態を示すフラグ)
  public int scy; // 0xFF42 SCYレジスタ (スクロールY座標)
  public int scx; // 0xFF43 SCXレジスタ (スクロールX座標)
  public int dma; // 0xFF46 DMAレジスタ (DMA転送の開始アドレス)
  public int bgp; // 0xFF47 BGPレジスタ (背景パレットデータ)
  public int obp0; // 0xFF48 OBP0レジスタ (スプライトパレットデータ0)
  public int obp1; // 0xFF49 OBP1レジスタ (スプライトパレットデータ1)
  public int wy; // 0xFF4A WYレジスタ (ウィンドウY座標)
  public int wx; // 0xFF4B WXレジスタ (ウィンドウX座標)
  private TilePixelValue[] lineIndexes = new TilePixelValue[SCREEN_WIDTH]; // 1ライン分のタイルインデックスを格納する配列
  private int windowLine;
  public boolean frameUpdated;

  public int[] vram = new int[VRAM_SIZE];
  public Tile[] tiles = new Tile[384];
  public Sprite[] sprites = new Sprite[40]; // スプライトの数は40個

  private int[] frameBuffer = new int[SCREEN_WIDTH * SCREEN_HEIGHT];
  private int scanlineCounter;
  private int prevLy  = 0; // 前回のLYレジスタの値

  private int[] oam = new int[0xA0]; // OAM (Object Attribute Memory) (スプライトの情報を格納するメモリ)

  public PPU() {
    this.vram = new int[VRAM_SIZE];
    this.controls = new LCDControlRegisters(); // LCD制御レジスタの初期化
    this.status = new LCDStatusRegisters(); // LCDステータスレジスタの初期化
    this.ly = 0; // LYレジスタの初期化
    this.lyc = 0; // LYCレジスタの初期化
    this.scy = 0; // SCYレジスタの初期化
    this.scx = 0; // SCXレジスタの初期化
    this.bgp = 0; // BGPレジスタの初期化
    this.obp0 = 0; // OBP0レジスタの初期化
    this.obp1 = 0; // OBP1レジスタの初期化
    this.wy = 0; // WYレジスタの初期化
    this.wx = 0; // WXレジスタの初期化
    this.scanlineCounter = 0; // スキャンラインカウンタの初期化
    this.windowLine = 0; // ウィンドウラインの初期化
    this.tiles = new Tile[384]; // 384 tiles
    for (int i = 0; i < this.vram.length; i++) {
      this.vram[i] = 0; // Initialize VRAM with 0
    }
    for (int i = 0; i < this.tiles.length; i++) {
        // 各タイルを正しく初期化
        TilePixelValue[][] pixels = new TilePixelValue[Tile.TILE_LENGTH][Tile.TILE_LENGTH];
        for (int y = 0; y < Tile.TILE_LENGTH; y++) {
            for (int x = 0; x < Tile.TILE_LENGTH; x++) {
                pixels[y][x] = TilePixelValue.Zero; // 明示的に初期化
            }
        }
        this.tiles[i] = new Tile();
        this.tiles[i].pixels = pixels;
    }
    for (int i = 0; i < this.oam.length; i++) {
      this.oam[i] = 0; // Initialize OAM with 0
    }
    for (int i = 0; i < this.sprites.length; i++) {
      this.sprites[i] = new Sprite(0, 0, 0, 0, 0); // Initialize sprites with default values
    }
    for (int i = 0; i < this.lineIndexes.length; i++) {
      this.lineIndexes[i] = TilePixelValue.Zero; // Initialize lineIndexes with Zero
    }
  }

  public PPUInterrupt update(int cycles) {
    if (!this.controls.enabled) {
      return PPUInterrupt.NONE; // LCDが無効な場合は何もしない
    }

    this.scanlineCounter += cycles;
    PPUInterrupt interrupt = setLCDStatus();


    if (this.scanlineCounter >= 456) {
      // 1ライン分描画された
      this.scanlineCounter -= 456;

      // LYレジスタをインクリメント
      this.ly++;
      int currentLine = this.ly; // 現在のラインを取得

      if (currentLine == 144) {
        // VBlank開始
        // VBlankフラグをセット
        // this.drawFrame();
        this.frameUpdated = true; // フレームが更新されたことを示すフラグをセット
        return PPUInterrupt.VBLANK; // VBlank割り込みを返す
      } else if (currentLine > 153) {
        // 1フレーム描画完了
        this.ly = 0; // LYをリセット
        this.windowLine = 0; // ウィンドウラインをリセット
      }
    }
    return interrupt; // 割り込みを返す
  }

  // MARK: drawScanline
  private void drawScanline(int scanline) {
    drawBackgroundLine(scanline); // 背景の描画
    drawWindowLine(scanline); // ウィンドウの描画
    drawSpritesLine(scanline); // スプライトの描画
  }

  private void drawBackgroundLine(int scanline) {
    /*  1. どこを描くのか割り出す
        x = this.scx, y = this.scy + scanline

        2. 何列目を描くのか割り出す
        row = y / 8

        3. そのタイルの何行目を描くのか割り出す
        column = y % 8

        4. そのピクセルのデータを取得・描画する
        xからSCREEN_WIDTHだけ繰り返す
    */
    if (!this.controls.bgWindowEnabled) return;

    int startAddress = ((this.controls.bgTileMap) ? 0x9C00 : 0x9800) - VRAM_BEGIN; // タイルマップの先頭アドレスをVRAMのアドレスに変換
    // int endAddress = (this.controls.bgTileMap) ? 0x9FFF : 0x9BFF;

    int originX = this.scx;
    int originY = wrappingAdd(this.scy, scanline); // スクロールY座標を考慮
    int row = originY / 8; // タイルの行を計算

    int offset = row * 32; // タイルのオフセットを計算

    int srcY = originY % 8; // タイルのY座標を計算


    for (int x = 0; x < SCREEN_WIDTH; x++) {
      int srcX = wrappingAdd(originX, x);
      int destY = scanline;
      int dextX = x;

      int vramIndex = offset + (wrappingAdd(originX, x)) / 8; // タイルのインデックスを計算

      // タイルマップの取得
      int tileIndex = this.vram[startAddress + vramIndex];

      int index = tileIndex; // タイルのインデックスを取得
      if (!this.controls.tiles && index < 128) {
        // タイルのインデックスが0x00から0x7Fまでの範囲の場合、タイルのインデックスを-128して取得
        index = wrappingAdd(index, 256); // タイルのインデックスを-128して取得
      }

      Tile tile = this.tiles[index]; // タイルを取得
      TilePixelValue pixel = tile.pixels[srcY % 8][srcX % 8]; // タイルのピクセル値を取得

      this.lineIndexes[dextX] = pixel; // タイルのインデックスを保存

      int color = Tile.getColorFromPalette(pixel, this.bgp); // タイルの色を取得
      this.frameBuffer[destY * SCREEN_WIDTH + dextX] = color; // ピクセル値をフレームバッファに書き込む
    }
  }

  // MARK: drawBackground
  private void drawBackground() {
    if (!this.controls.bgWindowEnabled) return; // BG & Windowが無効な場合は何もしない

    // タイルマップの範囲を設定
    int startAddress = (this.controls.bgTileMap) ? 0x9C00 : 0x9800;
    int endAddress = (this.controls.bgTileMap) ? 0x9FFF : 0x9BFF;

    for (int addr = startAddress; addr <= endAddress; addr++) {
      int vramAddr = addr - VRAM_BEGIN; // VRAMの配列用にアドレスを変換
      int index = this.vram[vramAddr]; // タイルの番号を取得

      if (!this.controls.tiles && index < 128) {
        // タイルの番号が0x00から0x7Fまでの範囲の場合、タイルのを-128して取得
        index = wrappingAdd(index, 256); // タイルのインデックスを-128して取得
      }
      Tile tile = this.tiles[index];

      int i = addr - startAddress; // タイルのインデックスを計算
      int screenX = ((i) % 32) * Tile.TILE_LENGTH;
      int screenY = ((i) / 32) * Tile.TILE_LENGTH;

      for (int tileX = 0; tileX < Tile.TILE_LENGTH; tileX++) {
        for (int tileY = 0; tileY < Tile.TILE_LENGTH; tileY++) {
          TilePixelValue pixel = tile.pixels[tileY][tileX]; // タイルのピクセル値を取得
          int color = Tile.getColorFromPalette(pixel, this.bgp); // タイルの色を取得
          int x = screenX + tileX; // タイルのX座標を計算
          int y = screenY + tileY; // タイルのY座標を計算

          if (x < this.scx) {
            x += BACKGROUND_SIZE-1; // スクロールX座標より左のタイルは無視
          }

          if (y < this.scy) {
            y += BACKGROUND_SIZE-1; // スクロールY座標より上のタイルは無視
          }

          x -= this.scx; // スクロールX座標を考慮
          y -= this.scy; // スクロールY座標を考慮

          if (x >= SCREEN_WIDTH || y >= SCREEN_HEIGHT) continue;

          this.frameBuffer[y * SCREEN_WIDTH + x] = color; // ピクセル値をフレームバッファに書き込む
        }
      }
    }
  }


  private void drawWindowLine(int scanline) {
    if (!this.controls.windowEnabled || this.wx > 166 || this.wy > 143) return; // ウィンドウが無効な場合は何もしない

    if (this.wy > scanline) return; // ウィンドウY座標より上のタイルは無視

    int startAddress = ((this.controls.windowTileMap) ? 0x9C00 : 0x9800) - VRAM_BEGIN; // タイルマップの先頭アドレスをVRAMのアドレスに変換

    int originY = this.windowLine; // スクロールY座標を考慮
    this.windowLine = wrappingAdd(this.windowLine, 1); // ウィンドウラインをインクリメント

    int row = originY / 8; // タイルの行を計算
    int offset = row * 32; // タイルのオフセットを計算

    for (int x = 0; x < SCREEN_WIDTH; x++) {
      int srcX = x;
      int srcY = originY;

      if (this.wx + x < WINDOW_OFFSET_X || wrappingAdd(this.wx, x) < this.wx) continue;

      int destX = wrappingSub(wrappingAdd(this.wx, x), WINDOW_OFFSET_X);
      int destY = scanline;

      if (destX >= SCREEN_WIDTH || destY >= SCREEN_HEIGHT) continue; // 画面外のタイルは無視

      int vramIndex = offset + (x / 8); // タイルのインデックスを計算
      int tileIndex = this.vram[startAddress + vramIndex]; // タイルのインデックスを取得

      if (!this.controls.tiles && tileIndex < 128) {
        // タイルのインデックスが0x00から0x7Fまでの範囲の場合、タイルのインデックスを-128して取得
        tileIndex = wrappingAdd(tileIndex, 256); // タイルのインデックスを-128して取得
      }

      Tile tile = this.tiles[tileIndex];
      TilePixelValue pixel = tile.pixels[srcY % 8][srcX % 8];

      this.lineIndexes[destX] = pixel; // タイルのインデックスを保存

      int color = Tile.getColorFromPalette(pixel, this.bgp);
      this.frameBuffer[destY * SCREEN_WIDTH + destX] = color;
    }

  }

  // MARK: drawWindow
  private void drawWindow() {
    if (!(this.controls.windowEnabled && this.wx <= 166 && this.wy <= 143)) return; // ウィンドウが無効な場合は何もしない

    int wx = this.wx - WINDOW_OFFSET_X; // ウィンドウのX座標を計算
    int wy = this.wy - WINDOW_OFFSET_Y; // ウィンドウのY座標を計算

    int windowAddressStart = (this.controls.windowTileMap) ? 0x9C00 : 0x9800; // ウィンドウタイルマップのアドレスを決定
    int windowAddressEnd = (this.controls.windowTileMap) ? 0x9FFF : 0x9BFF; // ウィンドウタイルマップのアドレスを決定

    // System.out.println(String.format("%04X..%04X", windowAddressStart, windowAddressEnd));

    for (int addr = windowAddressStart; addr <= windowAddressEnd; addr++) {
      // System.out.println("addr: " + String.format("0x%04X", addr));
      int vramAddr = addr - VRAM_BEGIN; // タイルのインデックスが保存されている先頭アドレスをVRAMのアドレスに変換
      // System.out.println("vramAddr: " + String.format("0x%04X", vramAddr));
      int index = this.vram[vramAddr];

      if (!this.controls.tiles && index < 128) {
        // タイルのインデックスが0x00から0x7Fまでの範囲の場合、タイルのインデックスを-128して取得
        index = wrappingAdd(index, 256); // タイルのインデックスを-128して取得
      }

      // System.out.println("index: " + String.format("0x%04X", index));
      Tile tile = this.tiles[index]; // タイルを取得
      // System.out.println("tile: " + tile);

      int i = addr - windowAddressStart; // タイルのインデックスを計算
      int screenX = ((i) % 32) * Tile.TILE_LENGTH; // タイルのX座標の始点を計算
      int screenY = ((i) / 32) * Tile.TILE_LENGTH; // タイルのY座標の始点を計算
      // System.out.println("sx: " + screenX + ", sy: " + screenY);

      // System.out.println("Tile print");

      for (int tileX = 0; tileX < Tile.TILE_LENGTH; tileX++) {
        for (int tileY = 0; tileY < Tile.TILE_LENGTH; tileY++) {
          TilePixelValue pixel = tile.pixels[tileY][tileX]; // タイルのピクセル値を取得
          int x = screenX + tileX; // タイルのX座標を計算
          int y = screenY + tileY; // タイルのY座標を計算

          if (x < wx || y < wy) continue; // ウィンドウのX座標より左のタイルは無視

          y -= wy; // ウィンドウY座標を考慮
          x -= wx; // ウィンドウX座標を考慮

          if (x >= SCREEN_WIDTH || y >= SCREEN_HEIGHT) continue; // 画面外のタイルは無視

          int color = Tile.getColorFromPalette(pixel, this.bgp); // タイルの色を取得

          // System.out.print(pixelNum + " ");
          this.frameBuffer[y * SCREEN_WIDTH + x] = color; // ピクセル値をフレームバッファに書き込む
        }
        // System.out.println();
      }
      // System.out.println();
    }
  }


  private void drawSpritesLine(int scanline) {
    if (!this.controls.objEnabled) return; // スプライトが無効な場合は何もしない

    int ySize = this.controls.objSize ? 16 : 8; // スプライトのサイズを取得

    for (Sprite sprite : this.sprites) {
      int spriteX = sprite.x; // スプライトのX座標を計算
      int spriteY = sprite.y; // スプライトのY座標を計算
      int attributes = sprite.attributes; // スプライトの属性を取得
      boolean priority = (attributes & 0x80) != 0; // スプライトの優先度を取得
      boolean yFlip = (attributes & 0x40) != 0; // Y軸反転フラグ
      boolean xFlip = (attributes & 0x20) != 0; // X軸反転フラグ
      int palette = (attributes & 0x10) >> 4; // パレット番号

      spriteX -= SPRITE_OFFSET_X; // スプライトのX座標を考慮
      spriteY -= SPRITE_OFFSET_Y; // スプライトのY座標を考慮

      if (scanline < spriteY || scanline >= spriteY + ySize) continue; // スプライトのY座標が範囲外の場合は無視

      int tileY = (scanline - spriteY); // スプライトのY座標を考慮
      if (yFlip) {
        tileY = ySize - 1 - tileY; // Y軸反転フラグが立っている場合はY座標を反転
      }

      for (int tileX = 0; tileX < Tile.TILE_LENGTH; tileX++) {
        // まず反転を考慮したピクセル座標を計算
        int pixelX = xFlip ? (Tile.TILE_LENGTH - 1 - tileX) : tileX;
        // int pixelY = yFlip ? (Tile.TILE_LENGTH - 1 - tileY) : tileY;

        // 反転座標からピクセルを取得
        Tile tile;
        TilePixelValue pixel;

        if (this.controls.objSize) {
          if (tileY >= Tile.TILE_LENGTH) {
            tile = this.tiles[sprite.tileIndex | 0x01]; // スプライトの上側のタイルを取得
            pixel = tile.pixels[tileY - Tile.TILE_LENGTH][pixelX]; // スプライトの上側のタイルのピクセルを取得
          } else {
            tile = this.tiles[sprite.tileIndex & 0xFE]; // スプライトの下側のタイルを取得
            pixel = tile.pixels[tileY][pixelX]; // スプライトの下側のタイルのピクセルを取得
          }
        } else {
          tile = this.tiles[sprite.tileIndex]; // スプライトのタイルを取得
          pixel =  tile.pixels[tileY][pixelX];
        }

        if (pixel == TilePixelValue.Zero) continue;

        int color = Tile.getColorFromPalette(pixel, (palette == 0) ? this.obp0 : this.obp1);

        // 画面上の表示位置は元の座標を使用
        int x = spriteX + tileX;
        int y = scanline;

        if (x < 0 || y < 0 || x >= SCREEN_WIDTH || y >= SCREEN_HEIGHT) continue;

        if (priority && this.lineIndexes[x] != TilePixelValue.Zero) {
          // スプライトの優先度が立っていて、すでに描画されている場合は無視
          continue;
        }

        this.frameBuffer[y * SCREEN_WIDTH + x] = color;
      }
    }
  }

  // MARK: drawSprites
  private void drawSprites() {
    if (!this.controls.objEnabled) return; // スプライトが無効な場合は何もしない
    for (int i = 0; i < this.sprites.length; i++) {
      int spriteX = this.sprites[i].x; // スプライトのX座標を計算
      int spriteY = this.sprites[i].y; // スプライトのY座標を計算
      Tile tile = this.tiles[this.sprites[i].tileIndex]; // スプライトのタイルを取得
      int attributes = this.sprites[i].attributes; // スプライトの属性を取得
      boolean priority = (attributes & 0x80) != 0; // スプライトの優先度を取得
      boolean yFlip = (attributes & 0x40) != 0; // Y軸反転フラグ
      boolean xFlip = (attributes & 0x20) != 0; // X軸反転フラグ
      int palette = (attributes & 0x10) >> 4; // パレット番号

      for (int tileX = 0; tileX < Tile.TILE_LENGTH; tileX++) {
        for (int tileY = 0; tileY < Tile.TILE_LENGTH; tileY++) {
          // まず反転を考慮したピクセル座標を計算
          int pixelX = xFlip ? (Tile.TILE_LENGTH - 1 - tileX) : tileX;
          int pixelY = yFlip ? (Tile.TILE_LENGTH - 1 - tileY) : tileY;

          // 反転座標からピクセルを取得
          TilePixelValue pixel = tile.pixels[pixelY][pixelX];

          if (pixel == TilePixelValue.Zero) continue;

          int color = Tile.getColorFromPalette(pixel, (palette == 0) ? this.obp0 : this.obp1);

          // 画面上の表示位置は元の座標を使用
          int x = spriteX + tileX - SPRITE_OFFSET_X;
          int y = spriteY + tileY - SPRITE_OFFSET_Y;

          if (x < 0 || y < 0 || x >= SCREEN_WIDTH || y >= SCREEN_HEIGHT) continue;

          this.frameBuffer[y * SCREEN_WIDTH + x] = color;
        }
      }
    }
  }

  // MARK: drawFrame
  private void drawFrame() {
    drawBackground(); // 背景の描画

    drawWindow(); // ウィンドウの描画

  }

  public int[] getFrame() {
    return this.frameBuffer;
  }

  public int readVRAM(int address) {
    return this.vram[address];
  }

  public void writeOAM(int address, int value) {
    this.oam[address - 0xFE00] = value; // OAMに書き込む
    for (int i = 0; i < this.sprites.length; i++) {
      int index = i * 4; // スプライトのインデックスを計算
      int y = this.oam[index];
      int x = this.oam[index + 1];
      int tileIndex = this.oam[index + 2];
      int attributes = this.oam[index + 3];

      this.sprites[i].index = i; // スプライトのインデックスを設定
      this.sprites[i].y = y; // スプライトのY座標を設定
      this.sprites[i].x = x; // スプライトのX座標を設定
      this.sprites[i].tileIndex = tileIndex;
      this.sprites[i].attributes = attributes;
    }
  }

  public int readOAM(int address) {
    return this.oam[address - 0xFE00];
  }

  public void writeVRAM(int index, int value) {
    this.vram[index] = value;

    if (index >= 0x1800) return; // タイルデータの範囲外であれば何もしない

    // タイルの色は 0x00 から 0x03 の4色で表現される
    // 0x00: 黒, 0x01: ダークグレー, 0x02: ライトグレー, 0x03: 白
    // そのためピクセルの値は2ビットで表現される
    // この色のデータはタイルの最初のバイトと2番目のバイトに格納されている
    // 1バイト目: 8ピクセル分の色データのlsb
    // 2バイト目: 8ピクセル分の色データのmsb
    //   0  1  2  3  4  5  6  7
    //  [0, 1, 0, 1, 0, 1, 0, 0]
    //  [0, 1, 1, 0, 0, 1, 1, 1]
    //   B  W  L  D  G  B  W  L


    int normalizedIndex = index & 0xFFFE;
    int byte1 = this.vram[normalizedIndex]; // 各タイルの最初のバイト
    int byte2 = this.vram[normalizedIndex + 1]; // 各タイルの2番目のバイト

    // VRAMの先頭から16バイトずつタイルのデータが格納されているため
    int tileIndex = index / 16; // タイルのインデックスを計算
    int rowIndex = (index % 16) / 2; // 行のインデックスを計算 (1行は2バイトのため)

    for (int pixelIndex = 0; pixelIndex < 8; pixelIndex++) {
      int mask = 1 << (7 - pixelIndex);
      boolean lsb = (byte1 & mask) != 0; // LSB
      boolean msb = (byte2 & mask) != 0; // MSB

      // ピクセルの色を決定
      TilePixelValue pixelValue;
      if (!lsb && !msb) {
        pixelValue = TilePixelValue.Zero; // 黒
      } else if (!lsb && msb) {
        pixelValue = TilePixelValue.One; // ダークグレー
      } else if (lsb && !msb) {
        pixelValue = TilePixelValue.Two; // ライトグレー
      } else {
        pixelValue = TilePixelValue.Three; // 白
      }

      this.tiles[tileIndex].pixels[rowIndex][pixelIndex] = pixelValue; // タイルのピクセル値を設定
    }
  }

  private PPUInterrupt setLCDStatus() {
    if (!this.controls.enabled) {
      this.scanlineCounter = 0; // スキャンラインカウンタをリセット
      this.ly = 0; // LYレジスタをリセット
      this.windowLine = 0; // ウィンドウラインをリセット
      this.status.PPUMode = 0; // モード0 に設定
      return PPUInterrupt.NONE;
    }

    int currentLine = this.ly; // 現在のラインを取得
    int currentMode = this.status.PPUMode; // 現在のモードを取得
    int mode = 0;
    boolean reqInt = false; // 割り込み要求フラグ

    if (currentLine >= 144) {
      mode = 1;
      this.status.PPUMode = 1; // VBlankモード
      reqInt = this.status.mode1IntSelect; // VBlankモードの割り込み要求
    } else {
      int mode2Bounds = 80; // モード2の時間 (80クロックサイクル)
      int mode3Bounds = 80 + 289; // モード3の時間 (172クロックサイクル)

      if (this.scanlineCounter < mode2Bounds) {
        mode = 2; // OAM読み込みモード
        this.status.PPUMode = 2; // OAM読み込みモード
        reqInt = this.status.mode2IntSelect; // OAM読み込みモードの割り込み要求
      } else if (this.scanlineCounter < mode3Bounds) {
        mode = 3; // VRAM読み込みモード
        this.status.PPUMode = 3; // VRAM読み込みモード
      } else {
        mode = 0; // HBlankモード
        if (this.status.PPUMode != 0) {
          drawScanline(currentLine); // スキャンラインを描画
        }
        this.status.PPUMode = 0; // HBlankモード
        reqInt = this.status.mode0IntSelect; // HBlankモードの割り込み要求
      }
    }

    PPUInterrupt interrupt = PPUInterrupt.NONE; // 割り込みの初期化

    if (reqInt && (mode != currentMode)) {
      // モードが変わった場合、割り込み要求をセット
      interrupt = PPUInterrupt.LCD; // LCD割り込みを要求
    }

    if (this.ly == this.lyc && this.prevLy != this.ly) {
      // LYCとLYが一致した場合、LYC=LY割り込みを要求
      this.status.lycFlag = true; // LYC=LY割り込みを要求

      if (this.status.lycIntSelect) {
        // LYC=LY割り込みが有効な場合、割り込みを要求
        interrupt = PPUInterrupt.LCD; // LCD割り込みを要求
      }
    } else {
      this.status.lycFlag = false; // LYC=LY割り込みをクリア
    }

    this.prevLy = this.ly; // LYレジスタの値を保存
    return interrupt; // 割り込みを返す
  }
}

class Tile {
  public static final int TILE_LENGTH = 8;
  public TilePixelValue[][] pixels = new TilePixelValue[TILE_LENGTH][TILE_LENGTH];

  Tile() {
    for (int i = 0; i < TILE_LENGTH; i++) {
      for (int j = 0; j < TILE_LENGTH; j++) {
        this.pixels[i][j] = TilePixelValue.Zero; // ピクセル値を初期化
      }
    }
  }

  // Tile emptyTile() {
  //   TilePixelValue[][] pixels = new TilePixelValue[Tile.TILE_LENGTH][Tile.TILE_LENGTH];
  //   for (int i = 0; i < Tile.TILE_LENGTH; i++) {
  //     for (int j = 0; j < Tile.TILE_LENGTH; j++) {
  //       pixels[i][j] = TilePixelValue.Zero;
  //     }
  //   }
  //   return new Tile(pixels);
  // }

  public static int getColorFromPalette(TilePixelValue pixelValue, int palette) {
    int[] colorTable = {
      0, // 0x00: 黒
      1, // 0x01: ダークグレー
      2, // 0x02: ライトグレー
      3, // 0x03: 白
    };

    switch (pixelValue) {
      case Zero:
        return colorTable[(palette & 0x03) >> 0]; // 黒
      case One:
        return colorTable[(palette & 0x0C) >> 2]; // ダークグレー
      case Two:
        return colorTable[(palette & 0x30) >> 4]; // ライトグレー
      case Three:
        return colorTable[(palette & 0xC0) >> 6]; // 白
      default:
        return 0; // 黒
    }
  }
}

class Sprite {
  public int y;
  public int x;
  public int tileIndex;
  public int attributes;
  public int index;

  public Sprite(int y, int x, int tileIndex, int attributes, int index) {
    this.y = y;
    this.x = x;
    this.tileIndex = tileIndex;
    this.attributes = attributes;
    this.index = index;
  }
}

enum TilePixelValue {
  Zero,
  One,
  Two,
  Three,
}
