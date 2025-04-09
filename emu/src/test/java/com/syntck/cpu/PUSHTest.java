package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PUSHTest {
  // MARK: PUSH BC
  @Test
  @DisplayName("Test PUSH BC instruction")
  public void testPUSHBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    int originalSP = cpu.sp;
    cpu.registers.b = 0x12;
    cpu.registers.c = 0x34;
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる (リトルエンディアン)
    assertEquals(0x34, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0x12, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH DE
  @Test
  @DisplayName("Test PUSH DE instruction")
  public void testPUSHDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD5); // PUSH DE
    int originalSP = cpu.sp;
    cpu.registers.d = 0x56;
    cpu.registers.e = 0x78;
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる
    assertEquals(0x78, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0x56, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH HL
  @Test
  @DisplayName("Test PUSH HL instruction")
  public void testPUSHHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE5); // PUSH HL
    int originalSP = cpu.sp;
    cpu.registers.h = 0x9A;
    cpu.registers.l = 0xBC;
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる
    assertEquals(0xBC, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0x9A, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH AF
  @Test
  @DisplayName("Test PUSH AF instruction")
  public void testPUSHAF() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF5); // PUSH AF
    int originalSP = cpu.sp;
    cpu.registers.a = 0xDE;
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = true;
    // Fレジスタの値は: Z=1, N=1, H=0, C=1 => 10010000 = 0x90
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる
    int flagsByte = cpu.bus.readByte(cpu.sp); // 下位バイト (フラグ)
    assertEquals(0xD0, flagsByte); // Z, N, C がセット => 11010000 = 0xD0
    assertEquals(0xDE, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト (A)
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH with zero values
  @Test
  @DisplayName("Test PUSH with zero values")
  public void testPUSHZeros() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    int originalSP = cpu.sp;
    cpu.registers.b = 0x00;
    cpu.registers.c = 0x00;
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックにゼロ値がプッシュされる
    assertEquals(0x00, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0x00, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH AF with all flags set
  @Test
  @DisplayName("Test PUSH AF with all flags set")
  public void testPUSHAFAllFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF5); // PUSH AF
    int originalSP = cpu.sp;
    cpu.registers.a = 0xFF;
    // すべてのフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    // Fレジスタの値は: Z=1, N=1, H=1, C=1 => 11110000 = 0xF0
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる
    assertEquals(0xF0, cpu.bus.readByte(cpu.sp)); // 下位バイト (フラグ)
    assertEquals(0xFF, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト (A)
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH AF with no flags set
  @Test
  @DisplayName("Test PUSH AF with no flags set")
  public void testPUSHAFNoFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF5); // PUSH AF
    int originalSP = cpu.sp;
    cpu.registers.a = 0x42;
    // すべてのフラグをリセット
    cpu.registers.f.zero = false;
    cpu.registers.f.subtract = false;
    cpu.registers.f.halfCarry = false;
    cpu.registers.f.carry = false;
    // Fレジスタの値は: Z=0, N=0, H=0, C=0 => 00000000 = 0x00
    cpu.step();
    
    // スタックポインタが2バイト減少する
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックに正しい値が順番にプッシュされる
    assertEquals(0x00, cpu.bus.readByte(cpu.sp)); // 下位バイト (フラグ)
    assertEquals(0x42, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト (A)
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: Consecutive PUSH instructions
  @Test
  @DisplayName("Test consecutive PUSH instructions")
  public void testConsecutivePUSH() throws Exception {
    CPU cpu = new CPU();
    // 1つ目のPUSH命令
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    cpu.registers.b = 0xAA;
    cpu.registers.c = 0xBB;
    int originalSP = cpu.sp;
    
    cpu.step();
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp); // SP-2
    assertEquals(0xBB, cpu.bus.readByte(cpu.sp)); // BC値の下位バイト
    assertEquals(0xAA, cpu.bus.readByte(cpu.sp + 1)); // BC値の上位バイト
    
    // 2つ目のPUSH命令
    cpu.bus.writeByte(0x0001, 0xD5); // PUSH DE
    cpu.registers.d = 0xCC;
    cpu.registers.e = 0xDD;
    int intermediateSP = cpu.sp;
    
    cpu.step();
    assertEquals((intermediateSP - 2) & 0xFFFF, cpu.sp); // さらにSP-2
    assertEquals(0xDD, cpu.bus.readByte(cpu.sp)); // DE値の下位バイト
    assertEquals(0xCC, cpu.bus.readByte(cpu.sp + 1)); // DE値の上位バイト
    
    // 元のBCの値がまだスタックに残っている
    assertEquals(0xBB, cpu.bus.readByte(cpu.sp + 2)); // BC値の下位バイト
    assertEquals(0xAA, cpu.bus.readByte(cpu.sp + 3)); // BC値の上位バイト
    
    // PCが2増加（1命令ずつ）
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: PUSH at stack limit
  @Test
  @DisplayName("Test PUSH at stack pointer boundary")
  public void testPUSHAtStackBoundary() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    // スタックポインタを境界付近に設定
    cpu.sp = 0x0002;
    cpu.registers.b = 0x12;
    cpu.registers.c = 0x34;
    
    cpu.step();
    
    // スタックポインタがラップアラウンドせずに正常に減少
    assertEquals(0x0000, cpu.sp);
    
    // スタックに正しい値がプッシュされる
    assertEquals(0x34, cpu.bus.readByte(0x0000)); // 下位バイト
    assertEquals(0x12, cpu.bus.readByte(0x0001)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH with highest value
  @Test
  @DisplayName("Test PUSH with maximum 16-bit value")
  public void testPUSHMaxValue() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE5); // PUSH HL
    int originalSP = cpu.sp;
    cpu.registers.h = 0xFF;
    cpu.registers.l = 0xFF;
    
    cpu.step();
    
    // スタックポインタが2バイト減少
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // 最大値0xFFFFがスタックにプッシュされる
    assertEquals(0xFF, cpu.bus.readByte(cpu.sp)); // 下位バイト
    assertEquals(0xFF, cpu.bus.readByte(cpu.sp + 1)); // 上位バイト
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: PUSH doesn't affect flags
  @Test
  @DisplayName("Test PUSH doesn't modify flags")
  public void testPUSHDoesNotModifyFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC5); // PUSH BC
    
    // フラグを設定
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // フラグは変更されない
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    
    // PCが1増加
    assertEquals(0x0001, cpu.pc);
  }
}
