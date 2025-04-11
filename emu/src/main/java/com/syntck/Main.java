package com.syntck;

import com.syntck.cartridge.Cartridge;

import java.io.*;

public class Main {
  public static void main(String[] args) {
    // Cartridge cartridge = new Cartridge("emu/src/main/java/com/syntck/rom/test/cpu_instrs.gb");
    Cartridge cartridge = new Cartridge("emu/src/main/java/com/syntck/rom/test/individual/01-special.gb");

    try {
      FileOutputStream fos = new FileOutputStream("logs/Log_0.txt");
      PrintStream ps = new PrintStream(fos);
      System.setOut(ps); // 標準出力をファイルにリダイレクト
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    new GameBoy(cartridge);
  }
}

