package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class StackOperationTest {

  // PUSH命令のテスト
  @Test
  @DisplayName("Test PUSH BC")
  public void testPushBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    cpu.registers.set_bc(0xABCD);
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0xAB, cpu.bus.readByte(0xFFFC)); // 上位バイト
    assertEquals(0xCD, cpu.bus.readByte(0xFFFD)); // 下位バイト
    assertEquals(0x0001, cpu.pc);
  }

  // POP命令のテスト
  @Test
  @DisplayName("Test POP DE")
  public void testPopDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD1); // POP DE
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x34);
    cpu.bus.writeByte(0xFFFD, 0x12);
    cpu.step();
    assertEquals(0x1234, cpu.registers.get_de());
    assertEquals(0xFFFE, cpu.sp); // SPが2バイト増加
    assertEquals(0x0001, cpu.pc);
  }

  // PUSH AF命令のテスト (フラグレジスタのテスト)
  @Test
  @DisplayName("Test PUSH AF")
  public void testPushAF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF5); // PUSH AF
    cpu.registers.a = 0x5A;
    cpu.registers.f.zero = true;
    cpu.registers.f.carry = true;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.subtract = false;
    // フラグレジスタの値: Z=1, N=0, H=0, C=1 -> 10010000 -> 0x90
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0x5A, cpu.bus.readByte(0xFFFC)); // Aレジスタ
    assertEquals(0x90, cpu.bus.readByte(0xFFFD)); // フラグレジスタ
    assertEquals(0x0001, cpu.pc);
  }

  // POP AF命令のテスト (フラグレジスタのテスト)
  @Test
  @DisplayName("Test POP AF")
  public void testPopAF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF1); // POP AF
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x5A);
    cpu.bus.writeByte(0xFFFD, 0x90); // Z=1, N=0, H=0, C=1
    cpu.step();
    assertEquals(0x5A, cpu.registers.a);
    assertEquals(true, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(true, cpu.registers.f.carry);
    assertEquals(0xFFFE, cpu.sp); // SPが2バイト増加
    assertEquals(0x0001, cpu.pc);
  }
}
