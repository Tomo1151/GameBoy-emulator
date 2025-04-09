package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RESTest {
  // MARK: RES (0, B)
  @Test
  @DisplayName("Test RES 0, B instruction")
  public void testRES0B() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x80); // RES 0, B
    cpu.registers.b = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xFE, cpu.registers.b); // ビット0がリセットされる (0xFE = 1111 1110)
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: RES (1, C)
  @Test
  @DisplayName("Test RES 1, C instruction")
  public void testRES1C() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x89); // RES 1, C
    cpu.registers.c = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xFD, cpu.registers.c); // ビット1がリセットされる (0xFD = 1111 1101)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (2, D)
  @Test
  @DisplayName("Test RES 2, D instruction")
  public void testRES2D() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x92); // RES 2, D
    cpu.registers.d = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xFB, cpu.registers.d); // ビット2がリセットされる (0xFB = 1111 1011)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (3, E)
  @Test
  @DisplayName("Test RES 3, E instruction")
  public void testRES3E() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x9B); // RES 3, E
    cpu.registers.e = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xF7, cpu.registers.e); // ビット3がリセットされる (0xF7 = 1111 0111)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (4, H)
  @Test
  @DisplayName("Test RES 4, H instruction")
  public void testRES4H() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xA4); // RES 4, H
    cpu.registers.h = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xEF, cpu.registers.h); // ビット4がリセットされる (0xEF = 1110 1111)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (5, L)
  @Test
  @DisplayName("Test RES 5, L instruction")
  public void testRES5L() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xAD); // RES 5, L
    cpu.registers.l = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xDF, cpu.registers.l); // ビット5がリセットされる (0xDF = 1101 1111)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (6, A)
  @Test
  @DisplayName("Test RES 6, A instruction")
  public void testRES6A() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xB7); // RES 6, A
    cpu.registers.a = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xBF, cpu.registers.a); // ビット6がリセットされる (0xBF = 1011 1111)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (7, (HL))
  @Test
  @DisplayName("Test RES 7, (HL) instruction")
  public void testRES7HL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xBE); // RES 7, (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0xFF); // メモリ位置C000に0xFFを書き込む
    cpu.step();
    assertEquals(0x7F, cpu.bus.readByte(0xC000)); // ビット7がリセットされる (0x7F = 0111 1111)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (0, B) 0
  @Test
  @DisplayName("Test RES 0, B when bit is already reset")
  public void testRES0B_AlreadyReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x80); // RES 0, B
    cpu.registers.b = 0xFE; // ビット0はすでに0
    cpu.step();
    assertEquals(0xFE, cpu.registers.b); // 値は変わらない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (3, C) 0
  @Test
  @DisplayName("Test RES 3, C when bit is already reset")
  public void testRES3C_AlreadyReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x99); // RES 3, C
    cpu.registers.c = 0xF7; // ビット3はすでに0
    cpu.step();
    assertEquals(0xF7, cpu.registers.c); // 値は変わらない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (7, A) 01
  @Test
  @DisplayName("Test RES 7, A with mixed bits")
  public void testRES7A_MixedBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xBF); // RES 7, A
    cpu.registers.a = 0xA5; // 1010 0101 - 複数のビットが混在
    cpu.step();
    assertEquals(0x25, cpu.registers.a); // ビット7がリセットされる (0x25 = 0010 0101)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: RES (複数ビット)
  @Test
  @DisplayName("Test multiple RES operations on same register")
  public void testMultipleRES() throws Exception {
    CPU cpu = new CPU();
    
    // 最初のRES命令: RES 0, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x80);
    cpu.registers.b = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xFE, cpu.registers.b); // ビット0がリセットされる
    assertEquals(0x0002, cpu.pc);
    
    // 2番目のRES命令: RES 1, B
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x88);
    cpu.registers.b = 0xFE; // 前の操作の結果
    cpu.step();
    assertEquals(0xFC, cpu.registers.b); // ビット1もリセットされる (0xFC = 1111 1100)
    
    // 3番目のRES命令: RES 4, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xA0);
    cpu.registers.b = 0xFC; // 前の操作の結果
    cpu.step();
    assertEquals(0xEC, cpu.registers.b); // ビット4もリセットされる (0xEC = 1110 1100)
  }

  // MARK: RES (すべてのビット)
  @Test
  @DisplayName("Test resetting all bits with RES instructions")
  public void testResetAllBits() throws Exception {
    CPU cpu = new CPU();
    cpu.registers.a = 0xFF; // すべてのビットが1
    
    // すべてのビットをリセット
    for (int bit = 0; bit < 8; bit++) {
      cpu.pc = 0x0000;
      cpu.bus.writeByte(0x0000, 0xCB);
      cpu.bus.writeByte(0x0001, 0x87 | (bit << 3)); // RES bit, A命令を生成
      cpu.step();
    }
    
    assertEquals(0x00, cpu.registers.a); // すべてのビットがリセットされる
  }

  // MARK: RES (特定のビットのみ)
  @Test
  @DisplayName("Test RES affecting only specified bit")
  public void testRESOnlyAffectsSpecifiedBit() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xA1); // RES 4, C
    cpu.registers.c = 0xFF; // すべてのビットが1
    cpu.step();
    assertEquals(0xEF, cpu.registers.c); // ビット4のみがリセットされる (0xEF = 1110 1111)
    // 他のレジスタは変更されないことを確認
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x00, cpu.registers.b);
    assertEquals(0x00, cpu.registers.d);
    assertEquals(0x00, cpu.registers.e);
    assertEquals(0x00, cpu.registers.h);
    assertEquals(0x00, cpu.registers.l);
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }
}