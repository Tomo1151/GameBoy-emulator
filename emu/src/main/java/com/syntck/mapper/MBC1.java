package com.syntck.mapper;

public class MBC1 implements Mapper {
  private int[] binaryData;
  private int romSize;
  private int ramSize;

  private int bank = 0x01; // バンク番号

  public MBC1(int[] binaryData, int romSize, int ramSize) {
    init(binaryData, romSize, ramSize);
  }

  public void init(int[] binaryData, int romSize, int ramSize) {
    this.binaryData = binaryData; // バイナリデータを保存
    this.romSize = romSize; // ROMサイズを保存
    this.ramSize = ramSize; // RAMサイズを保存
  }

  public int readByte(int address) {
    if (0x0000 <= address && address <= 0x3FFF) {
      return this.binaryData[address]; // ROMの最初の16KBは常に読み取れる
    }

    if (0x4000 <= address && address <= 0x7FFF) {
      int bank = this.bank & 0x1F; // 5ビットのバンク番号を取得
      if (bank == 0) {
        bank = 1;
      }

      int bankAddress = address + ((bank - 1) * 0x4000); // バンク番号を考慮してアドレスを計算
      return this.binaryData[bankAddress]; // バンクのROMデータを読み取る
    }

    return 0;
  }

  public void writeByte(int address, int value) {
    if (0x0000 <= address && address <= 0x1FFF) {
      // RAM有効化フラグ
    }
    if (0x2000 <= address && address <= 0x3FFF) {
      this.bank = value;
    }
    if (0x4000 <= address && address <= 0x5FFF) {
      // RAMバンク番号 || ROMバンク番号の上位ビット
    }
    if (0x6000 <= address && address <= 0x7FFF) {
      // ROMバンク番号の下位ビット
    }
  }
}
