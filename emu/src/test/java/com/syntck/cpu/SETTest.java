package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SETTest {
  // MARK: SET (0, B)
  @Test
  @DisplayName("Test SET 0, B instruction")
  public void testSET0B() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xC0); // SET 0, B
    cpu.registers.b = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x01, cpu.registers.b); // ビット0が1になる (0x01 = 0000 0001)
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: SET (1, C)
  @Test
  @DisplayName("Test SET 1, C instruction")
  public void testSET1C() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xC9); // SET 1, C
    cpu.registers.c = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x02, cpu.registers.c); // ビット1が1になる (0x02 = 0000 0010)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (2, D)
  @Test
  @DisplayName("Test SET 2, D instruction")
  public void testSET2D() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xD2); // SET 2, D
    cpu.registers.d = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x04, cpu.registers.d); // ビット2が1になる (0x04 = 0000 0100)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (3, E)
  @Test
  @DisplayName("Test SET 3, E instruction")
  public void testSET3E() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xDB); // SET 3, E
    cpu.registers.e = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x08, cpu.registers.e); // ビット3が1になる (0x08 = 0000 1000)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (4, H)
  @Test
  @DisplayName("Test SET 4, H instruction")
  public void testSET4H() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xE4); // SET 4, H
    cpu.registers.h = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x10, cpu.registers.h); // ビット4が1になる (0x10 = 0001 0000)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (5, L)
  @Test
  @DisplayName("Test SET 5, L instruction")
  public void testSET5L() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xED); // SET 5, L
    cpu.registers.l = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x20, cpu.registers.l); // ビット5が1になる (0x20 = 0010 0000)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (6, A)
  @Test
  @DisplayName("Test SET 6, A instruction")
  public void testSET6A() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xF7); // SET 6, A
    cpu.registers.a = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x40, cpu.registers.a); // ビット6が1になる (0x40 = 0100 0000)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (7, (HL))
  @Test
  @DisplayName("Test SET 7, (HL) instruction")
  public void testSET7HL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xFE); // SET 7, (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x00); // メモリ位置C000に0x00を書き込む
    cpu.step();
    assertEquals(0x80, cpu.bus.readByte(0xC000)); // ビット7が1になる (0x80 = 1000 0000)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (0, B) 1
  @Test
  @DisplayName("Test SET 0, B when bit is already set")
  public void testSET0B_AlreadySet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xC0); // SET 0, B
    cpu.registers.b = 0x01; // ビット0はすでに1
    cpu.step();
    assertEquals(0x01, cpu.registers.b); // 値は変わらない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (3, C) 1
  @Test
  @DisplayName("Test SET 3, C preserving other bits")
  public void testSET3C_PreserveOtherBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xD9); // SET 3, C
    cpu.registers.c = 0x45; // 0100 0101 - 他のビットが混在
    cpu.step();
    assertEquals(0x4D, cpu.registers.c); // ビット3が1になる (0x4D = 0100 1101)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (7, A) 01
  @Test
  @DisplayName("Test SET 7, A with mixed bits")
  public void testSET7A_MixedBits() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xFF); // SET 7, A
    cpu.registers.a = 0x2A; // 0010 1010 - 複数のビットが混在
    cpu.step();
    assertEquals(0xAA, cpu.registers.a); // ビット7が1になる (0xAA = 1010 1010)
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (複数ビット)
  @Test
  @DisplayName("Test multiple SET operations on same register")
  public void testMultipleSET() throws Exception {
    CPU cpu = new CPU();
    
    // 最初のSET命令: SET 0, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xC0);
    cpu.registers.b = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x01, cpu.registers.b); // ビット0が1になる
    assertEquals(0x0002, cpu.pc);
    
    // 2番目のSET命令: SET 1, B
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xC8);
    cpu.registers.b = 0x01; // 前の操作の結果
    cpu.step();
    assertEquals(0x03, cpu.registers.b); // ビット1も1になる (0x03 = 0000 0011)
    
    // 3番目のSET命令: SET 4, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xE0);
    cpu.registers.b = 0x03; // 前の操作の結果
    cpu.step();
    assertEquals(0x13, cpu.registers.b); // ビット4も1になる (0x13 = 0001 0011)
  }

  // MARK: SET (すべてのビット)
  @Test
  @DisplayName("Test setting all bits with SET instructions")
  public void testSetAllBits() throws Exception {
    CPU cpu = new CPU();
    cpu.registers.a = 0x00; // すべてのビットが0
    
    // すべてのビットをセット
    for (int bit = 0; bit < 8; bit++) {
      cpu.pc = 0x0000;
      cpu.bus.writeByte(0x0000, 0xCB);
      cpu.bus.writeByte(0x0001, 0xC7 | (bit << 3)); // SET bit, A命令を生成
      cpu.step();
    }
    
    assertEquals(0xFF, cpu.registers.a); // すべてのビットが1になる
  }

  // MARK: SET各ビット位置
  @Test
  @DisplayName("Test SET instruction with all bit positions on register B")
  public void testSETAllPositions_B() throws Exception {
    CPU cpu = new CPU();
    
    // SET 0, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xC0);
    cpu.registers.b = 0x00;
    cpu.step();
    assertEquals(0x01, cpu.registers.b); // ビット0が1になる
    assertEquals(0x0002, cpu.pc);
    
    // SET 1, B
    cpu.pc = 0x0000; // PCをリセット
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xC8);
    cpu.step();
    assertEquals(0x02, cpu.registers.b); // ビット1が1になる
    
    // SET 2, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xD0);
    cpu.step();
    assertEquals(0x04, cpu.registers.b); // ビット2が1になる
    
    // SET 3, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xD8);
    cpu.step();
    assertEquals(0x08, cpu.registers.b); // ビット3が1になる
    
    // SET 4, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xE0);
    cpu.step();
    assertEquals(0x10, cpu.registers.b); // ビット4が1になる
    
    // SET 5, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xE8);
    cpu.step();
    assertEquals(0x20, cpu.registers.b); // ビット5が1になる
    
    // SET 6, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xF0);
    cpu.step();
    assertEquals(0x40, cpu.registers.b); // ビット6が1になる
    
    // SET 7, B
    cpu.pc = 0x0000;
    cpu.registers.b = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xF8);
    cpu.step();
    assertEquals(0x80, cpu.registers.b); // ビット7が1になる
  }

  // MARK: SET全レジスタ
  @Test
  @DisplayName("Test SET 7 instruction with all registers")
  public void testSET7_AllRegisters() throws Exception {
    CPU cpu = new CPU();
    
    // SET 7, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xF8);
    cpu.registers.b = 0x00;
    cpu.step();
    assertEquals(0x80, cpu.registers.b); // ビット7が1になる
    assertEquals(0x0002, cpu.pc);
    
    // SET 7, C
    cpu.pc = 0x0000;
    cpu.registers.c = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xF9);
    cpu.step();
    assertEquals(0x80, cpu.registers.c); // ビット7が1になる
    
    // SET 7, D
    cpu.pc = 0x0000;
    cpu.registers.d = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xFA);
    cpu.step();
    assertEquals(0x80, cpu.registers.d); // ビット7が1になる
    
    // SET 7, E
    cpu.pc = 0x0000;
    cpu.registers.e = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xFB);
    cpu.step();
    assertEquals(0x80, cpu.registers.e); // ビット7が1になる
    
    // SET 7, H
    cpu.pc = 0x0000;
    cpu.registers.h = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xFC);
    cpu.step();
    assertEquals(0x80, cpu.registers.h); // ビット7が1になる
    
    // SET 7, L
    cpu.pc = 0x0000;
    cpu.registers.l = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xFD);
    cpu.step();
    assertEquals(0x80, cpu.registers.l); // ビット7が1になる
    
    // SET 7, A
    cpu.pc = 0x0000;
    cpu.registers.a = 0x00;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0xFF);
    cpu.step();
    assertEquals(0x80, cpu.registers.a); // ビット7が1になる
  }

  // MARK: SET (特定のビットのみ)
  @Test
  @DisplayName("Test SET affecting only specified bit")
  public void testSETOnlyAffectsSpecifiedBit() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xE1); // SET 4, C
    cpu.registers.c = 0x0A; // 0000 1010 - 他のビットが混在
    cpu.step();
    assertEquals(0x1A, cpu.registers.c); // ビット4が1になる (0x1A = 0001 1010)、他のビットは変わらない
    // 他のレジスタは変更されないことを確認
    assertEquals(0x00, cpu.registers.a);
    assertEquals(0x00, cpu.registers.b);
    assertEquals(0x00, cpu.registers.d);
    assertEquals(0x00, cpu.registers.e);
    assertEquals(0x00, cpu.registers.h);
    assertEquals(0x00, cpu.registers.l);
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: SET (フラグの影響なし)
  @Test
  @DisplayName("Test SET instruction does not affect flags")
  public void testSETDoesNotAffectFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0xC7); // SET 0, A
    
    // フラグを事前に設定
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // フラグは変更されないことを確認
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }
}