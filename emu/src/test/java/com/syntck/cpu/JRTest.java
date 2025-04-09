package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JRTest {
  // MARK: JR (r8)
  @Test
  @DisplayName("Test JR r8 unconditional jump forward")
  public void testJRForward() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.step();
    assertEquals(0x0007, cpu.pc); // PCが0x0000 + 2 + 5 = 0x0007になる
  }

  // MARK: JR (r8) -
  @Test
  @DisplayName("Test JR r8 unconditional jump backward")
  public void testJRBackward() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0010; // 初期PCを設定
    cpu.bus.writeByte(0x0010, 0x18); // JR r8
    cpu.bus.writeByte(0x0011, 0xFA); // オフセット -6 (2の補数表現で0xFA = -6)
    cpu.step();
    assertEquals(0x000C, cpu.pc); // PCが0x0010 + 2 - 6 = 0x000Cになる
  }

  // MARK: JR NZ (r8) when Z=0
  @Test
  @DisplayName("Test JR NZ, r8 when Z=0")
  public void testJRNZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x20); // JR NZ, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    cpu.step();
    assertEquals(0x0007, cpu.pc); // Z=0 なのでジャンプする (0x0000 + 2 + 5 = 0x0007)
  }

  // MARK: JR NZ (r8) when Z=1
  @Test
  @DisplayName("Test JR NZ, r8 when Z=1")
  public void testJRNZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x20); // JR NZ, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.zero = true; // ゼロフラグをセット
    cpu.step();
    assertEquals(0x0002, cpu.pc); // Z=1 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JR Z (r8) when Z=1
  @Test
  @DisplayName("Test JR Z, r8 when Z=1")
  public void testJRZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x28); // JR Z, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.zero = true; // ゼロフラグをセット
    cpu.step();
    assertEquals(0x0007, cpu.pc); // Z=1 なのでジャンプする (0x0000 + 2 + 5 = 0x0007)
  }

  // MARK: JR Z (r8) when Z=0
  @Test
  @DisplayName("Test JR Z, r8 when Z=0")
  public void testJRZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x28); // JR Z, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    cpu.step();
    assertEquals(0x0002, cpu.pc); // Z=0 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JR NC (r8) when C=0
  @Test
  @DisplayName("Test JR NC, r8 when C=0")
  public void testJRNCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x30); // JR NC, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x0007, cpu.pc); // C=0 なのでジャンプする (0x0000 + 2 + 5 = 0x0007)
  }

  // MARK: JR NC (r8) when C=1
  @Test
  @DisplayName("Test JR NC, r8 when C=1")
  public void testJRNCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x30); // JR NC, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x0002, cpu.pc); // C=1 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JR C (r8) when C=1
  @Test
  @DisplayName("Test JR C, r8 when C=1")
  public void testJRCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x38); // JR C, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.carry = true; // キャリーフラグをセット
    cpu.step();
    assertEquals(0x0007, cpu.pc); // C=1 なのでジャンプする (0x0000 + 2 + 5 = 0x0007)
  }

  // MARK: JR C (r8) when C=0
  @Test
  @DisplayName("Test JR C, r8 when C=0")
  public void testJRCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x38); // JR C, r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    cpu.registers.f.carry = false; // キャリーフラグをリセット
    cpu.step();
    assertEquals(0x0002, cpu.pc); // C=0 なのでジャンプしない（PCは次の命令を指す）
  }

  // MARK: JR NZ (r8) backward
  @Test
  @DisplayName("Test JR NZ, r8 backward")
  public void testJRNZBackward() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0010; // 初期PCを設定
    cpu.bus.writeByte(0x0010, 0x20); // JR NZ, r8
    cpu.bus.writeByte(0x0011, 0xFA); // オフセット -6 (2の補数表現で0xFA = -6)
    cpu.registers.f.zero = false; // ゼロフラグをリセット
    cpu.step();
    assertEquals(0x000C, cpu.pc); // Z=0 なのでジャンプする (0x0010 + 2 - 6 = 0x000C)
  }

  // MARK: JR (r8) 最大オフセット+
  @Test
  @DisplayName("Test JR r8 with maximum positive offset")
  public void testJRMaxForward() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x7F); // 最大正オフセット +127
    cpu.step();
    assertEquals(0x0081, cpu.pc); // PCが0x0000 + 2 + 127 = 0x0081になる
  }

  // MARK: JR (r8) 最大オフセット-
  @Test
  @DisplayName("Test JR r8 with maximum negative offset")
  public void testJRMaxBackward() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0100; // 初期PCを設定
    cpu.bus.writeByte(0x0100, 0x18); // JR r8
    cpu.bus.writeByte(0x0101, 0x80); // 最大負オフセット -128 (2の補数表現で0x80 = -128)
    cpu.step();
    assertEquals(0x0082, cpu.pc); // PCが0x0100 + 2 - 128 = 0x0082になる
  }

  // MARK: JR ループ
  @Test
  @DisplayName("Test JR r8 in a loop")
  public void testJRLoop() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0xFE); // オフセット -2 (2の補数表現で0xFE = -2)
    
    // 2回ステップ実行（無限ループになるため制限）
    cpu.step(); // PCが0x0000にループバック
    
    // JRは2バイト命令なので、PCが2進んでから-2されるため、結果は元のPC位置
    assertEquals(0x0000, cpu.pc);
    
    cpu.step(); // もう一度ループ
    assertEquals(0x0000, cpu.pc);
  }

  // MARK: JR フラグ変化なし
  @Test
  @DisplayName("Test JR doesn't modify flags")
  public void testJRDoesNotModifyFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x05); // オフセット +5
    
    // フラグを事前に設定
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // フラグが変わっていないことを確認
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    
    assertEquals(0x0007, cpu.pc);
  }

  // MARK: JR 連続実行
  @Test
  @DisplayName("Test consecutive JR instructions")
  public void testConsecutiveJR() throws Exception {
    CPU cpu = new CPU();
    // 1つ目のJR
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x03); // オフセット +3
    
    // 2つ目のJR（1つ目のジャンプ先）
    cpu.bus.writeByte(0x0005, 0x18); // JR r8
    cpu.bus.writeByte(0x0006, 0x04); // オフセット +4
    
    // 実行
    cpu.step();
    assertEquals(0x0005, cpu.pc); // 1つ目のJRの結果
    
    cpu.step();
    assertEquals(0x000B, cpu.pc); // 2つ目のJRの結果 (0x0005 + 2 + 4 = 0x000B)
  }

  // MARK: ゼロオフセット
  @Test
  @DisplayName("Test JR with zero offset")
  public void testJRZeroOffset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x00); // オフセット 0
    cpu.step();
    assertEquals(0x0002, cpu.pc); // PCが0x0000 + 2 + 0 = 0x0002になる
  }
}
