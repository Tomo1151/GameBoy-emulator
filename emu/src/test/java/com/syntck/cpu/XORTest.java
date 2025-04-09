package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class XORTest {
  // MARK: XOR (A, B)
  @Test
  @DisplayName("Test XOR A, B instruction")
  public void testXORAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA8); // XOR A, B
    cpu.registers.a = 0x5A;
    cpu.registers.b = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとBのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, C)
  @Test
  @DisplayName("Test XOR A, C instruction")
  public void testXORAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA9); // XOR A, C
    cpu.registers.a = 0x5A;
    cpu.registers.c = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとCのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, D)
  @Test
  @DisplayName("Test XOR A, D instruction")
  public void testXORAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAA); // XOR A, D
    cpu.registers.a = 0x5A;
    cpu.registers.d = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとDのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, E)
  @Test
  @DisplayName("Test XOR A, E instruction")
  public void testXORAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAB); // XOR A, E
    cpu.registers.a = 0x5A;
    cpu.registers.e = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとEのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, H)
  @Test
  @DisplayName("Test XOR A, H instruction")
  public void testXORAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAC); // XOR A, H
    cpu.registers.a = 0x5A;
    cpu.registers.h = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとHのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, L)
  @Test
  @DisplayName("Test XOR A, L instruction")
  public void testXORAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAD); // XOR A, L
    cpu.registers.a = 0x5A;
    cpu.registers.l = 0x3F;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // AとLのビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, (HL))
  @Test
  @DisplayName("Test XOR A, (HL) instruction")
  public void testXORAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAE); // XOR A, (HL)
    cpu.registers.a = 0x5A;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x3F); // メモリ位置C000に0x3Fを書き込む
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // Aと(HL)のビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR (A, d8)
  @Test
  @DisplayName("Test XOR A, d8 instruction")
  public void testXORAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xEE); // XOR A, d8
    cpu.bus.writeByte(0x0001, 0x3F); // 即値0x3F
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x65, cpu.registers.a); // Aと即値のビット単位XOR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: XOR (A, A)
  @Test
  @DisplayName("Test XOR A, A instruction")
  public void testXORAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAF); // XOR A, A
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AとAのXORは常に0になる
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR with zero value
  @Test
  @DisplayName("Test XOR A with zero operand")
  public void testXORWithZeroOperand() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA9); // XOR A, C
    cpu.registers.a = 0x5A;
    cpu.registers.c = 0x00; // Cレジスタに0をセット
    cpu.step();
    assertEquals(0x5A, cpu.registers.a); // Aと0のXOR = A（変化なし）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR resulting in zero
  @Test
  @DisplayName("Test XOR resulting in zero")
  public void testXORResultingInZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA8); // XOR A, B
    cpu.registers.a = 0xFF;
    cpu.registers.b = 0xFF;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 同じ値同士のXORは0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: XOR with complementary values
  @Test
  @DisplayName("Test XOR with complementary values")
  public void testXORComplementary() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA8); // XOR A, B
    cpu.registers.a = 0x55; // 0101 0101
    cpu.registers.b = 0xAA; // 1010 1010
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 相補的な値のXOR = 全ビットセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: Sequential XOR operations
  @Test
  @DisplayName("Test sequential XOR operations")
  public void testSequentialXOR() throws Exception {
    CPU cpu = new CPU();
    // 最初のXOR命令
    cpu.bus.writeByte(0x0000, 0xEE); // XOR A, d8
    cpu.bus.writeByte(0x0001, 0xAA); // 即値0xAA
    cpu.registers.a = 0x55; // 初期値
    
    // 最初の命令を実行
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 0x55 XOR 0xAA = 0xFF
    assertEquals(0x0002, cpu.pc);
    
    // 次のXOR命令
    cpu.bus.writeByte(0x0002, 0xAF); // XOR A, A
    
    // 2回目の命令を実行
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0xFF XOR 0xFF = 0x00
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertEquals(0x0003, cpu.pc);
  }

  // MARK: XOR with all bits set
  @Test
  @DisplayName("Test XOR with all bits set")
  public void testXORWithAllBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xAB); // XOR A, E
    cpu.registers.a = 0x00;
    cpu.registers.e = 0xFF; // 全ビットセット
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 0 XOR 0xFF = 0xFF
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
}