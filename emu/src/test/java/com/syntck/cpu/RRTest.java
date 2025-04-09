package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RRTest {
  // MARK: RR (A) C0
  @Test
  @DisplayName("Test RR A without initial carry")
  public void testRRA_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x1F); // RR A
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false; // キャリーフラグはリセット
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010（右シフトしてキャリーが0）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: RR (A) C1
  @Test
  @DisplayName("Test RR A with initial carry")
  public void testRRA_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1F); // RR A
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    assertEquals(0xC2, cpu.registers.a); // 結果は11000010（右シフトしてキャリーが1）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RR (B) C0
  @Test
  @DisplayName("Test RR B without initial carry")
  public void testRRB_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x18); // RR B
    cpu.registers.b = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x42, cpu.registers.b); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (C) C1
  @Test
  @DisplayName("Test RR C with initial carry")
  public void testRRC_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x19); // RR C
    cpu.registers.c = 0x84; // 10000100
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0xC2, cpu.registers.c); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（0）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (D) C0
  @Test
  @DisplayName("Test RR D without initial carry")
  public void testRRD_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1A); // RR D
    cpu.registers.d = 0x01; // 00000001
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.d); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (E) C1
  @Test
  @DisplayName("Test RR E with initial carry")
  public void testRRE_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1B); // RR E
    cpu.registers.e = 0x00; // 00000000
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x80, cpu.registers.e); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（0）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (H) C0
  @Test
  @DisplayName("Test RR H without initial carry")
  public void testRRH_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1C); // RR H
    cpu.registers.h = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x42, cpu.registers.h); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (L) C1
  @Test
  @DisplayName("Test RR L with initial carry")
  public void testRRL_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1D); // RR L
    cpu.registers.l = 0x85; // 10000101
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0xC2, cpu.registers.l); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR ((HL)) C0
  @Test
  @DisplayName("Test RR (HL) without initial carry")
  public void testRR_HL_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1E); // RR (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x42, cpu.bus.readByte(0xC000)); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR ((HL)) C1
  @Test
  @DisplayName("Test RR (HL) with initial carry")
  public void testRR_HL_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1E); // RR (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0xC2, cpu.bus.readByte(0xC000)); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (A) Z
  @Test
  @DisplayName("Test RR A resulting in zero")
  public void testRRA_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1F); // RR A
    cpu.registers.a = 0x01; // 00000001
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロなのでフラグがセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (B) キャリー→キャリー
  @Test
  @DisplayName("Test RR B carry to carry")
  public void testRRB_CarryToCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x18); // RR B
    cpu.registers.b = 0x81; // 10000001
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0xC0, cpu.registers.b); // 結果は11000000（MSBにキャリーが入る）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは最下位ビット（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (C) オールワン
  @Test
  @DisplayName("Test RR C with all ones")
  public void testRRC_AllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x19); // RR C
    cpu.registers.c = 0xFF; // 11111111
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x7F, cpu.registers.c); // 結果は01111111
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットはキャリーに（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (D) オールゼロ
  @Test
  @DisplayName("Test RR D with all zeros")
  public void testRRD_AllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1A); // RR D
    cpu.registers.d = 0x00; // 00000000
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.d); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR (A) 連続実行
  @Test
  @DisplayName("Test consecutive RR A operations")
  public void testRRA_Consecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1F); // RR A
    
    // 1回目の実行
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xA1, cpu.registers.a); // 結果は10100001（前回のキャリーが最上位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x50, cpu.registers.a); // 結果は01010000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }

  // MARK: RR (E) オールゼロ＋キャリーあり
  @Test
  @DisplayName("Test RR E with all zeros and carry")
  public void testRRE_AllZerosWithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1B); // RR E
    cpu.registers.e = 0x00; // 00000000
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x80, cpu.registers.e); // 結果は10000000（キャリーがMSBに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RR 全レジスタチェック
  @Test
  @DisplayName("Test RR instruction on all registers")
  public void testRR_AllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x85; // 10000101
    final int expectedResultNoCarry = 0x42; // 01000010（キャリーなしの場合）
    final int expectedResultWithCarry = 0xC2; // 11000010（キャリーありの場合）
    
    // RR A（キャリーなし）
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1F); // RR A
    cpu.registers.a = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResultNoCarry, cpu.registers.a);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // RR B（キャリーあり）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x18); // RR B
    cpu.registers.b = testValue;
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(expectedResultWithCarry, cpu.registers.b);
    assertTrue(cpu.registers.f.carry);
    
    // RR C（キャリーなし）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x19); // RR C
    cpu.registers.c = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResultNoCarry, cpu.registers.c);
    assertTrue(cpu.registers.f.carry);
    
    // RR D（キャリーあり）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1A); // RR D
    cpu.registers.d = testValue;
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(expectedResultWithCarry, cpu.registers.d);
    assertTrue(cpu.registers.f.carry);
    
    // RR E（キャリーなし）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1B); // RR E
    cpu.registers.e = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResultNoCarry, cpu.registers.e);
    assertTrue(cpu.registers.f.carry);
    
    // RR H（キャリーあり）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1C); // RR H
    cpu.registers.h = testValue;
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(expectedResultWithCarry, cpu.registers.h);
    assertTrue(cpu.registers.f.carry);
    
    // RR L（キャリーなし）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1D); // RR L
    cpu.registers.l = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResultNoCarry, cpu.registers.l);
    assertTrue(cpu.registers.f.carry);
    
    // RR (HL)（キャリーあり）
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x1E); // RR (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(expectedResultWithCarry, cpu.bus.readByte(0xC000));
    assertTrue(cpu.registers.f.carry);
  }
}
