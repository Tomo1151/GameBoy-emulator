package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RRCTest {
  // MARK: RRC (A)
  @Test
  @DisplayName("Test RRC A instruction")
  public void testRRCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x0F); // RRC A
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.a); // 結果は11000010（最下位ビットが最上位に移動）
    assertFalse(cpu.registers.f.zero); // ゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // 最下位ビットがキャリーに移動（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: RRC (B)
  @Test
  @DisplayName("Test RRC B instruction")
  public void testRRCB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x08); // RRC B
    cpu.registers.b = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.b); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは最下位ビット（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (C)
  @Test
  @DisplayName("Test RRC C instruction")
  public void testRRCC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x09); // RRC C
    cpu.registers.c = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.c); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (D)
  @Test
  @DisplayName("Test RRC D instruction")
  public void testRRCD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0A); // RRC D
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.d); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (E)
  @Test
  @DisplayName("Test RRC E instruction")
  public void testRRCE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0B); // RRC E
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.e); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (H)
  @Test
  @DisplayName("Test RRC H instruction")
  public void testRRCH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0C); // RRC H
    cpu.registers.h = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.h); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (L)
  @Test
  @DisplayName("Test RRC L instruction")
  public void testRRCL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0D); // RRC L
    cpu.registers.l = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.l); // 結果は11000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC ((HL))
  @Test
  @DisplayName("Test RRC (HL) instruction")
  public void testRRCHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0E); // RRC (HL)
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

  // MARK: RRC (A) LSB=0
  @Test
  @DisplayName("Test RRC A with LSB=0")
  public void testRRCAWithLSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0F); // RRC A
    cpu.registers.a = 0x84; // 10000100
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (B) Z
  @Test
  @DisplayName("Test RRC B resulting in zero")
  public void testRRCBZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x08); // RRC B
    cpu.registers.b = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 結果も00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (C) オールワン
  @Test
  @DisplayName("Test RRC C with all ones")
  public void testRRCCAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x09); // RRC C
    cpu.registers.c = 0xFF; // 11111111
    cpu.step();
    assertEquals(0xFF, cpu.registers.c); // 結果も11111111（変化なし）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (D) 連続実行
  @Test
  @DisplayName("Test consecutive RRC D operations")
  public void testRRCDConsecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0A); // RRC D
    
    // 1回目の実行
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0xC2, cpu.registers.d); // 結果は11000010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x61, cpu.registers.d); // 結果は01100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 今回はキャリーが0
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0xB0, cpu.registers.d); // 結果は10110000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }

  // MARK: RRC (E) ビット1のみ
  @Test
  @DisplayName("Test RRC E with only bit 0 set")
  public void testRRCEOnlyBit0Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0B); // RRC E
    cpu.registers.e = 0x01; // 00000001
    cpu.step();
    assertEquals(0x80, cpu.registers.e); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (H) ビット7のみ
  @Test
  @DisplayName("Test RRC H with only bit 7 set")
  public void testRRCHOnlyBit7Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0C); // RRC H
    cpu.registers.h = 0x80; // 10000000
    cpu.step();
    assertEquals(0x40, cpu.registers.h); // 結果は01000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC (L) 8回実行
  @Test
  @DisplayName("Test RRC L returns to original value after 8 operations")
  public void testRRCLFullRotation() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0D); // RRC L
    
    // 初期値
    int originalValue = 0x01; // 00000001
    cpu.registers.l = originalValue;
    
    // 8回の回転で元の値に戻るはず
    for (int i = 0; i < 8; i++) {
      cpu.pc = 0x0000; // PCをリセット
      cpu.step();
    }
    
    // 8回の右循環シフト後、元の値に戻る
    assertEquals(originalValue, cpu.registers.l);
    assertFalse(cpu.registers.f.carry); // 最後は0が最下位ビット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC ((HL)) フラグ変化
  @Test
  @DisplayName("Test RRC (HL) flag changes")
  public void testRRCHLFlagChanges() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0E); // RRC (HL)
    cpu.registers.set_hl(0xC000);
    
    // 最下位ビットが1の場合
    cpu.bus.writeByte(0xC000, 0x01); // 00000001
    cpu.step();
    assertEquals(0x80, cpu.bus.readByte(0xC000)); // 結果は10000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 最下位ビットが0の場合
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0xC000, 0x02); // 00000010
    cpu.step();
    assertEquals(0x01, cpu.bus.readByte(0xC000)); // 結果は00000001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
  }

  // MARK: RRC (A) Z
  @Test
  @DisplayName("Test RRC A resulting in zero")
  public void testRRCAZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0F); // RRC A
    cpu.registers.a = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果も00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RRC 全レジスタチェック
  @Test
  @DisplayName("Test RRC instruction on all registers")
  public void testRRCAllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x01; // 00000001
    final int expectedResult = 0x80; // 10000000
    
    // RRC A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0F); // RRC A
    cpu.registers.a = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.a);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    
    // RRC B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x08); // RRC B
    cpu.registers.b = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.b);
    assertTrue(cpu.registers.f.carry);
    
    // RRC C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x09); // RRC C
    cpu.registers.c = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.c);
    assertTrue(cpu.registers.f.carry);
    
    // RRC D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0A); // RRC D
    cpu.registers.d = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.d);
    assertTrue(cpu.registers.f.carry);
    
    // RRC E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0B); // RRC E
    cpu.registers.e = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.e);
    assertTrue(cpu.registers.f.carry);
    
    // RRC H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0C); // RRC H
    cpu.registers.h = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.h);
    assertTrue(cpu.registers.f.carry);
    
    // RRC L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0D); // RRC L
    cpu.registers.l = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.l);
    assertTrue(cpu.registers.f.carry);
    
    // RRC (HL)
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x0E); // RRC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.step();
    assertEquals(expectedResult, cpu.bus.readByte(0xC000));
    assertTrue(cpu.registers.f.carry);
  }
}
