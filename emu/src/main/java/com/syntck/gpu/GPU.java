package com.syntck.gpu;

public class GPU {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int VRAM_BEGIN = 0x8000;
  public static final int VRAM_END   = 0x9FFF;
  public static final int VRAM_SIZE  = VRAM_END - VRAM_BEGIN + 1;

  // レジスタ
  public int ly; // 0xFF44 LYレジスタ (現在のスキャンラインのY座標)
  public int lyc; // 0xFF45 LYCレジスタ (LYと比較するY座標)
  public LCDControlRegisters controls; // 0xFF40 LCD制御レジスタ (LCDの制御)
  public LCDStatusRegisters status; // 0xFF41 LCDステータスレジスタ (LCDの状態を示すフラグ)
  public boolean frameUpdated;

  public int[] vram = new int[VRAM_SIZE];
  public Tile[] tiles = new Tile[384];
  public Sprite[] sprites = new Sprite[40]; // スプライトの数は40個

  private int[] frameBuffer = new int[SCREEN_WIDTH * SCREEN_HEIGHT];
  private int scanlineCounter;

  private int[] oam = new int[0xA0]; // OAM (Object Attribute Memory) (スプライトの情報を格納するメモリ)

  public GPU() {
    this.vram = new int[VRAM_SIZE];
    this.controls = new LCDControlRegisters(); // LCD制御レジスタの初期化
    this.status = new LCDStatusRegisters(); // LCDステータスレジスタの初期化
    this.ly = 0; // LYレジスタの初期化
    this.lyc = 0; // LYCレジスタの初期化
    this.scanlineCounter = 0; // スキャンラインカウンタの初期化
    this.tiles = new Tile[384]; // 384 tiles
    for (int i = 0; i < this.vram.length; i++) {
      this.vram[i] = 0; // Initialize VRAM with 0
    }
    for (int i = 0; i < this.tiles.length; i++) {
      this.tiles[i] = new Tile(new TilePixelValue[Tile.TILE_LENGTH][Tile.TILE_LENGTH]);
    }
    for (int i = 0; i < this.oam.length; i++) {
      this.oam[i] = 0; // Initialize OAM with 0
    }
    for (int i = 0; i < this.sprites.length; i++) {
      this.sprites[i] = new Sprite(0, 0, 0, 0); // Initialize sprites with default values
    }
  }

  public boolean update(int cycles) {
    // setLCDStatus();
    // if (!isLCDEnabled) return;

    this.scanlineCounter += cycles;

    if (this.scanlineCounter >= 456) {
      // 1ライン分描画された
      this.scanlineCounter -= 456;

      // LYレジスタをインクリメント
      this.ly++;
      int currentLine = this.ly; // 現在のラインを取得

      if (currentLine == 144) {
        // VBlank開始
        // VBlankフラグをセット
        this.drawFrame();
      } else if (currentLine < 144) {
        // 画面描画中
        this.drawScanline(currentLine);
        this.frameUpdated = true; // フレームが更新されたことを示すフラグをセット
        return true;
      } else if (currentLine > 153) {
        // 1フレーム描画完了
        this.ly = 0; // LYをリセット
      }
    }
    return false;
  }

  private void drawScanline(int scanline) {

  }

  private void drawFrame() {
    // this.frameBuffer を全部書き換える
    for (int addr = 0x9800; addr <= 0x9BFF; addr++) {
      // System.out.println("addr: " + String.format("0x%04X", addr));
      int vramAddr = addr - VRAM_BEGIN; // タイルのインデックスが保存されている先頭アドレスをVRAMのアドレスに変換
      // System.out.println("vramAddr: " + String.format("0x%04X", vramAddr));
      int index = vramAddr - 0x1800;
      // System.out.println("index: " + String.format("0x%04X", index));
      Tile tile = this.tiles[this.vram[vramAddr]]; // タイルを取得
      // System.out.println("tile: " + tile);

      int screenX = ((index) % 32) * Tile.TILE_LENGTH; // タイルのX座標の始点を計算
      int screenY = ((index) / 32) * Tile.TILE_LENGTH; // タイルのY座標の始点を計算
      // System.out.println("sx: " + screenX + ", sy: " + screenY);


      // System.out.println("Tile print");
      for (int tileX = 0; tileX < Tile.TILE_LENGTH; tileX++) {
        for (int tileY = 0; tileY < Tile.TILE_LENGTH; tileY++) {
          TilePixelValue pixel = tile.pixels[tileY][tileX]; // タイルのピクセル値を取得
          int x = screenX + tileX; // タイルのX座標を計算
          int y = screenY + tileY; // タイルのY座標を計算

          if (x >= SCREEN_WIDTH || y >= SCREEN_HEIGHT) continue; // 画面外のタイルは無視

          int pixelNum = 0;
          if (pixel == TilePixelValue.Zero) {
            pixelNum = 0; // 黒
          } else if (pixel == TilePixelValue.One) {
            pixelNum = 1; // ダークグレー
          } else if (pixel == TilePixelValue.Two) {
            pixelNum = 2; // ライトグレー
          } else if (pixel == TilePixelValue.Three) {
            pixelNum = 3; // 白
          }

          // System.out.print(pixelNum + " ");
          this.frameBuffer[y * SCREEN_WIDTH + x] = pixelNum; // ピクセル値をフレームバッファに書き込む
        }
        // System.out.println();
      }
      // System.out.println();
    }
  }

  public int[] getFrames() {
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
}

class Tile {
  public static final int TILE_LENGTH = 8;
  public TilePixelValue[][] pixels = new TilePixelValue[TILE_LENGTH][TILE_LENGTH];

  Tile(TilePixelValue[][] pixels) {
    for (int i = 0; i < TILE_LENGTH; i++) {
      for (int j = 0; j < TILE_LENGTH; j++) {
        this.pixels[i][j] = pixels[i][j];
      }
    }
  }

  Tile emptyTile() {
    TilePixelValue[][] pixels = new TilePixelValue[Tile.TILE_LENGTH][Tile.TILE_LENGTH];
    for (int i = 0; i < Tile.TILE_LENGTH; i++) {
      for (int j = 0; j < Tile.TILE_LENGTH; j++) {
        pixels[i][j] = TilePixelValue.Zero;
      }
    }
    return new Tile(pixels);
  }
}

class Sprite {
  public int y;
  public int x;
  public int tileIndex;
  public int attributes;

  public Sprite(int y, int x, int tileIndex, int attributes) {
    this.y = y;
    this.x = x;
    this.tileIndex = tileIndex;
    this.attributes = attributes;
  }
}

enum TilePixelValue {
  Zero,
  One,
  Two,
  Three,
}
