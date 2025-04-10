package com.syntck.memory;
import com.syntck.cpu.CPU;
import com.syntck.gpu.GPU;
import com.syntck.cartridge.Cartridge;

public class MemoryBus {
  public static final int MEMORY_SIZE = 0xFFFF; // 64KB of memory
  public int[] memory = new int[MEMORY_SIZE]; // Memory array
  public CPU cpu; // CPU instance
  public GPU gpu; // GPU instance
  public Cartridge cartridge; // Cartridge instance

  public MemoryBus(CPU cpu, Cartridge cartridge) {
    this.cpu = cpu; // Initialize the CPU instance
    this.gpu = new GPU(); // Initialize the GPU instance
    this.cartridge = cartridge; // Initialize the cartridge instance
  }

  public int readByte(int address) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    // ROMがなければ、メモリから直接読み取る (CPUテスト用の苦肉の策)
    if (this.cartridge == null) return memory[address];
    if (address == 0xFF50) return memory[address];

    if (0x0000 <= address && address <= 0x8000) {
      return this.cartridge.readByte(address); // Read from the cartridge if address is in ROM range
    }
    if (address == 0xFF41) {
    return this.gpu.status.convertToInt(); // LCDステータスレジスタの値を返す
    } else if (address == 0xFF40) {
      return this.gpu.controls.convertToInt();
    } else if (address == 0xFF44) {
      return this.gpu.ly; // LYレジスタの値を返す
    } else if (address == 0xFF45) {
      return this.gpu.lyc; // LYCレジスタの値を返す
    } else if (GPU.VRAM_BEGIN <= address && address <= GPU.VRAM_END) {
      return this.gpu.readVRAM(address - GPU.VRAM_BEGIN); // アドレスがVRAMの範囲内の場合、GPUから読み取る
    }
    return memory[address];
  }

  public void writeByte(int address, int value) {
    if (address < 0 || address >= MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    if (address == 0xFF50) this.memory[address] = value; // 0xFF50は無視する

    if (address == 0xFF40) {
      this.gpu.controls.convertFromInt(value); // LCD制御レジスタに値を設定
    } else if (address == 0xFF41) {
      this.gpu.status.convertFromInt(value); // LCDステータスレジスタに値を設定
    } else if (address == 0xFF45) {
      this.gpu.lyc = value; // LYCレジスタに値を設定
    } else if (address == 0xFF44) {
      throw new UnsupportedOperationException("LYレジスタへの書き込みが発生しました"); // LYレジスタには書き込まない
    } else if (GPU.VRAM_BEGIN <= address && address <= GPU.VRAM_END) {
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
