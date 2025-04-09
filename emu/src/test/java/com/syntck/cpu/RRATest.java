package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RRATest {
  // MARK: RRA C0
  @Test
  @DisplayName("Test RRA without initial carry")
  public void testRRA_NoCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令 (非CBプレフィクス)
    cpu.registers.a = 0xA5; // 10100101
    cpu.registers.f.carry = false; // キャリーフラグはリセット
    cpu.step();
    
    assertEquals(0x52, cpu.registers.a); // 結果は01010010（右シフトしてキャリーが0）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常に0
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0001, cpu.pc); // PCが1バイト増加
  }

  // MARK: RRA C1
  @Test
  @DisplayName("Test RRA with initial carry")
  public void testRRA_WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0xA4; // 10100100
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    
    assertEquals(0xD2, cpu.registers.a); // 結果は11010010（右シフトしてキャリーが最上位ビットに）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常に0
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（0）
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA LSB=0
  @Test
  @DisplayName("Test RRA with LSB=0")
  public void testRRA_LSBZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0x7E; // 01111110 (最下位ビットが0)
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x3F, cpu.registers.a); // 結果は00111111
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA LSB=1
  @Test
  @DisplayName("Test RRA with LSB=1")
  public void testRRA_LSBOne() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0xC3; // 11000011 (最下位ビットが1)
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x61, cpu.registers.a); // 結果は01100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA Z-変更なし
  @Test
  @DisplayName("Test RRA always resets zero flag")
  public void testRRA_ZeroFlagAlwaysReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0x01; // 00000001
    cpu.registers.f.zero = true; // ゼロフラグを事前にセット
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertFalse(cpu.registers.f.zero); // ゼロフラグは必ず0（結果が0でも）
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA フラグリセット
  @Test
  @DisplayName("Test RRA resets flags")
  public void testRRA_ResetsFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
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

  // MARK: RRA 連続実行
  @Test
  @DisplayName("Test consecutive RRA operations")
  public void testRRA_Consecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    
    // 1回目の実行
    cpu.registers.a = 0xA5; // 10100101
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x52, cpu.registers.a); // 結果は01010010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xA9, cpu.registers.a); // 結果は10101001（前回のキャリーが最上位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x54, cpu.registers.a); // 結果は01010100
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }

  // MARK: RRA オールゼロ
  @Test
  @DisplayName("Test RRA with all zeros")
  public void testRRA_AllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 結果も00000000
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常に0
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA オールワン
  @Test
  @DisplayName("Test RRA with all ones")
  public void testRRA_AllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0xFF; // 11111111
    cpu.registers.f.carry = false;
    cpu.step();
    
    assertEquals(0x7F, cpu.registers.a); // 結果は01111111
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA C→A
  @Test
  @DisplayName("Test RRA carry to A")
  public void testRRA_CarryToA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.registers.f.carry = true; // キャリーフラグはセット
    cpu.step();
    
    assertEquals(0x80, cpu.registers.a); // 結果は10000000（キャリーが最上位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが発生しない
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRA A→C→A
  @Test
  @DisplayName("Test RRA A to carry to A")
  public void testRRA_AToCaryToA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    
    // 1回目の実行：最下位ビットがキャリーに
    cpu.registers.a = 0x01; // 00000001
    cpu.registers.f.carry = false;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行：キャリーが最上位ビットに
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 結果は10000000
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: RRA バイトテスト
  @Test
  @DisplayName("Test RRA is a single byte instruction")
  public void testRRA_ByteSize() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1F); // RRA命令
    cpu.bus.writeByte(0x0001, 0x00); // NOP命令 (次の命令)
    cpu.registers.a = 0x55; // 01010101
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    assertEquals(0xAA, cpu.registers.a); // 結果は10101010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    assertEquals(0x0001, cpu.pc); // PCは1バイト進む
    
    cpu.step(); // 次のNOP命令を実行
    
    assertEquals(0x0002, cpu.pc); // さらに1バイト進む
  }
}