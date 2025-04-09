package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SLATest {
  // MARK: SLA (A)
  @Test
  @DisplayName("Test SLA A instruction")
  public void testSLAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x27); // SLA A
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.a); // 結果は00001010（左シフトして最下位ビットは0）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: SLA (B)
  @Test
  @DisplayName("Test SLA B instruction")
  public void testSLAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x20); // SLA B
    cpu.registers.b = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.b); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは最上位ビットの値（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (C)
  @Test
  @DisplayName("Test SLA C instruction")
  public void testSLAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x21); // SLA C
    cpu.registers.c = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.c); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (D)
  @Test
  @DisplayName("Test SLA D instruction")
  public void testSLAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x22); // SLA D
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.d); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (E)
  @Test
  @DisplayName("Test SLA E instruction")
  public void testSLAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x23); // SLA E
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.e); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (H)
  @Test
  @DisplayName("Test SLA H instruction")
  public void testSLAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x24); // SLA H
    cpu.registers.h = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.h); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (L)
  @Test
  @DisplayName("Test SLA L instruction")
  public void testSLAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x25); // SLA L
    cpu.registers.l = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.l); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA ((HL))
  @Test
  @DisplayName("Test SLA (HL) instruction")
  public void testSLAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x26); // SLA (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.step();
    assertEquals(0x0A, cpu.bus.readByte(0xC000)); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (A) MSB=0
  @Test
  @DisplayName("Test SLA A with MSB=0")
  public void testSLAAWithMSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x27); // SLA A
    cpu.registers.a = 0x42; // 01000010
    cpu.step();
    assertEquals(0x84, cpu.registers.a); // 結果は10000100
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (B) Z
  @Test
  @DisplayName("Test SLA B resulting in zero")
  public void testSLABZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x20); // SLA B
    cpu.registers.b = 0x80; // 10000000
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (C) オールワン
  @Test
  @DisplayName("Test SLA C with all ones")
  public void testSLACAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x21); // SLA C
    cpu.registers.c = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFE, cpu.registers.c); // 結果は11111110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (D) オールゼロ
  @Test
  @DisplayName("Test SLA D with all zeros")
  public void testSLADAllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x22); // SLA D
    cpu.registers.d = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.d); // 結果も00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA (E) 連続実行
  @Test
  @DisplayName("Test consecutive SLA E operations")
  public void testSLAEConsecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x23); // SLA E
    
    // 1回目の実行
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0A, cpu.registers.e); // 結果は00001010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x14, cpu.registers.e); // 結果は00010100
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x28, cpu.registers.e); // 結果は00101000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: SLA (L) ビット7のみ
  @Test
  @DisplayName("Test SLA L with only bit 7 set")
  public void testSLALOnlyBit7Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x25); // SLA L
    cpu.registers.l = 0x80; // 10000000
    cpu.step();
    assertEquals(0x00, cpu.registers.l); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1だったのでキャリーは1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA ((HL)) フラグ変化
  @Test
  @DisplayName("Test SLA (HL) flag changes")
  public void testSLAHLFlagChanges() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x26); // SLA (HL)
    cpu.registers.set_hl(0xC000);
    
    // 最上位ビットが1の場合
    cpu.bus.writeByte(0xC000, 0x80); // 10000000
    cpu.step();
    assertEquals(0x00, cpu.bus.readByte(0xC000)); // 結果は00000000
    assertTrue(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 最上位ビットが0の場合
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0xC000, 0x40); // 01000000
    cpu.step();
    assertEquals(0x80, cpu.bus.readByte(0xC000)); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: SLA (H) 8ビット範囲外
  @Test
  @DisplayName("Test SLA H with 8-bit overflow")
  public void testSLAHOverflow() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x24); // SLA H
    cpu.registers.h = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFE, cpu.registers.h); // 結果は11111110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1だったのでキャリーは1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SLA 全レジスタチェック
  @Test
  @DisplayName("Test SLA instruction on all registers")
  public void testSLAAllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x81; // 10000001
    final int expectedResult = 0x02; // 00000010
    
    // SLA A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x27); // SLA A
    cpu.registers.a = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.a);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1だったのでキャリーは1
    
    // SLA B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x20); // SLA B
    cpu.registers.b = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.b);
    assertTrue(cpu.registers.f.carry);
    
    // SLA C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x21); // SLA C
    cpu.registers.c = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.c);
    assertTrue(cpu.registers.f.carry);
    
    // SLA D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x22); // SLA D
    cpu.registers.d = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.d);
    assertTrue(cpu.registers.f.carry);
    
    // SLA E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x23); // SLA E
    cpu.registers.e = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.e);
    assertTrue(cpu.registers.f.carry);
    
    // SLA H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x24); // SLA H
    cpu.registers.h = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.h);
    assertTrue(cpu.registers.f.carry);
    
    // SLA L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x25); // SLA L
    cpu.registers.l = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.l);
    assertTrue(cpu.registers.f.carry);
    
    // SLA (HL)
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x26); // SLA (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.step();
    assertEquals(expectedResult, cpu.bus.readByte(0xC000));
    assertTrue(cpu.registers.f.carry);
  }
}
