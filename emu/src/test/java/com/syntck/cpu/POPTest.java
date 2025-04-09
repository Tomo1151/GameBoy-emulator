package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class POPTest {
  // MARK: POP BC
  @Test
  @DisplayName("Test POP BC instruction")
  public void testPOPBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC1); // POP BC
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    cpu.bus.writeByte(cpu.sp, 0x34);     // 下位バイト (C)
    cpu.bus.writeByte(cpu.sp + 1, 0x12); // 上位バイト (B)
    
    cpu.step();
    
    // BCレジスタに正しく値が設定される
    assertEquals(0x12, cpu.registers.b);
    assertEquals(0x34, cpu.registers.c);
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP DE
  @Test
  @DisplayName("Test POP DE instruction")
  public void testPOPDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD1); // POP DE
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    cpu.bus.writeByte(cpu.sp, 0x78);     // 下位バイト (E)
    cpu.bus.writeByte(cpu.sp + 1, 0x56); // 上位バイト (D)
    
    cpu.step();
    
    // DEレジスタに正しく値が設定される
    assertEquals(0x56, cpu.registers.d);
    assertEquals(0x78, cpu.registers.e);
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP HL
  @Test
  @DisplayName("Test POP HL instruction")
  public void testPOPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE1); // POP HL
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    cpu.bus.writeByte(cpu.sp, 0xBC);     // 下位バイト (L)
    cpu.bus.writeByte(cpu.sp + 1, 0x9A); // 上位バイト (H)
    
    cpu.step();
    
    // HLレジスタに正しく値が設定される
    assertEquals(0x9A, cpu.registers.h);
    assertEquals(0xBC, cpu.registers.l);
    assertEquals(0x9ABC, cpu.registers.get_hl()); // 16ビットの値も検証
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP AF
  @Test
  @DisplayName("Test POP AF instruction")
  public void testPOPAF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF1); // POP AF
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    // フラグは Z=1, N=1, H=0, C=1 => 11010000 = 0xD0
    cpu.bus.writeByte(cpu.sp, 0xD0);     // 下位バイト (F)
    cpu.bus.writeByte(cpu.sp + 1, 0xDE); // 上位バイト (A)
    
    cpu.step();
    
    // Aレジスタに正しく値が設定される
    assertEquals(0xDE, cpu.registers.a);
    
    // フラグが正しく設定される
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP AF with all flags set
  @Test
  @DisplayName("Test POP AF with all flags set")
  public void testPOPAFAllFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF1); // POP AF
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    // すべてのフラグがセット Z=1, N=1, H=1, C=1 => 11110000 = 0xF0
    cpu.bus.writeByte(cpu.sp, 0xF0);     // 下位バイト (F)
    cpu.bus.writeByte(cpu.sp + 1, 0xFF); // 上位バイト (A)
    
    cpu.step();
    
    // Aレジスタに正しく値が設定される
    assertEquals(0xFF, cpu.registers.a);
    
    // フラグが正しく設定される
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP AF with no flags set
  @Test
  @DisplayName("Test POP AF with no flags set")
  public void testPOPAFNoFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF1); // POP AF
    
    // スタックに値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    // すべてのフラグがリセット Z=0, N=0, H=0, C=0 => 00000000 = 0x00
    cpu.bus.writeByte(cpu.sp, 0x00);     // 下位バイト (F)
    cpu.bus.writeByte(cpu.sp + 1, 0x42); // 上位バイト (A)
    
    cpu.step();
    
    // Aレジスタに正しく値が設定される
    assertEquals(0x42, cpu.registers.a);
    
    // フラグが正しく設定される
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP with zero values
  @Test
  @DisplayName("Test POP with zero values")
  public void testPOPZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC1); // POP BC
    
    // スタックにゼロ値をプッシュしておく
    int originalSP = cpu.sp;
    cpu.sp = cpu.sp - 2; // SPを2バイト分減らす
    cpu.bus.writeByte(cpu.sp, 0x00);     // 下位バイト (C)
    cpu.bus.writeByte(cpu.sp + 1, 0x00); // 上位バイト (B)
    
    cpu.step();
    
    // BCレジスタにゼロ値が設定される
    assertEquals(0x00, cpu.registers.b);
    assertEquals(0x00, cpu.registers.c);
    assertEquals(0x0000, cpu.registers.get_bc()); // 16ビットの値も検証
    
    // スタックポインタがポップ後に2バイト分増加する
    assertEquals(originalSP, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: POP at stack boundary
  @Test
  @DisplayName("Test POP at stack boundary")
  public void testPOPAtStackBoundary() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD1); // POP DE
    
    // スタックポインタを0xFFFCに設定（上位アドレス付近だが書き込み可能な範囲）
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x78); // 下位バイト (E)
    cpu.bus.writeByte(0xFFFD, 0x56); // 上位バイト (D)
    
    cpu.step();
    
    // DEレジスタに正しく値が設定される
    assertEquals(0x56, cpu.registers.d);
    assertEquals(0x78, cpu.registers.e);
    
    // スタックポインタが2バイト増加
    assertEquals(0xFFFE, cpu.sp);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: Consecutive POP instructions
  @Test
  @DisplayName("Test consecutive POP instructions")
  public void testConsecutivePOP() throws Exception {
    CPU cpu = new CPU();
    
    // 1つ目のPOP命令
    cpu.bus.writeByte(0x0000, 0xC1); // POP BC
    
    // 2つ目のPOP命令
    cpu.bus.writeByte(0x0001, 0xD1); // POP DE
    
    // スタックに2つの値をプッシュ
    int originalSP = cpu.sp;
    
    // まずDEの値をプッシュ（後にポップされるのでスタックの上位に配置）
    cpu.sp = cpu.sp - 2;
    cpu.bus.writeByte(cpu.sp, 0x78);     // E
    cpu.bus.writeByte(cpu.sp + 1, 0x56); // D
    
    // 次にBCの値をプッシュ（最初にポップされるのでスタックの最上位に配置）
    cpu.sp = cpu.sp - 2;
    cpu.bus.writeByte(cpu.sp, 0x34);     // C
    cpu.bus.writeByte(cpu.sp + 1, 0x12); // B
    
    // 1つ目のPOP実行
    cpu.step();
    
    // BCレジスタに正しく値が設定される
    assertEquals(0x12, cpu.registers.b);
    assertEquals(0x34, cpu.registers.c);
    assertEquals(originalSP - 2, cpu.sp); // SPが上昇してDEの位置に
    
    // 2つ目のPOP実行
    cpu.step();
    
    // DEレジスタに正しく値が設定される
    assertEquals(0x56, cpu.registers.d);
    assertEquals(0x78, cpu.registers.e);
    assertEquals(originalSP, cpu.sp); // SPが元の位置に戻る
    
    // PCが2増加（1命令ずつ）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: POP doesn't affect other registers
  @Test
  @DisplayName("Test POP doesn't modify other registers")
  public void testPOPDoesNotModifyOtherRegisters() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC1); // POP BC
    
    // スタックに値をプッシュしておく
    cpu.sp = cpu.sp - 2;
    cpu.bus.writeByte(cpu.sp, 0x34);     // C
    cpu.bus.writeByte(cpu.sp + 1, 0x12); // B
    
    // 他のレジスタに値をセット
    cpu.registers.d = 0x56;
    cpu.registers.e = 0x78;
    cpu.registers.h = 0x9A;
    cpu.registers.l = 0xBC;
    cpu.registers.a = 0xDE;
    
    cpu.step();
    
    // BCレジスタのみが変更されることを確認
    assertEquals(0x12, cpu.registers.b);
    assertEquals(0x34, cpu.registers.c);
    
    // 他のレジスタは変更されないことを確認
    assertEquals(0x56, cpu.registers.d);
    assertEquals(0x78, cpu.registers.e);
    assertEquals(0x9A, cpu.registers.h);
    assertEquals(0xBC, cpu.registers.l);
    assertEquals(0xDE, cpu.registers.a);
  }

  // MARK: POP with push-pop sequence
  @Test
  @DisplayName("Test POP after PUSH")
  public void testPOPAfterPUSH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    cpu.bus.writeByte(0x0001, 0xD1); // POP DE
    
    // BCレジスタに値をセット
    cpu.registers.b = 0x12;
    cpu.registers.c = 0x34;
    
    int originalSP = cpu.sp;
    
    // PUSH実行
    cpu.step();
    
    // SPが2減少
    assertEquals(originalSP - 2, cpu.sp);
    
    // スタックに正しい値がプッシュされる
    assertEquals(0x34, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0x12, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // POP実行
    cpu.step();
    
    // DEレジスタにBCの値がコピーされる
    assertEquals(0x12, cpu.registers.d);
    assertEquals(0x34, cpu.registers.e);
    
    // SPが元の値に戻る
    assertEquals(originalSP, cpu.sp);
    
    // PCが2増加（命令ごとに1ずつ）
    assertEquals(0x0002, cpu.pc);
  }
}
