package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SUBTest {
  // MARK: SUB (A, B)
  @Test
  @DisplayName("Test SUB A, B instruction")
  public void testSUBAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x90); // SUB A, B
    cpu.registers.a = 0x42;
    cpu.registers.b = 0x10;
    cpu.step();
    assertEquals(0x32, cpu.registers.a); // AからBを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > B）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, C)
  @Test
  @DisplayName("Test SUB A, C instruction")
  public void testSUBAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x91); // SUB A, C
    cpu.registers.a = 0x42;
    cpu.registers.c = 0x30;
    cpu.step();
    assertEquals(0x12, cpu.registers.a); // AからCを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > C）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, D)
  @Test
  @DisplayName("Test SUB A, D instruction")
  public void testSUBAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x92); // SUB A, D
    cpu.registers.a = 0x42;
    cpu.registers.d = 0x20;
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // AからDを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > D）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, E)
  @Test
  @DisplayName("Test SUB A, E instruction")
  public void testSUBAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x93); // SUB A, E
    cpu.registers.a = 0x42;
    cpu.registers.e = 0x20;
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // AからEを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > E）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, H)
  @Test
  @DisplayName("Test SUB A, H instruction")
  public void testSUBAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x94); // SUB A, H
    cpu.registers.a = 0x42;
    cpu.registers.h = 0x20;
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // AからHを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > H）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, L)
  @Test
  @DisplayName("Test SUB A, L instruction")
  public void testSUBAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x95); // SUB A, L
    cpu.registers.a = 0x42;
    cpu.registers.l = 0x20;
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // AからLを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > L）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, (HL))
  @Test
  @DisplayName("Test SUB A, (HL) instruction")
  public void testSUBAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x96); // SUB A, (HL)
    cpu.registers.a = 0x42;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x20); // メモリ位置C000に0x20を書き込む
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // Aから(HL)を減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > (HL)）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, d8)
  @Test
  @DisplayName("Test SUB A, d8 instruction")
  public void testSUBAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD6); // SUB A, d8
    cpu.bus.writeByte(0x0001, 0x20); // 即値0x20
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x22, cpu.registers.a); // Aから即値を減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > d8）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: SUB (A, A)
  @Test
  @DisplayName("Test SUB A, A instruction")
  public void testSUBAA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x97); // SUB A, A
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // A - A = 0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = A）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, B) C
  @Test
  @DisplayName("Test SUB A, B with carry")
  public void testSUBABWithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x90); // SUB A, B
    cpu.registers.a = 0x20;
    cpu.registers.b = 0x30; // Bの方が大きい
    cpu.step();
    assertEquals(0xF0, cpu.registers.a); // AからBを減算（負の値は2の補数で表現）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < B）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, C) H
  @Test
  @DisplayName("Test SUB A, C with half carry")
  public void testSUBACWithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x91); // SUB A, C
    cpu.registers.a = 0x10;
    cpu.registers.c = 0x01; // 下位4ビットで借りが発生
    cpu.step();
    assertEquals(0x0F, cpu.registers.a); // AからCを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（0x10 - 0x01 = 0x0F）
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > C）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, D) HC
  @Test
  @DisplayName("Test SUB A, D with half carry and carry")
  public void testSUBADWithHalfCarryAndCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x92); // SUB A, D
    cpu.registers.a = 0x02;
    cpu.registers.d = 0x13; // 下位4ビットと全体で借りが発生
    cpu.step();
    assertEquals(0xEF, cpu.registers.a); // AからDを減算（負の値は2の補数で表現）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（2 - 3 = -1）
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < D）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, H) Z
  @Test
  @DisplayName("Test SUB A, H resulting in zero")
  public void testSUBAHZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x94); // SUB A, H
    cpu.registers.a = 0x40;
    cpu.registers.h = 0x40;
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // A - H = 0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットでボローは発生しない
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A = H）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, L) HC
  @Test
  @DisplayName("Test SUB A, L with half carry")
  public void testSUBALWithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x95); // SUB A, L
    cpu.registers.a = 0x20;
    cpu.registers.l = 0x11; // 下位4ビットで借りが発生
    cpu.step();
    assertEquals(0x0F, cpu.registers.a); // AからLを減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（0 - 1 = -1）
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > L）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SUB (A, d8) C
  @Test
  @DisplayName("Test SUB A, d8 with carry")
  public void testSUBAd8WithCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD6); // SUB A, d8
    cpu.bus.writeByte(0x0001, 0xFF); // 即値0xFF
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.a); // Aから即値を減算（負の値は2の補数で表現）
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（2 - F = -13）
    assertTrue(cpu.registers.f.carry); // ボローが発生（A < d8）
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: SUB (A, (HL)) H
  @Test
  @DisplayName("Test SUB A, (HL) with half carry")
  public void testSUBAHLWithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x96); // SUB A, (HL)
    cpu.registers.a = 0x42;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x38); // メモリ位置C000に0x38を書き込む（下位4ビットで借りが発生）
    cpu.step();
    assertEquals(0x0A, cpu.registers.a); // Aから(HL)を減算
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットでボローが発生（2 - 8 = -6）
    assertFalse(cpu.registers.f.carry); // ボローは発生しない（A > (HL)）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
}
