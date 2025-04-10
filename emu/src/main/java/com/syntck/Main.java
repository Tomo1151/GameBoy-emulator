package com.syntck;

import com.syntck.cartridge.Cartridge;

public class Main {
  public static void main(String[] args) {
    Cartridge cartridge = new Cartridge("emu/src/main/java/com/syntck/rom/test/cpu_instrs.gb");
    GameBoy gb = new GameBoy(cartridge);
  }
}

