package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SBCTest {
  // MARK: SBC (A, B) C
  @Test
  @DisplayName("Test SBC A, B with carry")
  public void testSBCAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x98); // SBC A, B
    cpu.registers.a = 0x30;
    cpu.registers.b = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからBとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > B + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する (0x0 - 0x0 - 1)
  }

  // MARK: SBC (A, B)
  @Test
  @DisplayName("Test SBC A, B without carry")
  public void testSBCABWithoutCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x98); // SBC A, B
    cpu.registers.a = 0x30;
    cpu.registers.b = 0x10;
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x20, cpu.registers.a); // AからBを減算（0x30 - 0x10 = 0x20）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > B）
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
  }

  // MARK: SBC (A, C) C
  @Test
  @DisplayName("Test SBC A, C with carry")
  public void testSBCAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x99); // SBC A, C
    cpu.registers.a = 0x30;
    cpu.registers.c = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからCとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > C + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, D) C
  @Test
  @DisplayName("Test SBC A, D with carry")
  public void testSBCAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9A); // SBC A, D
    cpu.registers.a = 0x30;
    cpu.registers.d = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからDとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > D + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, E) C
  @Test
  @DisplayName("Test SBC A, E with carry")
  public void testSBCAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9B); // SBC A, E
    cpu.registers.a = 0x30;
    cpu.registers.e = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからEとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > E + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, H) C
  @Test
  @DisplayName("Test SBC A, H with carry")
  public void testSBCAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9C); // SBC A, H
    cpu.registers.a = 0x30;
    cpu.registers.h = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからHとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > H + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, L) C
  @Test
  @DisplayName("Test SBC A, L with carry")
  public void testSBCAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9D); // SBC A, L
    cpu.registers.a = 0x30;
    cpu.registers.l = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // AからLとキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > L + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, (HL)) C
  @Test
  @DisplayName("Test SBC A, (HL) with carry")
  public void testSBCAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9E); // SBC A, (HL)
    cpu.registers.a = 0x30;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x10); // メモリ位置C000に0x10を書き込む
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // Aから(HL)とキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > (HL) + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, d8) C
  @Test
  @DisplayName("Test SBC A, d8 with carry")
  public void testSBCAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xDE); // SBC A, d8
    cpu.bus.writeByte(0x0001, 0x10); // 即値0x10
    cpu.registers.a = 0x30;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1F, cpu.registers.a); // Aから即値とキャリーを減算（0x30 - 0x10 - 1 = 0x1F）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > d8 + carry）
    assertTrue(cpu.registers.f.halfCarry);  // 下位4ビットでボローが発生する
  }

  // MARK: SBC (A, A) C
  @Test
  @DisplayName("Test SBC A, A with carry")
  public void testSBCAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9F); // SBC A, A
    cpu.registers.a = 0x42;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // AからAとキャリーを減算（0x42 - 0x42 - 1 = -1 = 0xFF）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < A + carry）
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生
  }

  // MARK: SBC (A, A)
  @Test
  @DisplayName("Test SBC A, A without carry")
  public void testSBCAAwithoutCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9F); // SBC A, A
    cpu.registers.a = 0x42;
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AからAを減算（0x42 - 0x42 = 0）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = A）
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
  }

  // MARK: SBC (A, B) HC
  @Test
  @DisplayName("Test SBC A, B with half carry")
  public void testSBCABWithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x98); // SBC A, B
    cpu.registers.a = 0x20;
    cpu.registers.b = 0x11;
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x0F, cpu.registers.a); // AからBを減算（0x20 - 0x11 = 0x0F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > B）
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（0 < 1）
  }

  // MARK: SBC (A, C) Z
  @Test
  @DisplayName("Test SBC A, C resulting in zero")
  public void testSBCACZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x99); // SBC A, C
    cpu.registers.a = 0x10;
    cpu.registers.c = 0x10;
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AからCを減算（0x10 - 0x10 = 0）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = C）
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
  }

  // MARK: SBC (A, d8) CHC
  @Test
  @DisplayName("Test SBC A, d8 with carry and half carry")
  public void testSBCAd8CarryAndHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xDE); // SBC A, d8
    cpu.bus.writeByte(0x0001, 0x21); // 即値0x21
    cpu.registers.a = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0xEE, cpu.registers.a); // Aから即値とキャリーを減算（0x10 - 0x21 - 1 = -0x12 = 0xEE）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < d8 + carry）
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（0 < 1）
  }

  // MARK: SBC (A, E) ZC
  @Test
  @DisplayName("Test SBC A, E resulting in zero with carry")
  public void testSBCAEZeroWithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9B); // SBC A, E
    cpu.registers.a = 0x11;
    cpu.registers.e = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AからEとキャリーを減算（0x11 - 0x10 - 1 = 0）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > E + carry）
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
  }

  // MARK: SBC (A, L) ZCHC
  @Test
  @DisplayName("Test SBC A, L with all flags set")
  public void testSBCALAllFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x9D); // SBC A, L
    cpu.registers.a = 0x00;
    cpu.registers.l = 0x00;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // AからLとキャリーを減算（0x00 - 0x00 - 1 = -1 = 0xFF）
    assertEquals(0x0001, cpu.pc); // PCが1増加
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < L + carry）
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生
  }
}
