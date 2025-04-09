package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CPTest {
  // MARK: CP (A, B)
  @Test
  @DisplayName("Test CP A, B instruction")
  public void testCPAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB8); // CP A, B
    cpu.registers.a = 0x42;
    cpu.registers.b = 0x40;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > B）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, C)
  @Test
  @DisplayName("Test CP A, C instruction")
  public void testCPAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB9); // CP A, C
    cpu.registers.a = 0x42;
    cpu.registers.c = 0x42;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertTrue(cpu.registers.f.zero); // 値が等しいのでゼロフラグはセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = C）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, D)
  @Test
  @DisplayName("Test CP A, D instruction")
  public void testCPAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBA); // CP A, D
    cpu.registers.a = 0x50;
    cpu.registers.d = 0x42;
    cpu.step();
    assertEquals(0x50, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生
    assertFalse(cpu.registers.f.carry); // ボローが発生しない（A < D）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, E)
  @Test
  @DisplayName("Test CP A, E instruction")
  public void testCPAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBB); // CP A, E
    cpu.registers.a = 0x42;
    cpu.registers.e = 0x40;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > E）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, H)
  @Test
  @DisplayName("Test CP A, H instruction")
  public void testCPAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBC); // CP A, H
    cpu.registers.a = 0x42;
    cpu.registers.h = 0x40;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > H）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, L)
  @Test
  @DisplayName("Test CP A, L instruction")
  public void testCPAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBD); // CP A, L
    cpu.registers.a = 0x42;
    cpu.registers.l = 0x40;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > L）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, (HL))
  @Test
  @DisplayName("Test CP A, (HL) instruction")
  public void testCPAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBE); // CP A, (HL)
    cpu.registers.a = 0x42;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x40); // メモリ位置C000に0x40を書き込む
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > (HL)）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, d8)
  @Test
  @DisplayName("Test CP A, d8 instruction")
  public void testCPAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xFE); // CP A, d8
    cpu.bus.writeByte(0x0001, 0x40); // 即値0x40
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > d8）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: CP (A, A)
  @Test
  @DisplayName("Test CP A, A instruction")
  public void testCPAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBF); // CP A, A
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x42, cpu.registers.a); // Aレジスタの値は変化しない
    assertTrue(cpu.registers.f.zero); // 値が等しいのでゼロフラグはセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = A）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, D) C
  @Test
  @DisplayName("Test CP A, D with carry")
  public void testCPADWithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xBA); // CP A, D
    cpu.registers.a = 0x10;
    cpu.registers.d = 0x20;
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertTrue(cpu.registers.f.carry); // ボローが発生する（A < D）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CP (A, d8) H
  @Test
  @DisplayName("Test CP A, d8 with half carry")
  public void testCPAd8WithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xFE); // CP A, d8
    cpu.bus.writeByte(0x0001, 0x1F); // 即値0x1F
    cpu.registers.a = 0x10;
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0 < F）
    assertTrue(cpu.registers.f.carry); // ボローが発生する（A < d8）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: CP (A, C) HC
  @Test
  @DisplayName("Test CP A, C with half carry and carry")
  public void testCPACWithHalfCarryAndCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xB9); // CP A, C
    cpu.registers.a = 0x02;
    cpu.registers.c = 0x13;
    cpu.step();
    assertEquals(0x02, cpu.registers.a); // Aレジスタの値は変化しない
    assertFalse(cpu.registers.f.zero); // 値が等しくないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（2 < 3）
    assertTrue(cpu.registers.f.carry); // ボローが発生する（A < C）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
}