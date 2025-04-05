package com.syntck.memory;

public class MemoryBus {
  public static final int MEMORY_SIZE = 0xFFFF; // 64KB of memory
  public int[] memory = new int[MEMORY_SIZE]; // Memory array

  public int readByte(int address) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    return memory[address]; // Read a byte from the specified address
  }

  public void writeByte(int address, int value) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    memory[address] = value; // Write a byte to the specified address
  }
}
