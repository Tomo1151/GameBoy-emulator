package com.syntck.mapper;

public class MBC1 implements Mapper {
  private int[] binaryData;
  private int[] ram;
  private int romSize;
  private int ramSize;
  private int secondaryBank;
  private int ramBank;
  private int mode;
  private boolean ramEnabled;
  private boolean isBigROM;
  private boolean isBigRAM;

  private int bank = 0x01; // バンク番号

  public MBC1(int[] binaryData, int[] ram, int romSize, int ramSize) {
    init(binaryData, ram, romSize, ramSize);
  }

  public void init(int[] binaryData, int[] ram, int romSize, int ramSize) {
    this.binaryData = binaryData; // バイナリデータを保存
    this.romSize = romSize; // ROMサイズを保存
    this.ramSize = ramSize; // RAMサイズを保存
    this.secondaryBank = 0; // 拡張バンク番号を初期化
    this.ramBank = 0; // RAMバンク番号を初期化
    this.ramEnabled = false;
    this.mode = 1;
    this.isBigROM = (this.binaryData.length >= 1024 * 1024); // ROMが1MB以上かどうかを判定
    this.ram = ram;
    this.isBigRAM = (this.ramSize >= 8 * 1024); // RAMが8KB以上かどうかを判定
  }

  public int readByte(int address) {
    if (this.mode == 0) {
      return readByteBasic(address); // 基本モードで読み取る
    } else {
      return readByteAdvanced(address); // 高度なモードで読み取る
    }
  }

  private int calcBank1Address(int address) {
    int bannk = this.bank & 0x1F;
    if (bank == 0) bank = 1;
    int bankAddress = address + (bank * 0x4000); // バンク番号を考慮してアドレスを計算
    return bankAddress - 0x4000; // バンクのROMデータを読み取る
  }

  private int calcBank1AddressWithSecondary(int address) {
    int bank = ((this.secondaryBank & 0x03) << 5) | (this.bank & 0x1F); // 5ビットのバンク番号を取得
    if (bank == 0) bank = 1;
    int bankAddress = address + (bank * 0x4000); // バンク番号を考慮してアドレスを計算
    return bankAddress - 0x4000; // バンクのROMデータを読み取る
  }

  public int readByteBasic(int address) {
    // bank 0
    if (0x0000 <= address && address <= 0x3FFF) {
      return this.binaryData[address]; // ROMの最初の16KBは常に読み取れる
    }

    // bank 1
    if (0x4000 <= address && address <= 0x7FFF) {
      if (!this.isBigRAM && this.isBigROM) {
        return this.binaryData[calcBank1Address(address)]; // バンクのROMデータを読み取る
      } else {
        return this.binaryData[calcBank1AddressWithSecondary(address)];
      }
    }

    // RAM
    if (0xA000 <= address && address <= 0xBFFF) {
      // RAMにアクセス
      if (this.ramEnabled) {
        return this.ram[address - 0xA000];
      }
    }

    return 0;
  }

  public int readByteAdvanced(int address) {
    // bank 0
    if (0x0000 <= address && address <= 0x3FFF) {
      if (this.isBigROM) {
        int bank = (this.secondaryBank & 0x03) << 5; // 5ビットのバンク番号を取得
        int bankAddress = address + (this.bank  * 0x4000); // バンク番号を考慮してアドレスを計算
        return this.binaryData[bankAddress]; // バンクのROMデータを読み取る
      } else {
        return this.binaryData[address]; // ROMの最初の16KBは常に読み取れる
      }
    }

    // bank 1
    if (0x4000 <= address && address <= 0x7FFF) {
      if (this.isBigROM) {
        return this.binaryData[calcBank1AddressWithSecondary(address)]; // バンクのROMデータを読み取る
      } else {
        return this.binaryData[calcBank1Address(address)]; // バンクのROMデータを読み取る
      }
    }

    // RAM
    if (0xA000 <= address && address <= 0xBFFF) {
      if (this.isBigRAM) {
        return this.ram[address - 0xA000]; // RAMのデータを読み取る
      } else {
        int bank = (this.secondaryBank & 0x03);
        int addr = address + (bank * 0x2000); // バンク番号を考慮してアドレスを計算
        return this.ram[addr - 0xA000]; // RAMのデータを読み取る
      }
    }

    return 0;
  }

  public void writeByte(int address, int value) {
    if (0x0000 <= address && address <= 0x1FFF) {
      // RAM有効化フラグ
      this.ramEnabled = (value & 0x0A) == 0x0A; // RAMを有効化する
    }
    if (0x2000 <= address && address <= 0x3FFF) {
      this.bank = value;
    }
    if (0x4000 <= address && address <= 0x5FFF) {
      // RAMバンク番号 || ROMバンク番号の上位ビット
      this.secondaryBank = value;
    }
    if (0x6000 <= address && address <= 0x7FFF) {
      // ROMバンク番号の下位ビット
      this.mode = value; // モードを設定
    }
    if (0xA000 <= address && address <= 0xBFFF) {
      if (!this.ramEnabled || this.ram == null || this.ramSize == 0) return;

      if (this.mode == 0x00) {
        int index = address - 0xA000;
        if (index < 0 || index >= this.ram.length) return; // 範囲チェック
        this.ram[index] = value;
      } else {
        if (this.isBigRAM) {
          int bank = (this.secondaryBank & 0x03);
          int addr = address + (bank * 0x2000);
          int index = addr - 0xA000;
          if (index < 0 || index >= this.ram.length) return; // 範囲チェック
          this.ram[index] = value;
        } else {
          int index = address - 0xA000;
          if (index < 0 || index >= this.ram.length) return; // 範囲チェック
          this.ram[index] = value;
        }
      }
    }
  }
}
