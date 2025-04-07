package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class MiscInstructionTest {

  // NOP命令のテスト
  @Test
  @DisplayName("Test NOP")
  public void testNop() throws Exception {
    CPU cpu = new CPU();
    int initialPc = cpu.pc;
    cpu.bus.writeByte(0x0000, 0x00); // NOP
    cpu.step();
    assertEquals(initialPc + 1, cpu.pc); // PCが1増加
  }

  // HALT命令のテスト
  @Test
  @DisplayName("Test HALT")
  public void testHalt() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x76); // HALT
    cpu.step();
    assertTrue(cpu.isHalted()); // HALTフラグがセット
    assertEquals(0x0001, cpu.pc);
  }

  // DI命令のテスト
  @Test
  @DisplayName("Test DI")
  public void testDI() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF3); // DI
    cpu.interruptMasterEnable = true;
    cpu.step();
    assertFalse(cpu.getInterruptMasterEnable()); // IMEがリセット
    assertEquals(0x0001, cpu.pc);
  }

  // EI命令のテスト
  @Test
  @DisplayName("Test EI")
  public void testEI() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xFB); // EI
    cpu.interruptMasterEnable = false;
    cpu.step();
    assertTrue(cpu.getInterruptMasterEnable()); // IMEがセット
    assertEquals(0x0001, cpu.pc);
  }

  // STOP命令のテスト
  @Test
  @DisplayName("Test STOP")
  public void testStop() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x10); // STOP
    cpu.bus.writeByte(0x0001, 0x00); // STOP命令は2バイト
    cpu.step();
    assertEquals(0x0002, cpu.pc); // PCが2増加
    // STOPの具体的な振る舞いはハードウェア依存
  }

  // DAA命令のテスト (BCDフォーマットの調整)
  @Test
  @DisplayName("Test DAA after addition")
  public void testDAA_AfterAddition() throws Exception {
    CPU cpu = new CPU();
    // まず加算
    cpu.bus.writeByte(0x0000, 0x80); // ADD A, B
    cpu.registers.a = 0x45;
    cpu.registers.b = 0x38;
    cpu.step();
    assertEquals(0x7D, cpu.registers.a); // 0x45 + 0x38 = 0x7D

    // 次にBCD調整
    cpu.bus.writeByte(0x0001, 0x27); // DAA
    cpu.step();
    assertEquals(0x83, cpu.registers.a); // BCDで45 + 38 = 83
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(false, cpu.registers.f.carry);
  }

  @Test
  @DisplayName("Test DAA after subtraction")
  public void testDAA_AfterSubtraction() throws Exception {
    CPU cpu = new CPU();
    // まず減算
    cpu.bus.writeByte(0x0000, 0x91); // SUB A, C
    cpu.registers.a = 0x45;
    cpu.registers.c = 0x16;
    cpu.step();
    assertEquals(0x2F, cpu.registers.a); // 0x45 - 0x16 = 0x2F

    // 次にBCD調整
    cpu.bus.writeByte(0x0001, 0x27); // DAA
    cpu.step();
    assertEquals(0x29, cpu.registers.a); // BCDで45 - 16 = 29
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(true, cpu.registers.f.subtract); // サブトラクトフラグは保持
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(false, cpu.registers.f.carry);
  }
}
