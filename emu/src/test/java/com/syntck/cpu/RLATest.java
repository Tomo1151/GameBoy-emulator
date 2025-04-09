package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RLATest {
  // MARK: RLA C0
  @Test
  @DisplayName("Test RLA without initial carry")
  public void testRLA_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令 (非CBプレフィクス)
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false; // キャリーフラグはリセット
    cpu.step();
    
    assertEquals(0x0A, cpu.registers.a); // 結果は00001010（左シフトしてキャリーが0）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは変わらない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0001, cpu.pc); // PCが1バイト増加
  }

  // MARK: RLA C1
  @Test
  @DisplayName("Test RLA with initial carry")
  public void testRLA_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    
    assertEquals(0x0B, cpu.registers.a); // 結果は00001011（左シフトしてキャリーが1）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットがキャリーに移動（1）
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA MSB=0
  @Test
  @DisplayName("Test RLA with MSB=0")
  public void testRLA_MSBZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x7F; // 01111111 (最上位ビットが0)
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0xFE, cpu.registers.a); // 結果は11111110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA MSB=1
  @Test
  @DisplayName("Test RLA with MSB=1")
  public void testRLA_MSBOne() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0xC3; // 11000011 (最上位ビットが1)
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x86, cpu.registers.a); // 結果は10000110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA Z-変更なし
  @Test
  @DisplayName("Test RLA preserves zero flag")
  public void testRLA_PreservesZeroFlag() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x01; // 00000001
    cpu.registers.f.zero = true; // ゼロフラグを事前にセット
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x02, cpu.registers.a); // 結果は00000010
    assertFalse(cpu.registers.f.zero); // RLAではゼロフラグは固定で0
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA フラグリセット
  @Test
  @DisplayName("Test RLA resets flags")
  public void testRLA_ResetsFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x00; // 00000000
    
    // 事前にフラグをセット
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 結果も00000000
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグも0のまま
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA 連続実行
  @Test
  @DisplayName("Test consecutive RLA operations")
  public void testRLA_Consecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    
    // 1回目の実行
    cpu.registers.a = 0x85; // 10000101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x0A, cpu.registers.a); // 結果は00001010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x15, cpu.registers.a); // 結果は00010101（前回のキャリーが最下位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x2A, cpu.registers.a); // 結果は00101010
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: RLA オールゼロ
  @Test
  @DisplayName("Test RLA with all zeros")
  public void testRLA_AllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 結果も00000000
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA オールワン
  @Test
  @DisplayName("Test RLA with all ones")
  public void testRLA_AllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0xFF; // 11111111
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0xFE, cpu.registers.a); // 結果は11111110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA C→A
  @Test
  @DisplayName("Test RLA carry to A")
  public void testRLA_CarryToA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    
    assertEquals(0x01, cpu.registers.a); // 結果は00000001（キャリーが最下位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLA A→C→A
  @Test
  @DisplayName("Test RLA A to carry to A")
  public void testRLA_AToCaryToA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    
    // 1回目の実行：最上位ビットがキャリーに
    cpu.registers.a = 0x80; // 10000000
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行：キャリーが最下位ビットに
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 結果は00000001
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: RLA バイトテスト
  @Test
  @DisplayName("Test RLA is a single byte instruction")
  public void testRLA_ByteSize() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x17); // RLA命令
    cpu.bus.writeByte(0x0001, 0x00); // NOP命令 (次の命令)
    cpu.registers.a = 0x55; // 01010101
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    assertEquals(0xAB, cpu.registers.a); // 結果は10101011
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0001, cpu.pc); // PCは1バイト進む
    
    cpu.step(); // 次のNOP命令を実行
    
    assertEquals(0x0002, cpu.pc); // さらに1バイト進む
  }
}