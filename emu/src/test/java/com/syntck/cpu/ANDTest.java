package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class ANDTest {
  // MARK: AND (A, B)
  @Test
  @DisplayName("Test AND A, B instruction")
  public void testANDAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA0); // AND A, B
    cpu.registers.a = 0xF0;
    cpu.registers.b = 0x0F;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AとBのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, C)
  @Test
  @DisplayName("Test AND A, C instruction")
  public void testANDAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA1); // AND A, C
    cpu.registers.a = 0xFF;
    cpu.registers.c = 0xAA;
    cpu.step();
    assertEquals(0xAA, cpu.registers.a); // AとCのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, D)
  @Test
  @DisplayName("Test AND A, D instruction")
  public void testANDAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA2); // AND A, D
    cpu.registers.a = 0x55;
    cpu.registers.d = 0x33;
    cpu.step();
    assertEquals(0x11, cpu.registers.a); // AとDのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, E)
  @Test
  @DisplayName("Test AND A, E instruction")
  public void testANDAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA3); // AND A, E
    cpu.registers.a = 0x12;
    cpu.registers.e = 0x34;
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // AとEのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, H)
  @Test
  @DisplayName("Test AND A, H instruction")
  public void testANDAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA4); // AND A, H
    cpu.registers.a = 0x5A;
    cpu.registers.h = 0x3F;
    cpu.step();
    assertEquals(0x1A, cpu.registers.a); // AとHのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, L)
  @Test
  @DisplayName("Test AND A, L instruction")
  public void testANDAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA5); // AND A, L
    cpu.registers.a = 0xCC;
    cpu.registers.l = 0x33;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AとLのビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, (HL))
  @Test
  @DisplayName("Test AND A, (HL) instruction")
  public void testANDAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA6); // AND A, (HL)
    cpu.registers.a = 0xF0;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x0F); // メモリ位置C000に0x0Fを書き込む
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // Aと(HL)のビット単位AND
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, d8)
  @Test
  @DisplayName("Test AND A, d8 instruction")
  public void testANDAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE6); // AND A, d8
    cpu.bus.writeByte(0x0001, 0x3C); // 即値0x3C
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x18, cpu.registers.a); // Aと即値のビット単位AND
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, A)
  @Test
  @DisplayName("Test AND A, A instruction")
  public void testANDAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA7); // AND A, A
    cpu.registers.a = 0xFF;
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // AとAのビット単位AND（変化なし）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, B) Z
  @Test
  @DisplayName("Test AND A, B resulting in zero")
  public void testANDABZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xA0); // AND A, B
    cpu.registers.a = 0xF0;
    cpu.registers.b = 0x0F;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AとBのビット単位ANDは0
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, 0)
  @Test
  @DisplayName("Test AND A with zero")
  public void testANDWithZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE6); // AND A, d8
    cpu.bus.writeByte(0x0001, 0x00); // 即値0x00
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // Aと0のAND = 0
    assertEquals(0x0002, cpu.pc); // PCが2増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, FF)
  @Test
  @DisplayName("Test AND A with 0xFF (all bits set)")
  public void testANDWithAllBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE6); // AND A, d8
    cpu.bus.writeByte(0x0001, 0xFF); // 即値0xFF
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.registers.a); // Aと0xFFのAND = A（変化なし）
    assertEquals(0x0002, cpu.pc); // PCが2増加
    assertFalse(cpu.registers.f.zero); // 結果が非0なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }

  // MARK: AND (A, 0) repeated
  @Test
  @DisplayName("Test AND A with 0 when A is already 0")
  public void testANDWithZeroWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE6); // AND A, d8
    cpu.bus.writeByte(0x0001, 0x00); // 即値0x00
    cpu.registers.a = 0x00;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // 0と0のAND = 0
    assertEquals(0x0002, cpu.pc); // PCが2増加
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
  }
}
