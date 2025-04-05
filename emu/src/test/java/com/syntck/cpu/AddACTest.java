package com.syntck.cpu;

import static org.junit.Assert.*;
import org.junit.Test;

public class AddACTest {
  @Test
  public void testAddAC() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0x02;
    cpu.registers.c = 0x03;
    cpu.step();
    assertEquals(0x05, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0000, FlagsRegister.convertToInt(cpu.registers.f));
    assertEquals(0xFFFF, cpu.sp);
  }
}
