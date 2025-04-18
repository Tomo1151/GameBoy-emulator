package com.syntck.memory;
import com.syntck.cpu.CPU;
import com.syntck.cartridge.Cartridge;
import com.syntck.joypad.Joypad;
import com.syntck.ppu.PPU;

public class MemoryBus {
  public static final int MEMORY_SIZE = 0xFFFF; // 64KB of memory
  public int[] memory = new int[MEMORY_SIZE+1]; // Memory array
  public CPU cpu; // CPU instance
  public PPU gpu; // GPU instance
  public Cartridge cartridge; // Cartridge instance
  public Joypad joypad; // Joypad instance

  public MemoryBus(CPU cpu, Cartridge cartridge) {
    this.cpu = cpu; // Initialize the CPU instance
    this.gpu = new PPU(); // Initialize the GPU instance
    this.cartridge = cartridge; // Initialize the cartridge instance
    this.joypad = new Joypad(); // Initialize the joypad instance
  }

  public int readByte(int address) {
    if (address < 0 || address > MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    // ROMがなければ、メモリから直接読み取る (CPUテスト用の苦肉の策)
    if (this.cartridge == null) return memory[address];
    if (address == 0xFF50) return memory[address];

    if (0x0000 <= address && address <= 0x7FFF) {
      return this.cartridge.readByte(address); // Read from the cartridge if address is in ROM range
    }

    if (0xA000 <= address && address <= 0xBFFF) {
      return this.cartridge.readByte(address);
    }

    // OAM
    if (0xFE00 < address && address <= 0xFE9F) {
      return this.gpu.readOAM(address); // OAMから読み取る
    }

    if (address == 0xFF42) {
      return this.gpu.scy; // SCYレジスタの値を返す
    } else if (address == 0xFF43) {
      return this.gpu.scx; // SCXレジスタの値を返す
    } else if (address == 0xFF46) {
      return this.gpu.dma; // DMAレジスタの値を返す
    } else if (address == 0xFF47) {
      return this.gpu.bgp; // BGPレジスタの値を返す
    } else if (address == 0xFF48) {
      return this.gpu.obp0; // OBP0レジスタの値を返す
    } else if (address == 0xFF49) {
      return this.gpu.obp1; // OBP1レジスタの値を返す
    } else if (address == 0xFF4A) {
      return this.gpu.wy; // WYレジスタの値を返す
    } else if (address == 0xFF4B) {
      return this.gpu.wx; // WXレジスタの値を返す
    }

    if (address == 0xFF41) {
    return this.gpu.status.convertToInt(); // LCDステータスレジスタの値を返す
    } else if (address == 0xFF40) {
      return this.gpu.controls.convertToInt();
    } else if (address == 0xFF44) {
      // if (this.cpu.debug) System.out.println("LYレジスタへの読み取りが発生しました" + String.format("- 0x%04X", this.gpu.ly)); // LYレジスタは読み取らない
      return this.gpu.ly; // LYレジスタの値を返す
    } else if (address == 0xFF45) {
      return this.gpu.lyc; // LYCレジスタの値を返す
    } else if (PPU.VRAM_BEGIN <= address && address <= PPU.VRAM_END) {
      return this.gpu.readVRAM(address - PPU.VRAM_BEGIN); // アドレスがVRAMの範囲内の場合、GPUから読み取る
    }
    if (address == 0xFF00) {
      return this.joypad.read(); // Joypadから読み取る
    }

    return memory[address];
  }

  public void writeByte(int address, int value) {
    if (address < 0 || address > MEMORY_SIZE) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }

    if (address == 0xFF01) {
      System.out.print((char) value); // 0xFF01はコンソールに出力する
    }

    // DIVカウンタ
    if (address == 0xFF04) {
      this.memory[address] = 0;
    }

    // DMA転送
    if (address == 0xFF46) {
      handleDMATransfer(value); // DMA転送を処理する
    }

    // OAM
    if (0xFE00 <= address && address <= 0xFE9F) {
      this.gpu.writeOAM(address, value); // OAMに書き込む
    }

    if (address == 0xFF42) {
      this.gpu.scy = value; // SCYレジスタに値を設定
    } else if (address == 0xFF43) {
      this.gpu.scx = value; // SCXレジスタに値を設定
    } else if (address == 0xFF47) {
      this.gpu.bgp = value; // BGPレジスタに値を設定
    } else if (address == 0xFF48) {
      this.gpu.obp0 = value; // OBP0レジスタに値を設定
    } else if (address == 0xFF49) {
      this.gpu.obp1 = value; // OBP1レジスタに値を設定
    } else if (address == 0xFF4A) {
      this.gpu.wy = value; // WYレジスタに値を設定
    } else if (address == 0xFF4B) {
      this.gpu.wx = value; // WXレジスタに値を設定
    }

    if (address == 0xFF50) this.memory[address] = value; // 0xFF50は無視する

    if (0x0000 <= address && address <= 0x7FFF) {
      this.cartridge.writeByte(address, value); // カートリッジに書き込む
    }
    if (0xA000 <= address && address <= 0xBFFF) {
      this.cartridge.writeByte(address, value); // カートリッジに書き込む
    }


    if (address == 0xFF40) {
      this.gpu.controls.convertFromInt(value); // LCD制御レジスタに値を設定
    } else if (address == 0xFF41) {
      this.gpu.status.convertFromInt(value); // LCDステータスレジスタに値を設定
    } else if (address == 0xFF45) {
      this.gpu.lyc = value; // LYCレジスタに値を設定
    } else if (address == 0xFF44) {
      throw new UnsupportedOperationException("LYレジスタへの書き込みが発生しました"); // LYレジスタには書き込まない
    } else if (PPU.VRAM_BEGIN <= address && address <= PPU.VRAM_END) {
      this.gpu.writeVRAM(address - PPU.VRAM_BEGIN, value); // アドレスがVRAMの範囲内の場合、GPUに書き込む
      return;
    }

    if (address == 0xFF00) {
      this.joypad.write(value); // Joypadに書き込む
    }

    memory[address] = value; // Write a byte to the specified address
  }

  public void writeWord(int address, int value) {
    if (address < 0 || address > MEMORY_SIZE - 1) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    writeByte(address, (value & 0xFF)); // Write the lower byte
    writeByte(address + 1, (value >> 8));
  }

  public int readWord(int address) {
    if (address < 0 || address > MEMORY_SIZE - 1) {
      throw new IllegalArgumentException("Address out of bounds: " + String.format("0x%04X", address));
    }
    return readByte(address) | readByte(address + 1) << 8; // Read a word from the specified address
  }

  public void handleDMATransfer(int value) {
    int address = (value << 8) & 0xFFFF; // DMA転送のアドレスを計算
    for (int i = 0; i < 0xA0; i++) {
      this.writeByte(0xFE00 + i, this.readByte(address + i)); // OAMにDMA転送する
    }
  }

  public void clear() {
    for (int i = 0; i < MEMORY_SIZE; i++) {
      memory[i] = 0; // Clear the memory
    }
  }
}
