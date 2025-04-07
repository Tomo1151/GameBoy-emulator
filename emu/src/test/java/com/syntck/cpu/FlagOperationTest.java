package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class FlagOperationTest {

  // CCF命令のテスト
  @Test
  @DisplayName("Test CCF")
  public void testCCF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(false, cpu.registers.f.carry); // キャリーフラグが反転
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // SCF命令のテスト
  @Test
  @DisplayName("Test SCF")
  public void testSCF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(true, cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // CPL命令のテスト
  @Test
  @DisplayName("Test CPL")
  public void testCPL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL
    cpu.registers.a = 0xA5;
    cpu.step();
    assertEquals(0x5A, cpu.registers.a); // 0xA5 の補数は 0x5A
    assertEquals(true, cpu.registers.f.subtract);
    assertEquals(true, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // RRA命令のテスト
  @Test
  @DisplayName("Test RRA")
  public void testRRA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA
    cpu.registers.a = 0x81;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x40, cpu.registers.a); // 0x81 を右にローテートして、キャリーフラグ経由で回転
    assertEquals(true, cpu.registers.f.carry); // LSBがキャリーにセット
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // RLA命令のテスト
  @Test
  @DisplayName("Test RLA")
  public void testRLA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA
    cpu.registers.a = 0x80;
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 0x80 を左にローテートして、キャリーフラグ経由で回転
    assertEquals(true, cpu.registers.f.carry); // MSBがキャリーにセット
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // RRCA命令のテスト
  @Test
  @DisplayName("Test RRCA")
  public void testRRCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA
    cpu.registers.a = 0x01;
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 0x01 を右に循環ローテート
    assertEquals(true, cpu.registers.f.carry); // LSBがキャリーにセット
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }

  // RLCA命令のテスト
  @Test
  @DisplayName("Test RLCA")
  public void testRLCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA
    cpu.registers.a = 0x80;
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 0x80 を左に循環ローテート
    assertEquals(true, cpu.registers.f.carry); // MSBがキャリーにセット
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(0x0001, cpu.pc);
  }
}
