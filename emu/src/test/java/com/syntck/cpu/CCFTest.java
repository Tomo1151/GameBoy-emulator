package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CCFTest {
  // MARK: CCF (C=0)
  @Test
  @DisplayName("Test CCF when carry flag is reset")
  public void testCCFWhenCarryFlagReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.registers.f.subtract = true; // サブトラクトフラグをセット
    cpu.registers.f.halfCarry = true; // ハーフキャリーフラグをセット
    cpu.registers.f.zero = true; // ゼロフラグをセット
    
    cpu.step();
    
    assertTrue(cpu.registers.f.carry); // キャリーフラグが反転して1になる
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertTrue(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CCF (C=1)
  @Test
  @DisplayName("Test CCF when carry flag is set")
  public void testCCFWhenCarryFlagSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.registers.f.subtract = true; // サブトラクトフラグをセット
    cpu.registers.f.halfCarry = true; // ハーフキャリーフラグをセット
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    
    cpu.step();
    
    assertFalse(cpu.registers.f.carry); // キャリーフラグが反転して0になる
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertFalse(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CCF after arithmetic
  @Test
  @DisplayName("Test CCF after arithmetic operation")
  public void testCCFAfterArithmetic() throws Exception {
    CPU cpu = new CPU();
    // 最初に加算命令を実行
    cpu.bus.writeByte(0x0000, 0x87); // ADD A, A
    cpu.registers.a = 0x80; // オーバーフローを起こす値
    
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 加算結果: 0x80 + 0x80 = 0x100, 下位8ビットは0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertTrue(cpu.registers.f.carry); // 桁あふれが発生するのでキャリーフラグはセット
    
    // 続いてCCF命令を実行
    cpu.bus.writeByte(0x0001, 0x3F); // CCF命令
    
    cpu.step();
    
    assertFalse(cpu.registers.f.carry); // キャリーフラグが反転して0になる
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグはリセットされる
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグはリセットされる
    assertTrue(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertEquals(0x0002, cpu.pc); // PCが2増加（2命令実行）
  }

  // MARK: CCF preserves other register values
  @Test
  @DisplayName("Test CCF preserves other register values")
  public void testCCFPreservesRegisters() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    
    // 各レジスタに値をセット
    cpu.registers.a = 0xA5;
    cpu.registers.b = 0xB6;
    cpu.registers.c = 0xC7;
    cpu.registers.d = 0xD8;
    cpu.registers.e = 0xE9;
    cpu.registers.h = 0x12;
    cpu.registers.l = 0x34;
    cpu.sp = 0xFFF0;
    
    cpu.registers.f.carry = true; // キャリーフラグをセット
    
    cpu.step();
    
    // CCF実行後もレジスタ値は変わらないことを確認
    assertEquals(0xA5, cpu.registers.a);
    assertEquals(0xB6, cpu.registers.b);
    assertEquals(0xC7, cpu.registers.c);
    assertEquals(0xD8, cpu.registers.d);
    assertEquals(0xE9, cpu.registers.e);
    assertEquals(0x12, cpu.registers.h);
    assertEquals(0x34, cpu.registers.l);
    assertEquals(0xFFF0, cpu.sp);
    assertEquals(0x0001, cpu.pc); // PCが1増加
    
    assertFalse(cpu.registers.f.carry); // キャリーフラグは反転
  }

  // MARK: CCF effect with all flags set
  @Test
  @DisplayName("Test CCF with all flags initially set")
  public void testCCFWithAllFlagsSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    
    // すべてのフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // CCF実行後のフラグ状態を確認
    assertTrue(cpu.registers.f.zero); // Z: 変化なし
    assertFalse(cpu.registers.f.subtract); // N: クリア
    assertFalse(cpu.registers.f.halfCarry); // H: クリア
    assertFalse(cpu.registers.f.carry); // C: 反転
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
  
  // MARK: CCF effect with all flags reset
  @Test
  @DisplayName("Test CCF with all flags initially reset")
  public void testCCFWithAllFlagsReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // CCF命令
    
    // すべてのフラグをリセット
    cpu.registers.f.zero = false;
    cpu.registers.f.subtract = false;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    // CCF実行後のフラグ状態を確認
    assertFalse(cpu.registers.f.zero); // Z: 変化なし
    assertFalse(cpu.registers.f.subtract); // N: 変化なし（すでに0）
    assertFalse(cpu.registers.f.halfCarry); // H: 変化なし（すでに0）
    assertTrue(cpu.registers.f.carry); // C: 反転
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }
  
  // MARK: Multiple consecutive CCF instructions
  @Test
  @DisplayName("Test multiple consecutive CCF instructions")
  public void testConsecutiveCCF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3F); // 1回目のCCF
    cpu.bus.writeByte(0x0001, 0x3F); // 2回目のCCF
    cpu.bus.writeByte(0x0002, 0x3F); // 3回目のCCF
    
    // 初期フラグ状態
    cpu.registers.f.carry = false;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    
    // 1回目のCCF実行
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: 0→1
    assertFalse(cpu.registers.f.subtract); // N: リセット
    assertFalse(cpu.registers.f.halfCarry); // H: リセット
    
    // 2回目のCCF実行
    cpu.step();
    assertFalse(cpu.registers.f.carry); // C: 1→0
    assertFalse(cpu.registers.f.subtract); // N: リセット
    assertFalse(cpu.registers.f.halfCarry); // H: リセット
    
    // 3回目のCCF実行
    cpu.step();
    assertTrue(cpu.registers.f.carry); // C: 0→1
    assertFalse(cpu.registers.f.subtract); // N: リセット
    assertFalse(cpu.registers.f.halfCarry); // H: リセット
    
    assertEquals(0x0003, cpu.pc); // PCが3増加（3命令実行）
  }
}
