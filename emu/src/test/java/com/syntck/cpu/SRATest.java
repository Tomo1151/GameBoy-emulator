package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SRATest {
  // MARK: SRA (A)
  @Test
  @DisplayName("Test SRA A instruction")
  public void testSRAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x2F); // SRA A
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.a); // 結果は11000010（MSB保持、右シフト）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // キャリーフラグは最下位ビット（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: SRA (B)
  @Test
  @DisplayName("Test SRA B instruction")
  public void testSRAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x28); // SRA B
    cpu.registers.b = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.b); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは最下位ビットの値（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (C)
  @Test
  @DisplayName("Test SRA C instruction")
  public void testSRAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x29); // SRA C
    cpu.registers.c = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.c); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (D)
  @Test
  @DisplayName("Test SRA D instruction")
  public void testSRAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2A); // SRA D
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.d); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (E)
  @Test
  @DisplayName("Test SRA E instruction")
  public void testSRAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2B); // SRA E
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.e); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (H)
  @Test
  @DisplayName("Test SRA H instruction")
  public void testSRAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2C); // SRA H
    cpu.registers.h = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.h); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (L)
  @Test
  @DisplayName("Test SRA L instruction")
  public void testSRAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2D); // SRA L
    cpu.registers.l = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.l); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA ((HL))
  @Test
  @DisplayName("Test SRA (HL) instruction")
  public void testSRAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2E); // SRA (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.step();
    assertEquals(0xC2, cpu.bus.readByte(0xC000)); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (A) MSB=0
  @Test
  @DisplayName("Test SRA A with MSB=0")
  public void testSRAAWithMSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2F); // SRA A
    cpu.registers.a = 0x42; // 01000010
    cpu.step();
    assertEquals(0x21, cpu.registers.a); // 結果は00100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (B) Z
  @Test
  @DisplayName("Test SRA B resulting in zero")
  public void testSRABZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x28); // SRA B
    cpu.registers.b = 0x01; // 00000001
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (C) オールワン
  @Test
  @DisplayName("Test SRA C with all ones")
  public void testSRACAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x29); // SRA C
    cpu.registers.c = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFF, cpu.registers.c); // 結果も11111111（MSB保持）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (D) オールゼロ
  @Test
  @DisplayName("Test SRA D with all zeros")
  public void testSRADAllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2A); // SRA D
    cpu.registers.d = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.d); // 結果も00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRA (E) 連続実行
  @Test
  @DisplayName("Test consecutive SRA E operations")
  public void testSRAEConsecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x2B); // SRA E
    
    // 1回目の実行
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.e); // 結果は11000010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xE1, cpu.registers.e); // 結果は11100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xF0, cpu.registers.e); // 結果は11110000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }
}
