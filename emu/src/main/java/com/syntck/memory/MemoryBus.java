package com.syntck.memory;
import com.syntck.cpu.CPU;
import com.syntck.gpu.GPU;

public class MemoryBus {
  public static final int MEMORY_SIZE = 0xFFFF; // 64KB of memory
  public int[] memory = new int[MEMORY_SIZE]; // Memory array
  public CPU cpu; // CPU instance
  public GPU gpu; // GPU instance

  public MemoryBus(CPU cpu) {
    this.cpu = cpu; // Initialize the CPU instance
  }

  public int readByte(int address) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    if (GPU.VRAM_BEGIN <= address && address <= GPU.VRAM_END) {
      return this.gpu.readVRAM(address - GPU.VRAM_BEGIN); // アドレスがVRAMの範囲内の場合、GPUから読み取る
    }
    return memory[address];
  }

  public void writeByte(int address, int value) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    if (GPU.VRAM_BEGIN <= address && address <= GPU.VRAM_END) {
      this.gpu.writeVRAM(address - GPU.VRAM_BEGIN, value); // アドレスがVRAMの範囲内の場合、GPUに書き込む
      return;
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
