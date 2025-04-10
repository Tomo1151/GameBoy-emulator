package com.syntck.gpu;

public class GPU {
  public static final int VRAM_BEGIN = 0x8000;
  public static final int VRAM_END   = 0x9FFF;
  public static final int VRAM_SIZE  = VRAM_END - VRAM_BEGIN + 1;

  public int[] vram = new int[VRAM_SIZE];
  public Tile[] tiles = new Tile[384];

  GPU() {
    this.vram = new int[VRAM_SIZE];
    this.tiles = new Tile[384]; // 384 tiles
    for (int i = 0; i < this.vram.length; i++) {
      this.vram[i] = 0; // Initialize VRAM with 0
    }
    for (int i = 0; i < this.tiles.length; i++) {
      this.tiles[i] = new Tile(new TilePixelValue[Tile.TILE_LENGTH][Tile.TILE_LENGTH]);
    }
  }

  public int readVRAM(int address) {
    return this.vram[address];
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

enum TilePixelValue {
  Zero,
  One,
  Two,
  Three,
}
