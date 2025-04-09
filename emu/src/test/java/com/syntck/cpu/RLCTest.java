package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RLCTest {
  // MARK: RLC (A)
  @Test
  @DisplayName("Test RLC A instruction")
  public void testRLCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x07); // RLC A
    cpu.registers.a = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.a); // 結果は00001011（左回転し最上位ビットが最下位ビットになる）
    assertFalse(cpu.registers.f.zero); // 結果は0ではない
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常に0
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常に0
    assertTrue(cpu.registers.f.carry); // 最上位ビット(1)がキャリーに移動
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }
  
  // MARK: RLC (B)
  @Test
  @DisplayName("Test RLC B instruction")
  public void testRLCB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x00); // RLC B
    cpu.registers.b = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.b); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (C)
  @Test
  @DisplayName("Test RLC C instruction")
  public void testRLCC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x01); // RLC C
    cpu.registers.c = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.c); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (D)
  @Test
  @DisplayName("Test RLC D instruction")
  public void testRLCD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x02); // RLC D
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.d); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (E)
  @Test
  @DisplayName("Test RLC E instruction")
  public void testRLCE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x03); // RLC E
    cpu.registers.e = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.e); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (H)
  @Test
  @DisplayName("Test RLC H instruction")
  public void testRLCH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x04); // RLC H
    cpu.registers.h = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.h); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (L)
  @Test
  @DisplayName("Test RLC L instruction")
  public void testRLCL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x05); // RLC L
    cpu.registers.l = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.l); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC ((HL))
  @Test
  @DisplayName("Test RLC (HL) instruction")
  public void testRLCHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x06); // RLC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x85); // メモリ位置C000に0x85を書き込む
    cpu.step();
    assertEquals(0x0B, cpu.bus.readByte(0xC000)); // 結果は00001011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (A) Z
  @Test
  @DisplayName("Test RLC A resulting in zero")
  public void testRLCAZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x07); // RLC A
    cpu.registers.a = 0x00; // 00000000
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 結果も00000000（0を回転しても0）
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // キャリーはセットされない
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (A) MSB
  @Test
  @DisplayName("Test RLC A with MSB set")
  public void testRLCAWithMSB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x07); // RLC A
    cpu.registers.a = 0x80; // 10000000 - 最上位ビットのみ1
    cpu.step();
    assertEquals(0x01, cpu.registers.a); // 結果は00000001（左回転して最上位ビットが最下位ビットに）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (B) フラグテスト
  @Test
  @DisplayName("Test RLC B flag effects")
  public void testRLCBFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x00); // RLC B
    
    // 事前にフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = false;
    
    cpu.registers.b = 0x01; // 00000001
    cpu.step();
    assertEquals(0x02, cpu.registers.b); // 結果は00000010（左回転）
    assertFalse(cpu.registers.f.zero); // 結果は0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセット
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーフラグもリセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (C) オールワン
  @Test
  @DisplayName("Test RLC C with all ones")
  public void testRLCCAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x01); // RLC C
    cpu.registers.c = 0xFF; // 11111111 - すべてのビットが1
    cpu.step();
    assertEquals(0xFF, cpu.registers.c); // 結果も11111111（すべて1の回転なので変化なし）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーフラグはセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (D) 連続実行
  @Test
  @DisplayName("Test consecutive RLC D operations")
  public void testConsecutiveRLCD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x02); // RLC D
    
    // 最初の実行
    cpu.registers.d = 0x85; // 10000101
    cpu.step();
    assertEquals(0x0B, cpu.registers.d); // 結果は00001011
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    
    // 2回目の実行（PCを戻す）
    cpu.pc = 0x0000;
    cpu.step();
    assertEquals(0x16, cpu.registers.d); // 結果は00010110（00001011を左回転）
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーフラグはリセット
    
    // 3回目の実行（PCを戻す）
    cpu.pc = 0x0000;
    cpu.step();
    assertEquals(0x2C, cpu.registers.d); // 結果は00101100（00010110を左回転）
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーフラグはリセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (E) LSB
  @Test
  @DisplayName("Test RLC E with LSB set")
  public void testRLCEWithLSB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x03); // RLC E
    cpu.registers.e = 0x01; // 00000001 - 最下位ビットのみ1
    cpu.step();
    assertEquals(0x02, cpu.registers.e); // 結果は00000010（左回転）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry); // 最上位ビットが0なのでキャリーフラグはリセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (H) 複数ビット
  @Test
  @DisplayName("Test RLC H with multiple bits")
  public void testRLCHMultipleBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x04); // RLC H
    cpu.registers.h = 0xAA; // 10101010
    cpu.step();
    assertEquals(0x55, cpu.registers.h); // 結果は01010101（10101010を左回転）
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry); // 最上位ビットが1なのでキャリーフラグはセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC ((HL)) メモリアクセス
  @Test
  @DisplayName("Test RLC (HL) memory access")
  public void testRLCHLMemoryAccess() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CBプレフィックス
    cpu.bus.writeByte(0x0001, 0x06); // RLC (HL)
    cpu.registers.set_hl(0xC000);
    
    // メモリの複数の場所に値をセット（関連しない場所は変更されないことを確認）
    cpu.bus.writeByte(0xBFFF, 0x11);
    cpu.bus.writeByte(0xC000, 0x85); // 対象の値: 10000101
    cpu.bus.writeByte(0xC001, 0x22);
    
    cpu.step();
    
    assertEquals(0x0B, cpu.bus.readByte(0xC000)); // 結果は00001011
    assertEquals(0x11, cpu.bus.readByte(0xBFFF)); // 変更されていない
    assertEquals(0x22, cpu.bus.readByte(0xC001)); // 変更されていない
    assertTrue(cpu.registers.f.carry); // キャリーフラグがセット
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: RLC (A) 全レジスタチェック
  @Test
  @DisplayName("Test RLC instruction on all registers")
  public void testRLCAllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x81; // 10000001
    final int expectedResult = 0x03; // 00000011（10000001を左回転）
    
    // RLC A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x07); // RLC A
    cpu.registers.a = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.a);
    assertTrue(cpu.registers.f.carry);
    
    // RLC B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x00); // RLC B
    cpu.registers.b = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.b);
    
    // RLC C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x01); // RLC C
    cpu.registers.c = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.c);
    
    // RLC D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x02); // RLC D
    cpu.registers.d = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.d);
    
    // RLC E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x03); // RLC E
    cpu.registers.e = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.e);
    
    // RLC H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x04); // RLC H
    cpu.registers.h = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.h);
    
    // RLC L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x05); // RLC L
    cpu.registers.l = testValue;
    cpu.step();
    assertEquals(expectedResult, cpu.registers.l);
    
    // RLC (HL)
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x06); // RLC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, testValue);
    cpu.step();
    assertEquals(expectedResult, cpu.bus.readByte(0xC000));
  }
}