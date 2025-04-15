package com.syntck.mapper;

public class NoMBC implements Mapper {
  private int[] binaryData;
  private int[] ram;
  private int romSize;
  private int ramSize;

  public NoMBC(int[] binaryData, int[] ram, int romSize, int ramSize) {
    init(binaryData, ram, romSize, ramSize);
  }

  public void init(int[] binaryData, int[] ram, int romSize, int ramSize) {
    this.binaryData = binaryData; // バイナリデータを保存
    this.ram = ram; // RAMを保存
    this.romSize = romSize; // ROMサイズを保存
    this.ramSize = ramSize; // RAMサイズを保存
  }

  public int readByte(int address) {
    return this.binaryData[address]; // ROMを直接読み取る
  }

  public void writeByte(int address, int value) {
    throw new UnsupportedOperationException("NoMBC does not support writing to ROM"); // NoMBCはROMへの書き込みをサポートしない
  }
}
