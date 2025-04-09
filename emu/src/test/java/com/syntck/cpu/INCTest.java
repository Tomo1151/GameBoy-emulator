package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class INCTest {
  // MARK: INC (A)
  @Test
  @DisplayName("Test INC A instruction")
  public void testINCA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3C); // INC A
    cpu.registers.a = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.a);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (B)
  @Test
  @DisplayName("Test INC B instruction")
  public void testINCB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x04); // INC B
    cpu.registers.b = 0xFF;
    cpu.step();
    assertEquals(0x00, cpu.registers.b); // 0xFF + 1 = 0x00 (オーバーフロー)
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりが発生 (0xF + 1 = 0x10)
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (C)
  @Test
  @DisplayName("Test INC C instruction")
  public void testINCC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0C); // INC C
    cpu.registers.c = 0x0F;
    cpu.step();
    assertEquals(0x10, cpu.registers.c); // 0x0F + 1 = 0x10
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりが発生 (0xF + 1 = 0x10)
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (D)
  @Test
  @DisplayName("Test INC D instruction")
  public void testINCD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x14); // INC D
    cpu.registers.d = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.d);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (E)
  @Test
  @DisplayName("Test INC E instruction")
  public void testINCE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x1C); // INC E
    cpu.registers.e = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.e);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (H)
  @Test
  @DisplayName("Test INC H instruction")
  public void testINCH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x24); // INC H
    cpu.registers.h = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.h);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (L)
  @Test
  @DisplayName("Test INC L instruction")
  public void testINCL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2C); // INC L
    cpu.registers.l = 0x42;
    cpu.step();
    assertEquals(0x43, cpu.registers.l);
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC ((HL))
  @Test
  @DisplayName("Test INC (HL) instruction")
  public void testINCHLAddr() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x34); // INC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x42);
    cpu.step();
    assertEquals(0x43, cpu.bus.readByte(0xC000)); // メモリ内の値がインクリメントされる
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertFalse(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりは発生しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC (A) HC
  @Test
  @DisplayName("Test INC A instruction with half carry")
  public void testINCAWithHalfCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3C); // INC A
    cpu.registers.a = 0x0F; // 下位4ビットが全て1
    cpu.step();
    assertEquals(0x10, cpu.registers.a); // 0x0F + 1 = 0x10
    assertFalse(cpu.registers.f.zero); // 結果が0ではないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりが発生 (0xF + 1 = 0x10)
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INC ((HL)) Z
  @Test
  @DisplayName("Test INC (HL) instruction resulting in zero")
  public void testINCHLAddrWithZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x34); // INC (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0xFF); // 0xFF + 1 = 0x00（オーバーフロー）
    cpu.step();
    assertEquals(0x00, cpu.bus.readByte(0xC000)); // オーバーフローして0になる
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグがセット
    assertFalse(cpu.registers.f.subtract); // インクリメント命令なのでサブトラクトフラグはリセット
    assertTrue(cpu.registers.f.halfCarry); // 下位4ビットで桁上がりが発生 (0xF + 1 = 0x10)
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (BC)
  @Test
  @DisplayName("Test INCRP BC instruction")
  public void testINCRPBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x03); // INC BC
    cpu.registers.set_bc(0x1234);
    cpu.step();
    assertEquals(0x1235, cpu.registers.get_bc());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (DE)
  @Test
  @DisplayName("Test INCRP DE instruction")
  public void testINCRPDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x13); // INC DE
    cpu.registers.set_de(0x5678);
    cpu.step();
    assertEquals(0x5679, cpu.registers.get_de());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (HL)
  @Test
  @DisplayName("Test INCRP HL instruction")
  public void testINCRPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x23); // INC HL
    cpu.registers.set_hl(0xABCD);
    cpu.step();
    assertEquals(0xABCE, cpu.registers.get_hl());
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (SP)
  @Test
  @DisplayName("Test INCRP SP instruction")
  public void testINCRPSP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x33); // INC SP
    cpu.sp = 0xFEDC;
    cpu.step();
    assertEquals(0xFEDD, cpu.sp);
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP overflow
  @Test
  @DisplayName("Test INCRP with overflow (FFFF to 0000)")
  public void testINCRPOverflow() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x03); // INC BC
    cpu.registers.set_bc(0xFFFF);
    cpu.step();
    assertEquals(0x0000, cpu.registers.get_bc()); // 0xFFFF + 1 = 0x10000、下位16ビットは0x0000
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (BC) high byte
  @Test
  @DisplayName("Test INCRP BC instruction affecting high byte")
  public void testINCRPBCHighByte() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x03); // INC BC
    cpu.registers.set_bc(0x12FF);
    cpu.step();
    assertEquals(0x1300, cpu.registers.get_bc()); // BCが0x12FFから0x1300に増加（高バイトも変更される）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: INCRP (HL) component regs
  @Test
  @DisplayName("Test INCRP HL instruction affecting H and L registers")
  public void testINCRPHLComponentRegs() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x23); // INC HL
    cpu.registers.h = 0x34;
    cpu.registers.l = 0xFF;
    cpu.step();
    assertEquals(0x35, cpu.registers.h); // Hレジスタが0x34から0x35に増加
    assertEquals(0x00, cpu.registers.l); // Lレジスタが0xFFから0x00に増加（オーバーフロー）
    assertEquals(0x3500, cpu.registers.get_hl()); // HL全体は0x34FFから0x3500に増加
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
}