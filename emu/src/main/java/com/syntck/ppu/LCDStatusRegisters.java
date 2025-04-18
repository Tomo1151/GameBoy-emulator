package com.syntck.ppu;

public class LCDStatusRegisters { // FF41
  public boolean lycIntSelect; // LYC=LY interrupt select
  public boolean mode2IntSelect; // Mode 2 OAM interrupt select
  public boolean mode1IntSelect; // Mode 1 VBlank interrupt select
  public boolean mode0IntSelect; // Mode 0 HBlank interrupt select
  public boolean lycFlag; // LYC==LY flag
  public int PPUMode; // (2bit) PPU mode

  public LCDStatusRegisters() {
    this.lycIntSelect = false; // LYC=LY interrupt select
    this.mode2IntSelect = false; // Mode 2 OAM interrupt select
    this.mode1IntSelect = false; // Mode 1 VBlank interrupt select
    this.mode0IntSelect = false; // Mode 0 HBlank interrupt select
    this.lycFlag = false; // LYC==LY flag
    this.PPUMode = 0; // (2bit) PPU mode
  }

  public int convertToInt() {
    int result = 0x00;
    if (this.lycIntSelect) result |= (1 << 6);
    if (this.mode2IntSelect) result |= (1 << 5);
    if (this.mode1IntSelect) result |= (1 << 4);
    if (this.mode0IntSelect) result |= (1 << 3);
    if (this.lycFlag) result |= (1 << 2);
    result |= this.PPUMode; // PPU mode
    return result;
  }

  public void convertFromInt(int bytes) {
    this.lycIntSelect = (bytes & (1 << 6)) != 0x0000;
    this.mode2IntSelect = (bytes & (1 << 5)) != 0x0000;
    this.mode1IntSelect = (bytes & (1 << 4)) != 0x0000;
    this.mode0IntSelect = (bytes & (1 << 3)) != 0x0000;
    this.lycFlag = (bytes & (1 << 2)) != 0x0000;
    this.PPUMode = bytes & 0x03; // PPU mode
  }
}
