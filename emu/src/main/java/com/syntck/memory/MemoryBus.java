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

  public void writeWord(int address, int value) {
    if (address < 0 || address >= MEMORY_SIZE - 1) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    memory[address] = value & 0xFF; // Write the lower byte
    memory[address + 1] = (value >> 8) & 0xFF; // Write the upper byte
  }

  public int readWord(int address) {
    if (address < 0 || address >= MEMORY_SIZE - 1) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    return (memory[address] & 0xFF) | ((memory[address + 1] & 0xFF) << 8); // Read a word from the specified address
  }
}
