package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class ORTest {
  // MARK: OR (A, B)
  @Test
  @DisplayName("Test OR A, B instruction")
  public void testORAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB0); // OR A, B
    cpu.registers.a = 0x5A;
    cpu.registers.b = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとBのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, C)
  @Test
  @DisplayName("Test OR A, C instruction")
  public void testORAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB1); // OR A, C
    cpu.registers.a = 0x5A;
    cpu.registers.c = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとCのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, D)
  @Test
  @DisplayName("Test OR A, D instruction")
  public void testORAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB2); // OR A, D
    cpu.registers.a = 0x5A;
    cpu.registers.d = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとDのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, E)
  @Test
  @DisplayName("Test OR A, E instruction")
  public void testORAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB3); // OR A, E
    cpu.registers.a = 0x5A;
    cpu.registers.e = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとEのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, H)
  @Test
  @DisplayName("Test OR A, H instruction")
  public void testORAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB4); // OR A, H
    cpu.registers.a = 0x5A;
    cpu.registers.h = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとHのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, L)
  @Test
  @DisplayName("Test OR A, L instruction")
  public void testORAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB5); // OR A, L
    cpu.registers.a = 0x5A;
    cpu.registers.l = 0x0F;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // AとLのビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, (HL))
  @Test
  @DisplayName("Test OR A, (HL) instruction")
  public void testORAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB6); // OR A, (HL)
    cpu.registers.a = 0x5A;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x0F); // メモリ位置C000に0x0Fを書き込む
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // Aと(HL)のビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR (A, d8)
  @Test
  @DisplayName("Test OR A, d8 instruction")
  public void testORAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF6); // OR A, d8
    cpu.bus.writeByte(0x0001, 0x0F); // 即値0x0F
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // Aと即値のビット単位OR
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: OR (A, A)
  @Test
  @DisplayName("Test OR A, A instruction")
  public void testORAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB7); // OR A, A
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.registers.a); // AとAのビット単位OR（変化なし）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR with zero value
  @Test
  @DisplayName("Test OR A with zero operand")
  public void testORWithZeroOperand() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB1); // OR A, C
    cpu.registers.a = 0x5A;
    cpu.registers.c = 0x00; // Cレジスタに0をセット
    cpu.step();
    assertEquals(0x5A, cpu.registers.a); // Aと0のOR = A（変化なし）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR resulting in zero
  @Test
  @DisplayName("Test OR resulting in zero")
  public void testORResultingInZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB0); // OR A, B
    cpu.registers.a = 0x00;
    cpu.registers.b = 0x00;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0と0のOR = 0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: OR with all bits set
  @Test
  @DisplayName("Test OR with all bits set (0xFF)")
  public void testORWithAllBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF6); // OR A, d8
    cpu.bus.writeByte(0x0001, 0xFF); // 即値0xFF（全ビットセット）
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // AとFFのOR = FF（全ビットセット）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: OR complementary values
  @Test
  @DisplayName("Test OR with complementary values")
  public void testORComplementary() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB0); // OR A, B
    cpu.registers.a = 0x55; // 0101 0101
    cpu.registers.b = 0xAA; // 1010 1010
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 相補的な値のOR = 全ビットセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: Sequential OR operations
  @Test
  @DisplayName("Test sequential OR operations")
  public void testSequentialOR() throws Exception {
    CPU cpu = new CPU();
    // 最初のOR命令
    cpu.bus.writeByte(0x0000, 0xF6); // OR A, d8
    cpu.bus.writeByte(0x0001, 0x0F); // 即値0x0F
    cpu.registers.a = 0x50; // 初期値
    
    // 最初の命令を実行
    cpu.step();
    assertEquals(0x5F, cpu.registers.a); // 0x50 OR 0x0F = 0x5F
    assertEquals(0x0002, cpu.pc);
    
    // 次のOR命令
    cpu.bus.writeByte(0x0002, 0xF6); // OR A, d8
    cpu.bus.writeByte(0x0003, 0xA0); // 即値0xA0
    
    // 2回目の命令を実行
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 0x5F OR 0xA0 = 0xFF
    assertEquals(0x0004, cpu.pc);
  }
}
