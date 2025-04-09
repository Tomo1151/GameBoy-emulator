package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class ADCTest {
  // MARK: ADC (A, B) C
  @Test
  @DisplayName("Test ADC A, B with carry")
  public void testADCAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x88); // ADC A, B
    cpu.registers.a = 0x10;
    cpu.registers.b = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにBとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, B)
  @Test
  @DisplayName("Test ADC A, B without carry")
  public void testADCABWithoutCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x88); // ADC A, B
    cpu.registers.a = 0x10;
    cpu.registers.b = 0x20;
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x30, cpu.registers.a); // AにBを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, C) C
  @Test
  @DisplayName("Test ADC A, C with carry")
  public void testADCAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x89); // ADC A, C
    cpu.registers.a = 0x10;
    cpu.registers.c = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにCとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, D) C
  @Test
  @DisplayName("Test ADC A, D with carry")
  public void testADCAD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8A); // ADC A, D
    cpu.registers.a = 0x10;
    cpu.registers.d = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにDとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, E) C
  @Test
  @DisplayName("Test ADC A, E with carry")
  public void testADCAE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8B); // ADC A, E
    cpu.registers.a = 0x10;
    cpu.registers.e = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにEとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, H) C
  @Test
  @DisplayName("Test ADC A, H with carry")
  public void testADCAH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8C); // ADC A, H
    cpu.registers.a = 0x10;
    cpu.registers.h = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにHとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, L) C
  @Test
  @DisplayName("Test ADC A, L with carry")
  public void testADCAL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8D); // ADC A, L
    cpu.registers.a = 0x10;
    cpu.registers.l = 0x20;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // AにLとキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, (HL)) C
  @Test
  @DisplayName("Test ADC A, (HL) with carry")
  public void testADCAHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8E); // ADC A, (HL)
    cpu.registers.a = 0x10;
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x20); // メモリ位置C000に0x20を書き込む
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // Aに(HL)とキャリーを加算
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, d8) C
  @Test
  @DisplayName("Test ADC A, d8 with carry")
  public void testADCAd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCE); // ADC A, d8
    cpu.bus.writeByte(0x0001, 0x20); // 即値0x20
    cpu.registers.a = 0x10;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x31, cpu.registers.a); // Aに即値とキャリーを加算
    assertEquals(0x0002, cpu.pc); // PCが2増加（即値のため）
  }

  // MARK: ADC (A, A) C
  @Test
  @DisplayName("Test ADC A, A with carry and overflow")
  public void testADCAAWithCarryAndOverflow() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x8F); // ADC A, A
    cpu.registers.a = 0xFF;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // AにAとキャリーを加算（0xFF + 0xFF + 1 = 0x1FF、下位8ビットは0xFF）
    assertTrue(cpu.registers.f.carry); // キャリーが発生
    assertFalse(cpu.registers.f.zero); // ゼロではない
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーが発生
    assertFalse(cpu.registers.f.subtract); // 減算ではない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, B) HC
  @Test
  @DisplayName("Test ADC A, B with carry and half carry")
  public void testADCABWithCarryAndHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x88); // ADC A, B
    cpu.registers.a = 0x0F; // 下位4ビットが全て1
    cpu.registers.b = 0x01; // 1を加算
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x11, cpu.registers.a); // AにBとキャリーを加算（0x0F + 0x01 + 1 = 0x11）
    assertFalse(cpu.registers.f.carry); // キャリーは発生しない
    assertFalse(cpu.registers.f.zero); // ゼロではない
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーが発生
    assertFalse(cpu.registers.f.subtract); // 減算ではない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, C) ZHC
  @Test
  @DisplayName("Test ADC A, C resulting in zero")
  public void testADCACZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x89); // ADC A, C
    cpu.registers.a = 0xFF;
    cpu.registers.c = 0x00;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // AにCとキャリーを加算（0xFF + 0x00 + 1 = 0x100、下位8ビットは0x00）
    assertTrue(cpu.registers.f.carry); // キャリーが発生
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーが発生
    assertFalse(cpu.registers.f.subtract); // 減算ではない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: ADC (A, D) ZHC
  @Test
  @DisplayName("Test ADC A, d8 with multiple flags")
  public void testADCAd8MultipleFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCE); // ADC A, d8
    cpu.bus.writeByte(0x0001, 0xF0); // 即値0xF0
    cpu.registers.a = 0x0F;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // Aに即値とキャリーを加算（0x0F + 0xF0 + 1 = 0x100、下位8ビットは0x00）
    assertTrue(cpu.registers.f.carry); // キャリーが発生
    assertTrue(cpu.registers.f.zero); // 結果はゼロ
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーが発生
    assertFalse(cpu.registers.f.subtract); // 減算ではない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }
}
