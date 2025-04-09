package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JPTest {
  // MARK: JP (a16)
  @Test
  @DisplayName("Test JP a16 instruction")
  public void testJPa16() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC3); // JP a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが指定したアドレスに変更される
  }

  // MARK: JP NZ (a16) when Z=0
  @Test
  @DisplayName("Test JP NZ, a16 instruction when Z=0")
  public void testJPNZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC2); // JP NZ, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    cpu.step();
    assertEquals(0x1234, cpu.pc); // Z=0 なのでジャンプする
  }

  // MARK: JP NZ (a16) when Z=1
  @Test
  @DisplayName("Test JP NZ, a16 instruction when Z=1")
  public void testJPNZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC2); // JP NZ, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = true; // ゼロフラグをセット
    cpu.step();
    assertEquals(0x0003, cpu.pc); // Z=1 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JP Z (a16) when Z=1
  @Test
  @DisplayName("Test JP Z, a16 instruction when Z=1")
  public void testJPZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = true; // ゼロフラグをセット
    cpu.step();
    assertEquals(0x1234, cpu.pc); // Z=1 なのでジャンプする
  }

  // MARK: JP Z (a16) when Z=0
  @Test
  @DisplayName("Test JP Z, a16 instruction when Z=0")
  public void testJPZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    cpu.step();
    assertEquals(0x0003, cpu.pc); // Z=0 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JP NC (a16) when C=0
  @Test
  @DisplayName("Test JP NC, a16 instruction when C=0")
  public void testJPNCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD2); // JP NC, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x1234, cpu.pc); // C=0 なのでジャンプする
  }

  // MARK: JP NC (a16) when C=1
  @Test
  @DisplayName("Test JP NC, a16 instruction when C=1")
  public void testJPNCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD2); // JP NC, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x0003, cpu.pc); // C=1 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JP C (a16) when C=1
  @Test
  @DisplayName("Test JP C, a16 instruction when C=1")
  public void testJPCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xDA); // JP C, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x1234, cpu.pc); // C=1 なのでジャンプする
  }

  // MARK: JP C (a16) when C=0
  @Test
  @DisplayName("Test JP C, a16 instruction when C=0")
  public void testJPCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xDA); // JP C, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x0003, cpu.pc); // C=0 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JP (HL)
  @Test
  @DisplayName("Test JP (HL) instruction")
  public void testJPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE9); // JP (HL)
    cpu.registers.set_hl(0x1234);
    cpu.bus.writeByte(0x1234, 0x0024); // HLレジスタのアドレスに何か書き込む
    cpu.step();
    assertEquals(0x0024, cpu.bus.readByte(0x1234)); // HLレジスタのアドレスから値を読み取る
    assertEquals(0x0024, cpu.pc); // PCがHLレジスタが指すアドレス先の値に変更される
  }

  // MARK: Sequential JP instructions
  @Test
  @DisplayName("Test sequential JP instructions")
  public void testSequentialJP() throws Exception {
    CPU cpu = new CPU();
    // 最初のジャンプ命令
    cpu.bus.writeByte(0x0000, 0xC3); // JP a16
    cpu.bus.writeByte(0x0001, 0x10);
    cpu.bus.writeByte(0x0002, 0x20); // アドレス0x2010
    
    // 2番目のジャンプ命令（ジャンプ先に配置）
    cpu.bus.writeByte(0x2010, 0xC3); // JP a16
    cpu.bus.writeByte(0x2011, 0x30);
    cpu.bus.writeByte(0x2012, 0x40); // アドレス0x4030
    
    // 1回目のステップ
    cpu.step();
    assertEquals(0x2010, cpu.pc);
    
    // 2回目のステップ
    cpu.step();
    assertEquals(0x4030, cpu.pc);
  }

  // MARK: JP with all flags set
  @Test
  @DisplayName("Test JP with all flags set")
  public void testJPWithAllFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC3); // JP a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    
    // 全フラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが指定したアドレスに変更される
    
    // フラグは変更されないことを確認
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
  }
}