package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RRCATest {
  // MARK: RRCA 基本
  @Test
  @DisplayName("Test basic RRCA operation")
  public void testBasicRRCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.a); // 結果は11000010（右循環シフト）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // キャリーフラグは最下位ビットの値（1）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: RRCA LSB=0
  @Test
  @DisplayName("Test RRCA with LSB=0")
  public void testRRCAWithLSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x84; // 10000100
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA LSB=1
  @Test
  @DisplayName("Test RRCA with LSB=1")
  public void testRRCAWithLSB1() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x01; // 00000001
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1なのでキャリーは1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA オールワン
  @Test
  @DisplayName("Test RRCA with all ones")
  public void testRRCAWithAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 結果も11111111（変化なし）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA オールゼロ
  @Test
  @DisplayName("Test RRCA with all zeros")
  public void testRRCAWithAllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果も00000000（変化なし）
    assertFalse(cpu.registers.f.zero); // ゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA 連続実行
  @Test
  @DisplayName("Test consecutive RRCA operations")
  public void testConsecutiveRRCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    
    // 1回目の実行
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.a); // 結果は11000010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x61, cpu.registers.a); // 結果は01100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0 (0xC2)
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xB0, cpu.registers.a); // 結果は10110000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }

  // MARK: RRCA フラグリセット
  @Test
  @DisplayName("Test RRCA always resets flags")
  public void testRRCAResetsFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x02; // 00000010
    
    // 事前にフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    
    cpu.step();
    
    assertEquals(0x01, cpu.registers.a); // 結果は00000001
    assertFalse(cpu.registers.f.zero); // ゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセット
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA 8回実行
  @Test
  @DisplayName("Test RRCA returns to original value after 8 operations")
  public void testRRCAFullRotation() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    
    // 初期値
    int originalValue = 0x01; // 00000001
    cpu.registers.a = originalValue;
    
    // 8回の回転で元の値に戻るはず
    for (int i = 0; i < 8; i++) {
      cpu.pc = 0x0000; // PCをリセット
      cpu.step();
    }
    
    // 8回の右循環シフト後、元の値に戻る
    assertEquals(originalValue, cpu.registers.a);
    assertFalse(cpu.registers.f.carry); // 最後は0が最下位ビット
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA Z常時クリア
  @Test
  @DisplayName("Test RRCA always clears zero flag")
  public void testRRCAAlwaysClearsZeroFlag() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.registers.a = 0x00; // 00000000（結果も0）
    cpu.registers.f.zero = true; // 事前にゼロフラグをセット
    
    cpu.step();
    
    assertFalse(cpu.registers.f.zero); // 結果が0でもゼロフラグは常にリセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: RRCA バイトテスト
  @Test
  @DisplayName("Test RRCA is a single byte instruction")
  public void testRRCAByteSize() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    cpu.bus.writeByte(0x0001, 0x00); // NOP命令 (次の命令)
    cpu.registers.a = 0x80;
    
    cpu.step();
    
    assertEquals(0x40, cpu.registers.a); // 結果は01000000
    assertFalse(cpu.registers.f.carry); // キャリーは0
    assertEquals(0x0001, cpu.pc); // PCは1バイト進む
    
    cpu.step(); // 次のNOP命令を実行
    
    assertEquals(0x0002, cpu.pc); // さらに1バイト進む
  }

  // MARK: RRCA ビットパターン
  @Test
  @DisplayName("Test RRCA with various bit patterns")
  public void testRRCAWithVariousBitPatterns() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0F); // RRCA命令
    
    // ビット0だけセット
    cpu.registers.a = 0x01; // 00000001
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 結果は10000000
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // ビット7だけセット
    cpu.pc = 0x0000;
    cpu.registers.a = 0x80; // 10000000
    cpu.step();
    assertEquals(0x40, cpu.registers.a); // 結果は01000000
    assertFalse(cpu.registers.f.carry); // キャリーは0
    
    // 交互パターン1
    cpu.pc = 0x0000;
    cpu.registers.a = 0x55; // 01010101
    cpu.step();
    assertEquals(0xAA, cpu.registers.a); // 結果は10101010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 交互パターン2
    cpu.pc = 0x0000;
    cpu.registers.a = 0xAA; // 10101010
    cpu.step();
    assertEquals(0x55, cpu.registers.a); // 結果は01010101
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }
}
