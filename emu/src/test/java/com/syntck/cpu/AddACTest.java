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
  @DisplayName("Test ADD A, C with carry")
  public void testAddAC_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0xF0;
    cpu.registers.c = 0x20;
    cpu.step();
    cpu.registers.f.dump();
    assertEquals(0x10, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    cpu.registers.f.dump();

    // Expect the zero, half-carry and carry flags to be set (Z=false, N=false, H=false, C=false)
    assertEquals(FlagsRegister.convertToInt(new FlagsRegister(false, false, false, true)), FlagsRegister.convertToInt(cpu.registers.f));
    assertEquals(0xFFFF, cpu.sp);
  }
  
  @Test
  @DisplayName("Test ADD A, C with zero result")
  public void testAddAC_CarryAndZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0x00;
    cpu.registers.c = 0x00;
    cpu.step();
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x80, FlagsRegister.convertToInt(cpu.registers.f));
    assertEquals(0xFFFF, cpu.sp);
  }
}
