package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RLCATest {
  // MARK: RLCA 基本
  @Test
  @DisplayName("Test basic RLCA instruction")
  public void testBasicRLCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.a); // 結果は00001011（左循環シフト）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // キャリーフラグは最上位ビットの値（1）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: RLCA MSB=1
  @Test
  @DisplayName("Test RLCA with MSB=1")
  public void testRLCAWithMSB1() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x80; // 10000000
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 結果は00000001（最上位ビットが最下位ビットに移動）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA MSB=0
  @Test
  @DisplayName("Test RLCA with MSB=0")
  public void testRLCAWithMSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x40; // 01000000
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA オールワン
  @Test
  @DisplayName("Test RLCA with all ones")
  public void testRLCAWithAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 結果も11111111（変化なし）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA オールゼロ
  @Test
  @DisplayName("Test RLCA with all zeros")
  public void testRLCAWithAllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果も00000000（変化なし）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA 連続実行
  @Test
  @DisplayName("Test consecutive RLCA operations")
  public void testConsecutiveRLCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    
    // 1回目の実行
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.a); // 結果は00001011
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x16, cpu.registers.a); // 結果は00010110
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x2C, cpu.registers.a); // 結果は00101100
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
  }

  // MARK: RLCA フラグリセット
  @Test
  @DisplayName("Test RLCA resets flags")
  public void testRLCAResetsFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x01; // 00000001
    
    // 事前にフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    
    cpu.step();
    
    assertEquals(0x02, cpu.registers.a); // 結果は00000010
    assertFalse(cpu.registers.f.zero); // ゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセット
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA 複数ビット
  @Test
  @DisplayName("Test RLCA with multiple bits")
  public void testRLCAWithMultipleBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0xAA; // 10101010
    cpu.step();
    assertEquals(0x55, cpu.registers.a); // 結果は01010101（ビットパターンが反転）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA 特殊パターン
  @Test
  @DisplayName("Test RLCA with special bit pattern")
  public void testRLCAWithSpecialBitPattern() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x81; // 10000001
    cpu.step();
    assertEquals(0x03, cpu.registers.a); // 結果は00000011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA 8回実行で元に戻る
  @Test
  @DisplayName("Test RLCA returns to original value after 8 operations")
  public void testRLCAFullRotation() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x01; // 00000001 (最下位ビットのみ1)
    
    // 最初の値を保存
    int originalValue = cpu.registers.a;
    
    // 8回実行
    for (int i = 0; i < 8; i++) {
      cpu.pc = 0x0000; // PCをリセット
      cpu.step();
    }
    
    // 8回のローテーション後、元の値に戻るはず
    assertEquals(originalValue, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA Z常時クリア
  @Test
  @DisplayName("Test RLCA always clears zero flag")
  public void testRLCAAlwaysClearsZeroFlag() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.registers.a = 0x00; // 00000000（結果も0）
    cpu.registers.f.zero = true; // 事前にゼロフラグをセット
    
    cpu.step();
    
    assertFalse(cpu.registers.f.zero); // 結果が0でもゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RLCA バイトテスト
  @Test
  @DisplayName("Test RLCA is a single byte instruction")
  public void testRLCAByteSize() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    cpu.bus.writeByte(0x0001, 0x00); // NOP命令 (次の命令)
    cpu.registers.a = 0x01;
    
    cpu.step();
    
    assertEquals(0x02, cpu.registers.a); // 結果は00000010
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0001, cpu.pc); // PCは1バイト進む
    
    cpu.step(); // 次のNOP命令を実行
    
    assertEquals(0x0002, cpu.pc); // さらに1バイト進む
  }

  // MARK: RLCA 1ビット左シフト
  @Test
  @DisplayName("Test RLCA shifts value left by 1 bit")
  public void testRLCAShiftsLeft() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x07); // RLCA命令
    
    // 単一ビットのパターンをテスト
    for (int i = 0; i < 7; i++) {
      int inputBit = 1 << i; // 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40
      int expectedOutput = 1 << (i + 1); // 1ビット左シフト
      
      cpu.pc = 0x0000; // PCをリセット
      cpu.registers.a = inputBit;
      cpu.step();
      
      assertEquals(expectedOutput, cpu.registers.a);
      assertFalse(cpu.registers.f.carry);
    }
    
    // MSBのテスト（左シフトして最下位ビットになる）
    cpu.pc = 0x0000;
    cpu.registers.a = 0x80; // 10000000
    cpu.step();
    
    assertEquals(0x01, cpu.registers.a); // 00000001
    assertTrue(cpu.registers.f.carry);
  }
}
