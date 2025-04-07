package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class BitOperationTest {

  // BIT命令のテスト
  @Test
  @DisplayName("Test BIT 7, A")
  public void testBit7A() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0x7F); // BIT 7, A
    cpu.registers.a = 0x80;
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // 値は変わらない
    assertEquals(false, cpu.registers.f.zero); // ビット7は0ではない
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(true, cpu.registers.f.halfCarry);
    assertEquals(0x0002, cpu.pc);
  }

  @Test
  @DisplayName("Test BIT 0, B with zero")
  public void testBit0B_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0x40); // BIT 0, B
    cpu.registers.b = 0xFE;
    cpu.step();
    assertEquals(0xFE, cpu.registers.b); // 値は変わらない
    assertEquals(true, cpu.registers.f.zero); // ビット0は0
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(true, cpu.registers.f.halfCarry);
    assertEquals(0x0002, cpu.pc);
  }

  // SET命令のテスト
  @Test
  @DisplayName("Test SET 3, C")
  public void testSet3C() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0xD9); // SET 3, C
    cpu.registers.c = 0x00;
    cpu.step();
    assertEquals(0x08, cpu.registers.c); // ビット3をセット
    assertEquals(0x0002, cpu.pc);
  }

  // RES命令のテスト
  @Test
  @DisplayName("Test RES 5, D")
  public void testRes5D() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0xAA); // RES 5, D
    cpu.registers.d = 0xFF;
    cpu.step();
    assertEquals(0xDF, cpu.registers.d); // ビット5をリセット
    assertEquals(0x0002, cpu.pc);
  }

  // SWAP命令のテスト
  @Test
  @DisplayName("Test SWAP E")
  public void testSwapE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0x33); // SWAP E
    cpu.registers.e = 0xF0;
    cpu.step();
    assertEquals(0x0F, cpu.registers.e); // 上位4ビットと下位4ビットを入れ替え
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(false, cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // SRL命令のテスト
  @Test
  @DisplayName("Test SRL H")
  public void testSrlH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // プレフィックス
    cpu.bus.writeByte(0x0001, 0x3C); // SRL H
    cpu.registers.h = 0x01;
    cpu.step();
    assertEquals(0x00, cpu.registers.h); // 1ビット右シフト、MSBは0でフィル
    assertEquals(true, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(true, cpu.registers.f.carry); // LSBがキャリーにセット
    assertEquals(0x0002, cpu.pc);
  }
}
