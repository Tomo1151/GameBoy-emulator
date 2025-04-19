package com.syntck;

import com.syntck.cartridge.Cartridge;

import java.io.*;

public class Main {
  public static void main(String[] args) {
    String ROM_PATH = "emu/src/main/java/com/syntck/rom/";
    Cartridge[] tests = {
      new Cartridge(ROM_PATH + "test/cpu_instrs.gb"),
      new Cartridge(ROM_PATH + "test/individual/01-special.gb"),
      new Cartridge(ROM_PATH + "test/individual/02-interrupts.gb"),
      new Cartridge(ROM_PATH + "test/individual/03-op sp,hl.gb"),
      new Cartridge(ROM_PATH + "test/individual/04-op r,imm.gb"),
      new Cartridge(ROM_PATH + "test/individual/05-op rp.gb"),
      new Cartridge(ROM_PATH + "test/individual/06-ld r,r.gb"),
      new Cartridge(ROM_PATH + "test/individual/07-jr,jp,call,ret,rst.gb"),
      new Cartridge(ROM_PATH + "test/individual/08-misc instrs.gb"),
      new Cartridge(ROM_PATH + "test/individual/09-op r,r.gb"),
      new Cartridge(ROM_PATH + "test/individual/10-bit ops.gb"),
      new Cartridge(ROM_PATH + "test/individual/11-op a,(hl).gb"),
      new Cartridge(ROM_PATH + "test/dmg-acid2.gb"),
      new Cartridge(ROM_PATH + "test/bgbtest.gb"),
      new Cartridge(ROM_PATH + "tobu.gb"),
    };

    Cartridge cartridge = tests[12];

    // try {
    //   FileOutputStream fos = new FileOutputStream("logs/Log_0.txt");
    //   PrintStream ps = new PrintStream(fos);
    //   System.setOut(ps); // 標準出力をファイルにリダイレクト
    // } catch (FileNotFoundException e) {
    //   e.printStackTrace();
    // }

    new GameBoy(cartridge);
  }
}

