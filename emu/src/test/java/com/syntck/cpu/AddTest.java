package com.syntck.cpu;

import static com.syntck.Functions.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


public class ADDTest {
  // MARK: ADD (A, A)
  @Test
  @DisplayName("Test ADD A, A instruction")
  public void testAddAA() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x87); // Changed instruction to ADD A, A
    cpu.registers.a = 0x02;
    cpu.step();
    assertEquals(0x04, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, B)
  @Test
  @DisplayName("Test ADD A, B instruction")
  public void testAddAB() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x80); // Changed instruction to ADD A, B
    cpu.registers.a = 0x02;
    cpu.registers.b = 0x03;
    cpu.step();
    assertEquals(0x05, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, C)
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
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, D)
  @Test
  @DisplayName("Test ADD A, D instruction")
  public void testAddAD() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x82); // ADD A, D // Updated instruction comment
    cpu.registers.a = 0x02;
    cpu.registers.d = 0x03;
    cpu.step();
    assertEquals(0x05, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, E)
  @Test
  @DisplayName("Test ADD A, E instruction")
  public void testAddAE() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x83); // ADD A, E // Updated instruction comment
    cpu.registers.a = 0x05;
    cpu.registers.e = 0x03;
    cpu.step();
    assertEquals(0x08, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, H)
  @Test
  @DisplayName("Test ADD A, H instruction")
  public void testAddAH() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x84); // Changed instruction to ADD A, H
    cpu.registers.a = 0x02;
    cpu.registers.h = 0x03;
    cpu.step();
    assertEquals(0x05, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, L)
  @Test
  @DisplayName("Test ADD A, L instruction")
  public void testAddAL() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x85);
    cpu.registers.a = 0x02;
    cpu.registers.l = 0x03;
    cpu.step();
    assertEquals(0x05, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, d8)
  @Test
  @DisplayName("Test ADD A, d8 instruction")
  public void testAddAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0xC6); // Changed instruction to ADD A, d8
    cpu.bus.writeByte(0x0001, 0x08); // d8 = 8
    cpu.registers.a = 0x02;
    cpu.step();
    assertEquals(0x0A, cpu.registers.a);
    assertEquals(0x0002, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD (A, HL)
  @Test
  @DisplayName("Test ADD A, HL instruction")
  public void testAddAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0x86); // Changed instruction to ADD A, HL
    cpu.registers.a = 0x02;
    cpu.registers.set_hl(0x1040); // Set HL to 0x1040
    cpu.bus.writeByte(0x1040, 0x06); // Write 0x06 to memory at address 0x1040
    cpu.step();
    assertEquals(0x08, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD with various conditions
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
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, true, false));
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
    assertEquals(0x10, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, true) );

    assertEquals(0xFFFF, cpu.sp);
  }

  @Test
  @DisplayName("Test ADD A, C with carry & half carry")
  public void testAddAC_CarryAndHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0xFF;
    cpu.registers.c = 0x02;
    cpu.step();
    cpu.registers.f.dump();
    assertEquals(0x01, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    cpu.registers.f.dump();

    assertTrue(
        compareFlagsRegister(
          cpu.registers.f,
          false,
          false,
          true,
          true
        )
    );
    assertEquals(0xFFFF, cpu.sp);
  }

  @Test
  @DisplayName("Test ADD A, C with zero result")
  public void testAddAC_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0x00;
    cpu.registers.c = 0x00;
    cpu.step();
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(
      compareFlagsRegister(
        cpu.registers.f,
        true,
        false,
        false,
        false
      )
  );
    assertEquals(0xFFFF, cpu.sp);
  }

  @Test
  @DisplayName("Test ADD A, C with carry and zero result")
  public void testAddAC_CarryAndZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x81); // ADD A, C
    cpu.registers.a = 0xFF;
    cpu.registers.c = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
    assertTrue(
      compareFlagsRegister(
        cpu.registers.f,
        true,
        false,
        true,
        true
      )
    );
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD_HL (HL, BC)
  @Test
  @DisplayName("Test ADD HL, BC instruction")
  public void testAddHL_BC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x09);
    cpu.registers.set_hl(0x1234);
    cpu.registers.set_bc(0x2002); // Set BC to 0x2002
    cpu.step();
    assertEquals(0x3236, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD_HL (HL, DE)
  @Test
  @DisplayName("Test ADD HL, DE instruction")
  public void testAddHL_DE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x19);
    cpu.registers.set_hl(0x1234);
    cpu.registers.set_de(0x2002);
    cpu.step();
    assertEquals(0x3236, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD_HL (HL, HL)
  @Test
  @DisplayName("Test ADD HL, HL instruction")
  public void testAddHL_HL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x29);
    cpu.registers.set_hl(0x1234);
    cpu.step();
    assertEquals(0x2468, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0xFFFF, cpu.sp);
  }

  // MARK: ADD_HL (HL, SP)
  @Test
  @DisplayName("Test ADD HL, SP instruction")
  public void testAddHL_SP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x39);
    cpu.registers.set_hl(0x1234);
    cpu.sp = 0x2002;
    cpu.step();
    assertEquals(0x3236, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x2002, cpu.sp);
  }

  // MARK: ADD_HL with various conditions
  // ADD_HL (HL, BC) with carry
  @Test
  @DisplayName("Test ADD HL, BC instruction with carry")
  public void testAddHL_BC_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x09);
    cpu.registers.set_hl(0xF000);
    cpu.registers.set_bc(0x1000); // Set BC to 0x1000
    cpu.step();
    assertEquals(0x0000, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0xFFFF, cpu.sp);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, true));
  }

  // ADD_HL (HL, BC) with half carry
  @Test
  @DisplayName("Test ADD HL, BC instruction with half carry")
  public void testAddHL_BC_HalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x09);
    cpu.registers.set_hl(0x0F00);
    cpu.registers.set_bc(0x0100); // Set BC to 0x0100
    cpu.step();
    assertEquals(0x1000, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc);
    assertEquals(0xFFFF, cpu.sp);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, true, false));
  }

  // MARK: ADD_SP (SP, r8)
  @Test
  @DisplayName("Test ADD SP, r8 instruction")
  public void testAddSP_r8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE8); // ADD SP, r8
    cpu.bus.writeByte(0x0001, 0x04); // r8 = 4
    cpu.sp = 0x1003;
    cpu.step();
    assertEquals(0x1007, cpu.sp);
    assertEquals(0x0002, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false));
  }


  // MARK: ADD_SP with various conditions
  // ADD_SP (SP, r8) with carry
  @Test
  @DisplayName("Test ADD SP, r8 instruction with minus r8")
  public void testAddSP_r8_Minus() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE8); // ADD SP, r8
    cpu.bus.writeByte(0x0001, -1); // r8 = -1 (0xFF)
    cpu.sp = 0x0003;
    cpu.step();
    assertEquals(0x0002, cpu.sp);
    assertEquals(0x0002, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, true, true));
  }

  // ADD_SP (SP, r8) with carry
  @Test
  @DisplayName("Test ADD SP, r8 instruction with carry")
  public void testAddSP_r8_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE8);
    cpu.bus.writeByte(0x0001, 0x11); // r8
    cpu.sp = 0x00F0;
    cpu.step();
    assertEquals(0x0101, cpu.sp);
    assertEquals(0x0002, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, true));
  }

  // ADD_SP (SP, r8) with half carry
  @Test
  @DisplayName("Test ADD SP, r8 instruction with half carry")
  public void testAddSP_r8_HalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE8);
    cpu.bus.writeByte(0x0001, 0x01); // r8
    cpu.sp = 0x000F;
    cpu.step();
    assertEquals(0x0010, cpu.sp);
    assertEquals(0x0002, cpu.pc);
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, true, false));
  }
}
