package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SCFTest {
  // MARK: SCF (C=0)
  @Test
  @DisplayName("Test SCF when carry flag is reset")
  public void testSCFWhenCarryFlagReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF命令
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.registers.f.subtract = true; // サブトラクトフラグをセット
    cpu.registers.f.halfCarry = true; // ハーフキャリーフラグをセット
    cpu.registers.f.zero = true; // ゼロフラグをセット
    
    cpu.step();
    
    assertTrue(cpu.registers.f.carry); // キャリーフラグが1になる
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertTrue(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SCF (C=1)
  @Test
  @DisplayName("Test SCF when carry flag is already set")
  public void testSCFWhenCarryFlagSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF命令
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.registers.f.subtract = true; // サブトラクトフラグをセット
    cpu.registers.f.halfCarry = true; // ハーフキャリーフラグをセット
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    
    cpu.step();
    
    assertTrue(cpu.registers.f.carry); // キャリーフラグは1のまま
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertFalse(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: SCF after arithmetic
  @Test
  @DisplayName("Test SCF after arithmetic operation")
  public void testSCFAfterArithmetic() throws Exception {
    CPU cpu = new CPU();
    // 最初に減算命令を実行
    cpu.bus.writeByte(0x0000, 0x90); // SUB A, B
    cpu.registers.a = 0x10;
    cpu.registers.b = 0x20; // A < B なので、キャリーフラグがセットされる
    
    cpu.step();
    
    assertEquals(0xF0, cpu.registers.a); // 減算結果: 0x10 - 0x20 = 0xF0（2の補数表現）
    assertTrue(cpu.registers.f.carry); // A < B なのでキャリーフラグはセット
    assertTrue(cpu.registers.f.subtract); // 減算なのでサブトラクトフラグもセット
    
    // 続いてSCF命令を実行
    cpu.bus.writeByte(0x0001, 0x37); // SCF命令
    
    cpu.step();
    
    assertTrue(cpu.registers.f.carry); // キャリーフラグは1のまま
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertFalse(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0002, cpu.pc); // PCが2増加（2命令実行）
  }

  // MARK: SCF preserves other register values
  @Test
  @DisplayName("Test SCF preserves other register values")
  public void testSCFPreservesRegisters() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF命令
    
    // 各レジスタに値をセット
    cpu.registers.a = 0xA5;
    cpu.registers.b = 0xB6;
    cpu.registers.c = 0xC7;
    cpu.registers.d = 0xD8;
    cpu.registers.e = 0xE9;
    cpu.registers.h = 0x12;
    cpu.registers.l = 0x34;
    cpu.sp = 0xFFF0;
    
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    
    cpu.step();
    
    // SCF実行後もレジスタ値は変わらないことを確認
    assertEquals(0xA5, cpu.registers.a);
    assertEquals(0xB6, cpu.registers.b);
    assertEquals(0xC7, cpu.registers.c);
    assertEquals(0xD8, cpu.registers.d);
    assertEquals(0xE9, cpu.registers.e);
    assertEquals(0x12, cpu.registers.h);
    assertEquals(0x34, cpu.registers.l);
    assertEquals(0xFFF0, cpu.sp);
    assertEquals(0x0001, cpu.pc); // PCが1増加
    
    assertTrue(cpu.registers.f.carry); // キャリーフラグはセット
  }

  // MARK: SCF effect with all flags set
  @Test
  @DisplayName("Test SCF with all flags initially set")
  public void testSCFWithAllFlagsSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF命令
    
    // すべてのフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // SCF実行後のフラグ状態を確認
    assertTrue(cpu.registers.f.zero); // Z: 変化なし
    assertFalse(cpu.registers.f.subtract); // N: クリア
    assertFalse(cpu.registers.f.halfCarry); // H: クリア
    assertTrue(cpu.registers.f.carry); // C: セット（既に1なので変化なし）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
  
  // MARK: SCF effect with all flags reset
  @Test
  @DisplayName("Test SCF with all flags initially reset")
  public void testSCFWithAllFlagsReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // SCF命令
    
    // すべてのフラグをリセット
    cpu.registers.f.zero = false;
    cpu.registers.f.subtract = false;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    // SCF実行後のフラグ状態を確認
    assertFalse(cpu.registers.f.zero); // Z: 変化なし
    assertFalse(cpu.registers.f.subtract); // N: 変化なし（すでに0）
    assertFalse(cpu.registers.f.halfCarry); // H: 変化なし（すでに0）
    assertTrue(cpu.registers.f.carry); // C: セット
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
  
  // MARK: Multiple consecutive SCF instructions
  @Test
  @DisplayName("Test multiple consecutive SCF instructions")
  public void testConsecutiveSCF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x37); // 1回目のSCF
    cpu.bus.writeByte(0x0001, 0x37); // 2回目のSCF
    cpu.bus.writeByte(0x0002, 0x37); // 3回目のSCF
    
    // 初期フラグ状態
    cpu.registers.f.carry = false;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    
    // 1回目のSCF実行
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: セット
    assertFalse(cpu.registers.f.subtract); // N: リセット
    assertFalse(cpu.registers.f.halfCarry); // H: リセット
    
    // 2回目のSCF実行
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: セット（変化なし）
    assertFalse(cpu.registers.f.subtract); // N: リセット（変化なし）
    assertFalse(cpu.registers.f.halfCarry); // H: リセット（変化なし）
    
    // 3回目のSCF実行
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: セット（変化なし）
    assertFalse(cpu.registers.f.subtract); // N: リセット（変化なし）
    assertFalse(cpu.registers.f.halfCarry); // H: リセット（変化なし）
    
    assertEquals(0x0003, cpu.pc); // PCが3増加（3命令実行）
  }

  // MARK: SCF after CCF
  @Test
  @DisplayName("Test SCF after CCF")
  public void testSCFAfterCCF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    cpu.bus.writeByte(0x0001, 0x37); // SCF命令
    
    // 初期フラグ状態
    cpu.registers.f.carry = true;
    
    // CCF実行（キャリーフラグが反転）
    cpu.step();
    assertFalse(cpu.registers.f.carry); // C: 反転して0になる
    
    // SCF実行（キャリーフラグをセット）
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: セットして1になる
    assertFalse(cpu.registers.f.subtract); // N: リセット
    assertFalse(cpu.registers.f.halfCarry); // H: リセット
    
    assertEquals(0x0002, cpu.pc); // PCが2増加（2命令実行）
  }
}
