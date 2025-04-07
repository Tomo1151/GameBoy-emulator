package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class ArithmeticTest {

  // ADD命令のテスト
  @Test
  @DisplayName("Test ADD A, B")
  public void testAddAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x80); // ADD A, B
    cpu.registers.a = 0x45;
    cpu.registers.b = 0x38;
    cpu.step();
    assertEquals(0x7D, cpu.registers.a); // 0x45 + 0x38 = 0x7D
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0000, FlagsRegister.convertToInt(cpu.registers.f)); // フラグは設定されない
  }

  @Test
  @DisplayName("Test ADD A, B with carry")
  public void testAddAB_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x80); // ADD A, B
    cpu.registers.a = 0xE0;
    cpu.registers.b = 0x28;
    cpu.step();
    assertEquals(0x08, cpu.registers.a); // 0xE0 + 0x28 = 0x108 (0x08 with carry)
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0010, FlagsRegister.convertToInt(cpu.registers.f)); // キャリーフラグがセット
  }

  @Test
  @DisplayName("Test ADD A, B with half carry")
  public void testAddAB_HalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x80); // ADD A, B
    cpu.registers.a = 0x0F;
    cpu.registers.b = 0x01;
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // 0x0F + 0x01 = 0x10
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0020, FlagsRegister.convertToInt(cpu.registers.f)); // ハーフキャリーフラグがセット
  }

  @Test
  @DisplayName("Test ADD A, B with zero result")
  public void testAddAB_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x80); // ADD A, B
    cpu.registers.a = 0x80;
    cpu.registers.b = 0x80;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0x80 + 0x80 = 0x100 (0x00 with carry)
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0090, FlagsRegister.convertToInt(cpu.registers.f)); // ゼロとキャリーフラグがセット
  }

  // SUB命令のテスト
  @Test
  @DisplayName("Test SUB A, C")
  public void testSubAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x91); // SUB A, C
    cpu.registers.a = 0x3E;
    cpu.registers.c = 0x3E;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0x3E - 0x3E = 0x00
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x00C0, FlagsRegister.convertToInt(cpu.registers.f)); // ゼロとサブトラクトフラグがセット
  }

  @Test
  @DisplayName("Test SUB A, C with carry")
  public void testSubAC_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x91); // SUB A, C
    cpu.registers.a = 0x3E;
    cpu.registers.c = 0x40;
    cpu.step();
    assertEquals(0xFE, cpu.registers.a); // 0x3E - 0x40 = -2 (0xFE)
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0050, FlagsRegister.convertToInt(cpu.registers.f)); // サブトラクト、キャリー、ハーフキャリーフラグがセット
  }

  // AND命令のテスト
  @Test
  @DisplayName("Test AND A, D")
  public void testAndAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA2); // AND A, D
    cpu.registers.a = 0xF0;
    cpu.registers.d = 0x0F;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0xF0 & 0x0F = 0x00
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x00A0, FlagsRegister.convertToInt(cpu.registers.f)); // ゼロとハーフキャリーフラグがセット
  }

  // OR命令のテスト
  @Test
  @DisplayName("Test OR A, E")
  public void testOrAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB3); // OR A, E
    cpu.registers.a = 0xF0;
    cpu.registers.e = 0x0F;
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 0xF0 | 0x0F = 0xFF
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0000, FlagsRegister.convertToInt(cpu.registers.f)); // フラグは設定されない
  }

  // XOR命令のテスト
  @Test
  @DisplayName("Test XOR A, H")
  public void testXorAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAC); // XOR A, H
    cpu.registers.a = 0xFF;
    cpu.registers.h = 0x0F;
    cpu.step();
    assertEquals(0xF0, cpu.registers.a); // 0xFF ^ 0x0F = 0xF0
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0000, FlagsRegister.convertToInt(cpu.registers.f)); // フラグは設定されない
  }

  // CP命令のテスト
  @Test
  @DisplayName("Test CP A, L")
  public void testCpAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBD); // CP A, L
    cpu.registers.a = 0x40;
    cpu.registers.l = 0x40;
    cpu.step();
    assertEquals(0x40, cpu.registers.a); // 値は変わらない
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x00C0, FlagsRegister.convertToInt(cpu.registers.f)); // ゼロとサブトラクトフラグがセット
  }

  // INC命令のテスト
  @Test
  @DisplayName("Test INC A")
  public void testIncA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3C); // INC A
    cpu.registers.a = 0x0F;
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // 0x0F + 1 = 0x10
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0020, FlagsRegister.convertToInt(cpu.registers.f)); // ハーフキャリーフラグがセット
  }

  // DEC命令のテスト
  @Test
  @DisplayName("Test DEC B")
  public void testDecB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x05); // DEC B
    cpu.registers.b = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 0x01 - 1 = 0x00
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x00C0, FlagsRegister.convertToInt(cpu.registers.f)); // ゼロとサブトラクトフラグがセット
  }
}
