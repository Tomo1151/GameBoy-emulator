package com.syntck.ppu;

public class LCDControlRegisters {
  public boolean enabled; // LCD & PPU enable
  public boolean windowTileMap; // Window tile map
  public boolean windowEnabled; // Window enable
  public boolean tiles; // BG & Window tiles
  public boolean bgTileMap; // BG tile map
  public boolean objSize; // OBJ size
  public boolean objEnabled; // OBJ enable
  public boolean bgWindowEnabled; // BG & window enable / priority

  public LCDControlRegisters() {
    this.enabled = true; // LCD & PPU enable
    this.windowTileMap = false; // Window tile map
    this.windowEnabled = false; // Window enable
    this.tiles = true; // BG & Window tiles
    this.bgTileMap = false; // BG tile map
    this.objSize = false; // OBJ size
    this.objEnabled = false; // OBJ enable
    this.bgWindowEnabled = true; // BG & window enable / priority
  }

  public int convertToInt() {
    int result = 0x00;
    if (this.enabled) result |= (1 << 7);
    if (this.windowTileMap) result |= (1 << 6);
    if (this.windowEnabled) result |= (1 << 5);
    if (this.tiles) result |= (1 << 4);
    if (this.bgTileMap) result |= (1 << 3);
    if (this.objSize) result |= (1 << 2);
    if (this.objEnabled) result |= (1 << 1);
    if (this.bgWindowEnabled) result |= (1 << 0);
    return result;
  }

  public void convertFromInt(int bytes) {
    this.enabled = (bytes & (1 << 7)) != 0x0000;
    this.windowTileMap = (bytes & (1 << 6)) != 0x0000;
    this.windowEnabled = (bytes & (1 << 5)) != 0x0000;
    this.tiles = (bytes & (1 << 4)) != 0x0000;
    this.bgTileMap = (bytes & (1 << 3)) != 0x0000;
    this.objSize = (bytes & (1 << 2)) != 0x0000;
    this.objEnabled = (bytes & (1 << 1)) != 0x0000;
    this.bgWindowEnabled = (bytes & (1 << 0)) != 0x0000;
  }
}
