package emu;

import emu.cpu.CPU;

public class Main {
  public static void main(String[] args) {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
  }
}
