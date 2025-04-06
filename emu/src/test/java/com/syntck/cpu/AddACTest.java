package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


public class AddACTest {
  @Test
  @DisplayName("Test ADD A, C instruction")
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

  @Test
  @DisplayName("Test ADD A, C with half carry")
  public void testAddAC_HalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0x0F;
    cpu.registers.c = 0x01;
    cpu.step();
    assertEquals(0x10, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    // Expect the half-carry flag to be set (assuming half-carry flag equals 0x0020)
    assertEquals(0x0020, FlagsRegister.convertToInt(cpu.registers.f));
    assertEquals(0xFFFF, cpu.sp);
  }
  
  @Test
  @DisplayName("Test ADD A, C with carry and zero result")
  public void testAddAC_CarryAndZero() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0xFF;
    cpu.registers.c = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    // Expect the zero, half-carry, and carry flags to be set 
    // (assuming Z=0x80, H=0x20, C=0x10, total value 0xB0)
    assertEquals(0x00B0, FlagsRegister.convertToInt(cpu.registers.f));
    assertEquals(0xFFFF, cpu.sp);
  }
}
