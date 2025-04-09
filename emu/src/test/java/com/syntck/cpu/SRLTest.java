package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SRLTest {
  // MARK: SRL (A)
  @Test
  @DisplayName("Test SRL A instruction")
  public void testSRLA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x3F); // SRL A
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010（最上位ビットは常に0）
    assertFalse(cpu.registers.f.zero); // 結果はゼロではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertTrue(cpu.registers.f.carry); // キャリーフラグは最下位ビット（1）
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: SRL (B)
  @Test
  @DisplayName("Test SRL B instruction")
  public void testSRLB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x38); // SRL B
    cpu.registers.b = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.b); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは最下位ビットの値（1）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (C)
  @Test
  @DisplayName("Test SRL C instruction")
  public void testSRLC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x39); // SRL C
    cpu.registers.c = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.c); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (D)
  @Test
  @DisplayName("Test SRL D instruction")
  public void testSRLD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3A); // SRL D
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.d); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (E)
  @Test
  @DisplayName("Test SRL E instruction")
  public void testSRLE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3B); // SRL E
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.e); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (H)
  @Test
  @DisplayName("Test SRL H instruction")
  public void testSRLH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3C); // SRL H
    cpu.registers.h = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.h); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (L)
  @Test
  @DisplayName("Test SRL L instruction")
  public void testSRLL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3D); // SRL L
    cpu.registers.l = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.l); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL ((HL))
  @Test
  @DisplayName("Test SRL (HL) instruction")
  public void testSRLHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3E); // SRL (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.step();
    assertEquals(0x42, cpu.bus.readByte(0xC000)); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (A) LSB=0
  @Test
  @DisplayName("Test SRL A with LSB=0")
  public void testSRLAWithLSB0() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3F); // SRL A
    cpu.registers.a = 0x84; // 10000100
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // 結果は01000010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0なのでキャリーは0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (B) Z
  @Test
  @DisplayName("Test SRL B resulting in zero")
  public void testSRLBZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x38); // SRL B
    cpu.registers.b = 0x01; // 00000001
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (C) オールワン
  @Test
  @DisplayName("Test SRL C with all ones")
  public void testSRLCAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x39); // SRL C
    cpu.registers.c = 0xFF; // 11111111
    cpu.step();
    assertEquals(0x7F, cpu.registers.c); // 結果は01111111（最上位ビットは0になる）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (D) オールゼロ
  @Test
  @DisplayName("Test SRL D with all zeros")
  public void testSRLDAllZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3A); // SRL D
    cpu.registers.d = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.d); // 結果も00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL (E) 連続実行
  @Test
  @DisplayName("Test consecutive SRL E operations")
  public void testSRLEConsecutive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3B); // SRL E
    
    // 1回目の実行
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0x42, cpu.registers.e); // 結果は01000010
    assertTrue(cpu.registers.f.carry); // キャリーは1
    
    // 2回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x21, cpu.registers.e); // 結果は00100001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーは0
    
    // 3回目の実行
    cpu.pc = 0x0000; // PCをリセット
    cpu.step();
    assertEquals(0x10, cpu.registers.e); // 結果は00010000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーは1
  }

  // MARK: SRL (H) ビット7のみ
  @Test
  @DisplayName("Test SRL H with only bit 7 set")
  public void testSRLHOnlyBit7Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3C); // SRL H
    cpu.registers.h = 0x80; // 10000000
    cpu.step();
    assertEquals(0x40, cpu.registers.h); // 結果は01000000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最下位ビットが0
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL ((HL)) フラグ変化
  @Test
  @DisplayName("Test SRL (HL) flag changes")
  public void testSRLHLFlagChanges() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3E); // SRL (HL)
    cpu.registers.set_hl(0xC000);
    
    // 最下位ビットが1の場合
    cpu.bus.writeByte(0xC000, 0x01); // 00000001
    cpu.step();
    assertEquals(0x00, cpu.bus.readByte(0xC000)); // 結果は00000000
    assertTrue(cpu.registers.f.zero);
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

  // MARK: SRL (A) 8回実行
  @Test
  @DisplayName("Test SRL A with 8 shifts results in zero")
  public void testSRLAEightShifts() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3F); // SRL A
    
    // 初期値
    cpu.registers.a = 0xFF; // 11111111
    
    // 8回のシフトで全てのビットが右にシフトされるはず
    for (int i = 0; i < 8; i++) {
      cpu.pc = 0x0000; // PCをリセット
      cpu.step();
    }
    
    // 8回のシフト後、値は0になるはず
    assertEquals(0x00, cpu.registers.a);
    assertTrue(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最後のシフトの最下位ビットは1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL 全レジスタチェック
  @Test
  @DisplayName("Test SRL instruction on all registers")
  public void testSRLAllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x81; // 10000001
    final int expectedResult = 0x40; // 01000000
    
    // SRL A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3F); // SRL A
    cpu.registers.a = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.a);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1だったのでキャリーは1
    
    // SRL B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x38); // SRL B
    cpu.registers.b = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.b);
    assertTrue(cpu.registers.f.carry);
    
    // SRL C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x39); // SRL C
    cpu.registers.c = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.c);
    assertTrue(cpu.registers.f.carry);
    
    // SRL D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3A); // SRL D
    cpu.registers.d = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.d);
    assertTrue(cpu.registers.f.carry);
    
    // SRL E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3B); // SRL E
    cpu.registers.e = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.e);
    assertTrue(cpu.registers.f.carry);
    
    // SRL H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3C); // SRL H
    cpu.registers.h = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.h);
    assertTrue(cpu.registers.f.carry);
    
    // SRL L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3D); // SRL L
    cpu.registers.l = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.l);
    assertTrue(cpu.registers.f.carry);
    
    // SRL (HL)
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3E); // SRL (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.step();
    assertEquals(expectedResult, cpu.bus.readByte(0xC000));
    assertTrue(cpu.registers.f.carry);
  }

  // MARK: SRL (L) ビット0のみ
  @Test
  @DisplayName("Test SRL L with only bit 0 set")
  public void testSRLLOnlyBit0Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3D); // SRL L
    cpu.registers.l = 0x01; // 00000001
    cpu.step();
    assertEquals(0x00, cpu.registers.l); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロ
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1だったのでキャリーは1
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SRL ゼロフラグのみ
  @Test
  @DisplayName("Test SRL A with flags set before execution")
  public void testSRLAWithPresetFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x3F); // SRL A
    cpu.registers.a = 0x01; // 00000001
    
    // 事前にフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 結果は00000000
    assertTrue(cpu.registers.f.zero); // 結果がゼロなのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセット
    assertTrue(cpu.registers.f.carry); // 最下位ビットが1だったのでキャリーは1
    assertEquals(0x0002, cpu.pc);
  }
}
