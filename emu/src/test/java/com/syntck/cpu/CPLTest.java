package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CPLTest {
  // MARK: CPL (基本操作)
  @Test
  @DisplayName("Test CPL basic operation")
  public void testCPLBasic() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    cpu.registers.a = 0x35; // 0011 0101
    
    // フラグの初期状態を設定
    cpu.registers.f.zero = false;
    cpu.registers.f.subtract = false;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    assertEquals(0xCA, cpu.registers.a); // 0011 0101 -> 1100 1010 (0xCA)
    assertFalse(cpu.registers.f.zero); // ゼロフラグは変化しない
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグはセットされる
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグはセットされる
    assertFalse(cpu.registers.f.carry); // キャリーフラグは変化しない
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CPL (0x00)
  @Test
  @DisplayName("Test CPL with A=0x00")
  public void testCPLZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    cpu.registers.a = 0x00; // 全ビット0
    
    cpu.step();
    
    assertEquals(0xFF, cpu.registers.a); // 0000 0000 -> 1111 1111 (0xFF)
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグはセットされる
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグはセットされる
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CPL (0xFF)
  @Test
  @DisplayName("Test CPL with A=0xFF")
  public void testCPLAllOnes() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    cpu.registers.a = 0xFF; // 全ビット1
    
    cpu.step();
    
    assertEquals(0x00, cpu.registers.a); // 1111 1111 -> 0000 0000 (0x00)
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグはセットされる
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグはセットされる
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CPL (フラグ保存)
  @Test
  @DisplayName("Test CPL preserves Z and C flags")
  public void testCPLPreservesFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    cpu.registers.a = 0x55;
    
    // フラグの初期状態を設定
    cpu.registers.f.zero = true; // ゼロフラグをセット
    cpu.registers.f.subtract = false;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = true; // キャリーフラグをセット
    
    cpu.step();
    
    assertEquals(0xAA, cpu.registers.a); // 0101 0101 -> 1010 1010 (0xAA)
    assertTrue(cpu.registers.f.zero); // ゼロフラグは変化しない（元の値を維持）
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグはセットされる
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグはセットされる
    assertTrue(cpu.registers.f.carry); // キャリーフラグは変化しない（元の値を維持）
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CPL (連続実行)
  @Test
  @DisplayName("Test consecutive CPL instructions")
  public void testConsecutiveCPL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // 1回目のCPL
    cpu.bus.writeByte(0x0001, 0x2F); // 2回目のCPL
    
    cpu.registers.a = 0x35;
    
    // 1回目のCPL実行
    cpu.step();
    assertEquals(0xCA, cpu.registers.a); // 0011 0101 -> 1100 1010
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグはセットされる
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグはセットされる
    
    // 2回目のCPL実行
    cpu.step();
    assertEquals(0x35, cpu.registers.a); // 1100 1010 -> 0011 0101（元の値に戻る）
    assertTrue(cpu.registers.f.subtract); // サブトラクトフラグは引き続きセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは引き続きセット
    assertEquals(0x0002, cpu.pc); // PCが2増加（2命令実行）
  }

  // MARK: CPL (パターン)
  @Test
  @DisplayName("Test CPL with various patterns")
  public void testCPLPatterns() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    
    // パターン1: 0xAA (10101010)
    cpu.registers.a = 0xAA;
    cpu.step();
    assertEquals(0x55, cpu.registers.a); // 1010 1010 -> 0101 0101
    cpu.pc = 0x0000; // PCをリセット
    
    // パターン2: 0x55 (01010101)
    cpu.registers.a = 0x55;
    cpu.step();
    assertEquals(0xAA, cpu.registers.a); // 0101 0101 -> 1010 1010
    cpu.pc = 0x0000; // PCをリセット
    
    // パターン3: 0x0F (00001111)
    cpu.registers.a = 0x0F;
    cpu.step();
    assertEquals(0xF0, cpu.registers.a); // 0000 1111 -> 1111 0000
    cpu.pc = 0x0000; // PCをリセット
    
    // パターン4: 0xF0 (11110000)
    cpu.registers.a = 0xF0;
    cpu.step();
    assertEquals(0x0F, cpu.registers.a); // 1111 0000 -> 0000 1111
  }

  // MARK: CPL (レジスタ保存)
  @Test
  @DisplayName("Test CPL does not affect other registers")
  public void testCPLPreservesOtherRegisters() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    
    // 各レジスタに値をセット
    cpu.registers.a = 0x55;
    cpu.registers.b = 0xBC;
    cpu.registers.c = 0xDE;
    cpu.registers.d = 0xF1;
    cpu.registers.e = 0x23;
    cpu.registers.h = 0x45;
    cpu.registers.l = 0x67;
    cpu.sp = 0xFFF0;
    
    cpu.step();
    
    // Aレジスタだけが変化していることを確認
    assertEquals(0xAA, cpu.registers.a); // 0101 0101 -> 1010 1010
    
    // 他のレジスタは変化しないことを確認
    assertEquals(0xBC, cpu.registers.b);
    assertEquals(0xDE, cpu.registers.c);
    assertEquals(0xF1, cpu.registers.d);
    assertEquals(0x23, cpu.registers.e);
    assertEquals(0x45, cpu.registers.h);
    assertEquals(0x67, cpu.registers.l);
    assertEquals(0xFFF0, cpu.sp);
    assertEquals(0x0001, cpu.pc); // PCが1増加
  }

  // MARK: CPL (命令後)
  @Test
  @DisplayName("Test operations after CPL")
  public void testOperationsAfterCPL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2F); // CPL命令
    cpu.bus.writeByte(0x0001, 0x87); // ADD A, A命令
    
    cpu.registers.a = 0x55;
    
    // CPL実行
    cpu.step();
    assertEquals(0xAA, cpu.registers.a); // 0101 0101 -> 1010 1010
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    
    // ADD実行
    cpu.step();
    assertEquals(0x54, cpu.registers.a); // 1010 1010 + 1010 1010 = 0101 0100 (キャリー発生)
    assertFalse(cpu.registers.f.subtract); // ADD命令によってサブトラクトフラグはクリアされる
    assertTrue(cpu.registers.f.carry); // キャリーが発生
    assertEquals(0x0002, cpu.pc); // PCが2増加（2命令実行）
  }
}
