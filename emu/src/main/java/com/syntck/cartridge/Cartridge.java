package com.syntck.cartridge;

import java.io.FileInputStream;

import com.syntck.mapper.MBC1;
import com.syntck.mapper.Mapper;

public class Cartridge {
  public int[] binaryData; // ROMのバイナリデータを格納する配列
  public boolean isCGB; // CGBフラグ
  public boolean isSGB; // SGBフラグ
  public CartridgeType cartridgeType; // カートリッジタイプ
  public RomSize romSize; // ROMサイズ
  public RamSize ramSize; // RAMサイズ
  public int version; // ROMバージョン
  public Mapper mapper; // マッパー

  public Cartridge(String file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      // ROMのサイズを取得
      int fileSize = (int) fis.getChannel().size();
      binaryData = new int[fileSize]; // バイナリデータの配列を初期化

      // ROMのバイナリデータを読み込む
      for (int i = 0; i < fileSize; i++) {
        binaryData[i] = fis.read(); // 1バイトずつ読み込む
      }

      this.isCGB = (binaryData[0x0143] == 0xC0); // CGBフラグを取得
      this.isSGB = (binaryData[0x0146] == 0x03); // SGBフラグを取得
      setRomSize(binaryData[0x0148]); // ROMサイズを取得
      setRamSize(binaryData[0x0149]); // RAMサイズを取得
      this.version = binaryData[0x014C]; // ROMのバージョンを取得

      setCartridgeType(binaryData[0x0147]); // カートリッジタイプを取得

    } catch (Exception e) {
      e.printStackTrace(); // エラーが発生した場合はスタックトレースを表示
    }
  }

  public int readByte(int address) {
    return this.mapper.readByte(address); // マッパーを使用してバイトを読み取る
  }

  public void writeByte(int address, int value) {
    this.mapper.writeByte(address, value);
  }

  public void setRamSize(int ramSize) {
    switch (ramSize) {
      case 0x00:
        this.ramSize = RamSize.NO_RAM;
        break;
      case 0x01:
        this.ramSize = RamSize.UNUSED;
        break;
      case 0x02:
        this.ramSize = RamSize.BANK_1;
        break;
      case 0x03:
        this.ramSize = RamSize.BANK_4;
        break;
      case 0x04:
        this.ramSize = RamSize.BANK_16;
        break;
      case 0x05:
        this.ramSize = RamSize.BANK_8;
        break;
      default:
        throw new IllegalArgumentException("Invalid RAM size: " + ramSize);
    }
  }

  public void setRomSize(int romSize) {
    switch (romSize) {
      case 0x00:
        this.romSize = RomSize.BANK_2;
        break;
      case 0x01:
        this.romSize = RomSize.BANK_4;
        break;
      case 0x02:
        this.romSize = RomSize.BANK_8;
        break;
      case 0x03:
        this.romSize = RomSize.BANK_16;
        break;
      case 0x04:
        this.romSize = RomSize.BANK_32;
        break;
      case 0x05:
        this.romSize = RomSize.BANK_64;
        break;
      case 0x06:
        this.romSize = RomSize.BANK_128;
        break;
      case 0x07:
        this.romSize = RomSize.BANK_256;
        break;
      case 0x08:
        this.romSize = RomSize.BANK_512;
        break;
      case 0x52:
        this.romSize = RomSize.BANK_72;
        break;
      case 0x53:
        this.romSize = RomSize.BANK_80;
        break;
      case 0x54:
        this.romSize = RomSize.BANK_96;
        break;
      default:
        throw new IllegalArgumentException("Invalid ROM size: " + romSize);
    }
  }

  public void setCartridgeType(int cartridgeType) { // TODO MBC1以外のマッパーを実装する
    switch (cartridgeType) {
      case 0x00:
        this.cartridgeType = CartridgeType.ROM_ONLY;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x01:
        this.cartridgeType = CartridgeType.MBC1;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x02:
        this.cartridgeType = CartridgeType.MBC2;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x03:
        this.cartridgeType = CartridgeType.ROM_RAM;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x04:
        this.cartridgeType = CartridgeType.MBC3_TIMER_BATTERY;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x05:
        this.cartridgeType = CartridgeType.MBC5;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x06:
        this.cartridgeType = CartridgeType.MBC6;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x07:
        this.cartridgeType = CartridgeType.MBC7_SENSOR;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x08:
        this.cartridgeType = CartridgeType.POCKET_CAMERA;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x09:
        this.cartridgeType = CartridgeType.BANDAI_TAMA5;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x0A:
        this.cartridgeType = CartridgeType.HuC3;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      case 0x0B:
        this.cartridgeType = CartridgeType.HuC1_RAM_BATTERY;
        this.mapper = new MBC1(this.binaryData, this.romSize.getValue(), this.ramSize.getValue());
        break;
      default:
        throw new IllegalArgumentException("Invalid cartridge type: " + cartridgeType);
    }
  }

  public void dump(int start, int end) {
    if (this.binaryData == null) {
      System.out.println("No binary data loaded.");
      return;
    }

    if (start < 0 || end > binaryData.length || start >= end) {
      throw new IllegalArgumentException("Invalid range for dump: " + String.format("0x%04X - 0x%04X", start, end));
    }

    System.out.println("Cartridge loaded: \n" +
    "CGB: " + this.isCGB + "\n" +
    "SGB: " + this.isSGB + "\n" +
    "Cartridge Type: " + this.cartridgeType + "\n" +
    "ROM Size: " + this.romSize + "\n" +
    "RAM Size: " + this.ramSize + "\n" +
    "Version: " + this.version);

    for (int i = start; i <= end; i++) {
      System.out.printf("%02X ", binaryData[i]); // 16進数で表示
      if ((i - start + 1) % 16 == 0) {
        System.out.println(); // 16バイトごとに改行
      }
    }
    System.out.println(); // 最後に改行
  }
}

enum CartridgeType {
  ROM_ONLY(0x00),
  MBC1(0x01),
  MBC2(0x02),
  ROM_RAM(0x03),
  MBC3_TIMER_BATTERY(0x04),
  MBC5(0x05),
  MBC6(0x06),
  MBC7_SENSOR(0x07),
  POCKET_CAMERA(0x08),
  BANDAI_TAMA5(0x09),
  HuC3(0x0A),
  HuC1_RAM_BATTERY(0x0B);

  private final int value;

  CartridgeType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

enum RomSize {
  BANK_2(0x00),
  BANK_4(0x01),
  BANK_8(0x02),
  BANK_16(0x03),
  BANK_32(0x04),
  BANK_64(0x05),
  BANK_128(0x06),
  BANK_256(0x07),
  BANK_512(0x08),
  BANK_72(0x52),
  BANK_80(0x53),
  BANK_96(0x54);

  private final int value;

  RomSize(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}

enum RamSize {
  NO_RAM(0x00),
  UNUSED(0x01),
  BANK_1(0x02),
  BANK_4(0x03),
  BANK_16(0x04),
  BANK_8(0x05);

  private final int value;

  RamSize(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}