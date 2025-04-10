package com.syntck.mapper;

public interface Mapper {
  void init(int[] binaryData, int romSize, int ramSize);
  int readByte(int address);
  void writeByte(int address, int value);
}
