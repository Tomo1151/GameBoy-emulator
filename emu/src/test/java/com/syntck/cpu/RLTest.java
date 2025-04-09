package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RLTest {
  // MARK: RL (A) C0
  @Test
  @DisplayName("Test RL A without initial carry")
  public void testRLA_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false; // キャリーフラグはリセット
    cpu.step();
    assertEquals(0x0A, cpu.registers.a); // 結果は00001010（左シフトしてキャリーが0）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: RL (A) C1
  @Test
  @DisplayName("Test RL A with initial carry")
  public void testRLA_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    assertEquals(0x0B, cpu.registers.a); // 結果は00001011（左シフトしてキャリーが1）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (B) C0
  @Test
  @DisplayName("Test RL B without initial carry")
  public void testRLB_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x10); // RL B
    cpu.registers.b = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.registers.b); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (C) C1
  @Test
  @DisplayName("Test RL C with initial carry")
  public void testRLC_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x11); // RL C
    cpu.registers.c = 0x85; // 10000101
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x0B, cpu.registers.c); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (D) C0
  @Test
  @DisplayName("Test RL D without initial carry")
  public void testRLD_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x12); // RL D
    cpu.registers.d = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.registers.d); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (E) C1
  @Test
  @DisplayName("Test RL E with initial carry")
  public void testRLE_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x13); // RL E
    cpu.registers.e = 0x85; // 10000101
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x0B, cpu.registers.e); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (H) C0
  @Test
  @DisplayName("Test RL H without initial carry")
  public void testRLH_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x14); // RL H
    cpu.registers.h = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.registers.h); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (L) C1
  @Test
  @DisplayName("Test RL L with initial carry")
  public void testRLL_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x15); // RL L
    cpu.registers.l = 0x85; // 10000101
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x0B, cpu.registers.l); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL ((HL)) C0
  @Test
  @DisplayName("Test RL (HL) without initial carry")
  public void testRL_HL_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x16); // RL (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85 (10000101)を書き込む
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.bus.readByte(0xC000)); // 結果は00001010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL ((HL)) C1
  @Test
  @DisplayName("Test RL (HL) with initial carry")
  public void testRL_HL_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x16); // RL (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85 (10000101)を書き込む
    cpu.registers.f.carry = true;
    cpu.step();
    assertEquals(0x0B, cpu.bus.readByte(0xC000)); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (A) Z
  @Test
  @DisplayName("Test RL A resulting in zero")
  public void testRLA_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0x80; // 10000000 (シフト後に0になる可能性がある)
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (B) C→C
  @Test
  @DisplayName("Test RL B carry to carry")
  public void testRLB_CarryToCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x10); // RL B
    cpu.registers.b = 0x80; // 10000000
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 結果は00000000
    assertTrue(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1になる
    assertEquals(0x0002, cpu.pc);
    
    // 2回目の実行で前回のキャリーが新しい最下位ビットになる
    cpu.pc = 0x0000; // PCをリセット
    cpu.registers.b = 0x00; // 00000000
    cpu.step();
    assertEquals(0x01, cpu.registers.b); // 結果は00000001 (前回のキャリーが最下位ビットに)
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (C) C→0
  @Test
  @DisplayName("Test RL C carry and result flags")
  public void testRLC_CarryAndFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x11); // RL C
    cpu.registers.c = 0x01; // 00000001
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x02, cpu.registers.c); // 結果は00000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (A) 連続
  @Test
  @DisplayName("Test consecutive RL A operations")
  public void testRLA_Consecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    
    // 1回目の実行
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.registers.a); // 結果は00001010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x15, cpu.registers.a); // 結果は00010101 (前回のキャリーが最下位ビットに)
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x2A, cpu.registers.a); // 結果は00101010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
  }

  // MARK: RL (D) 全レジスタチェック
  @Test
  @DisplayName("Test RL instruction on all registers")
  public void testRL_AllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x85; // 10000101
    final int expectedResult = 0x0A; // 00001010 (キャリーがないとき)
    
    // RL A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.a);
    assertTrue(cpu.registers.f.carry);
    
    // RL B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x10); // RL B
    cpu.registers.b = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.b);
    
    // RL C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x11); // RL C
    cpu.registers.c = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.c);
    
    // RL D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x12); // RL D
    cpu.registers.d = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.d);
    
    // RL E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x13); // RL E
    cpu.registers.e = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.e);
    
    // RL H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x14); // RL H
    cpu.registers.h = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.h);
    
    // RL L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x15); // RL L
    cpu.registers.l = testValue;
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.l);
    
    // RL (HL)
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x16); // RL (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(expectedResult, cpu.bus.readByte(0xC000));
  }

  // MARK: RL (A) オールワン
  @Test
  @DisplayName("Test RL A with all ones")
  public void testRLA_AllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0xFF; // 11111111
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0xFE, cpu.registers.a); // 結果は11111110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (A) オールゼロ
  @Test
  @DisplayName("Test RL A with all zeros")
  public void testRLA_AllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RL (A) オールゼロ C1
  @Test
  @DisplayName("Test RL A with all zeros and carry")
  public void testRLA_AllZerosWithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x17); // RL A
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 結果は00000001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0002, cpu.pc);
  }
}