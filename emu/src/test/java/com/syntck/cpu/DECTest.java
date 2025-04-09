package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class DECTest {
  // MARK: DEC (A)
  @Test
  @DisplayName("Test DEC A instruction")
  public void testDECA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3D); // DEC A
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x41, cpu.registers.a);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (B)
  @Test
  @DisplayName("Test DEC B instruction")
  public void testDECB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x05); // DEC B
    cpu.registers.b = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.b);
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (C)
  @Test
  @DisplayName("Test DEC C instruction")
  public void testDECC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0D); // DEC C
    cpu.registers.c = 0x10;
    cpu.step();
    assertEquals(0x0F, cpu.registers.c);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0x10 -> 0x0F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (D)
  @Test
  @DisplayName("Test DEC D instruction")
  public void testDECD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x15); // DEC D
    cpu.registers.d = 0x20;
    cpu.step();
    assertEquals(0x1F, cpu.registers.d);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0x20 -> 0x1F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (E)
  @Test
  @DisplayName("Test DEC E instruction")
  public void testDECE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1D); // DEC E
    cpu.registers.e = 0x00;
    cpu.step();
    assertEquals(0xFF, cpu.registers.e); // デクリメントで8ビットオーバーフロー発生
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0x00 -> 0xFF）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (H)
  @Test
  @DisplayName("Test DEC H instruction")
  public void testDECH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x25); // DEC H
    cpu.registers.h = 0x42;
    cpu.step();
    assertEquals(0x41, cpu.registers.h);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC (L)
  @Test
  @DisplayName("Test DEC L instruction")
  public void testDECL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2D); // DEC L
    cpu.registers.l = 0x42;
    cpu.step();
    assertEquals(0x41, cpu.registers.l);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC ((HL))
  @Test
  @DisplayName("Test DEC (HL) instruction")
  public void testDECHLAddr() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x35); // DEC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x42);
    cpu.step();
    assertEquals(0x41, cpu.bus.readByte(0xC000)); // メモリ内の値が1減少
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC Z
  @Test
  @DisplayName("Test DEC instruction resulting in zero")
  public void testDECZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x05); // DEC B
    cpu.registers.b = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.b);
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットのボローは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC H
  @Test
  @DisplayName("Test DEC instruction with half carry")
  public void testDECHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0D); // DEC C
    cpu.registers.c = 0x10;
    cpu.step();
    assertEquals(0x0F, cpu.registers.c);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0x10 -> 0x0F）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DEC overflow
  @Test
  @DisplayName("Test DEC instruction with overflow (0 to FF)")
  public void testDECOverflow() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3D); // DEC A
    cpu.registers.a = 0x00;
    cpu.step();
    assertEquals(0xFF, cpu.registers.a); // 0からデクリメントすると0xFFになる（8ビットオーバーフロー）
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertTrue(cpu.registers.f.subtract); // 減算命令なのでサブトラクトフラグはセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットのボローが発生（0x00 -> 0xFF）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (BC)
  @Test
  @DisplayName("Test DECRP BC instruction")
  public void testDECRPBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0B); // DEC BC
    cpu.registers.set_bc(0x1234);
    cpu.step();
    assertEquals(0x1233, cpu.registers.get_bc());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (DE)
  @Test
  @DisplayName("Test DECRP DE instruction")
  public void testDECRPDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1B); // DEC DE
    cpu.registers.set_de(0x5678);
    cpu.step();
    assertEquals(0x5677, cpu.registers.get_de());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (HL)
  @Test
  @DisplayName("Test DECRP HL instruction")
  public void testDECRPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2B); // DEC HL
    cpu.registers.set_hl(0xABCD);
    cpu.step();
    assertEquals(0xABCC, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (SP)
  @Test
  @DisplayName("Test DECRP SP instruction")
  public void testDECRPSP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3B); // DEC SP
    cpu.sp = 0xFEDC;
    cpu.step();
    assertEquals(0xFEDB, cpu.sp);
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP overflow
  @Test
  @DisplayName("Test DECRP with overflow (0000 to FFFF)")
  public void testDECRPOverflow() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0B); // DEC BC
    cpu.registers.set_bc(0x0000);
    cpu.step();
    assertEquals(0xFFFF, cpu.registers.get_bc()); // 0からデクリメントすると0xFFFFになる（16ビットオーバーフロー）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (BC) high byte
  @Test
  @DisplayName("Test DECRP BC instruction affecting high byte")
  public void testDECRPBCHighByte() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0B); // DEC BC
    cpu.registers.set_bc(0x1200);
    cpu.step();
    assertEquals(0x11FF, cpu.registers.get_bc()); // BCが0x1200から0x11FFに減少（高バイトも変更される）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: DECRP (HL) component regs
  @Test
  @DisplayName("Test DECRP HL instruction affecting H and L registers")
  public void testDECRPHLComponentRegs() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2B); // DEC HL
    cpu.registers.h = 0x34;
    cpu.registers.l = 0x00;
    cpu.step();
    assertEquals(0x33, cpu.registers.h); // Hレジスタが0x34から0x33に減少
    assertEquals(0xFF, cpu.registers.l); // Lレジスタが0x00から0xFFに減少（オーバーフロー）
    assertEquals(0x33FF, cpu.registers.get_hl()); // HL全体は0x3400から0x33FFに減少
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
}