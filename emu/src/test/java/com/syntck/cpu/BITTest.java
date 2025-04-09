package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class BITTest {
  // MARK: BIT (0, B)
  @Test
  @DisplayName("Test BIT 0, B instruction with bit reset")
  public void testBIT0B_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x40); // BIT 0, B
    cpu.registers.b = 0xFE; // ビット0がリセット (0)
    cpu.step();
    assertTrue(cpu.registers.f.zero); // ビット0が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: BIT (0, B) 1
  @Test
  @DisplayName("Test BIT 0, B instruction with bit set")
  public void testBIT0B_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x40); // BIT 0, B
    cpu.registers.b = 0x01; // ビット0がセット (1)
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット0が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (1, C)
  @Test
  @DisplayName("Test BIT 1, C instruction with bit reset")
  public void testBIT1C_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x49); // BIT 1, C
    cpu.registers.c = 0xFD; // ビット1がリセット (0)
    cpu.step();
    assertTrue(cpu.registers.f.zero); // ビット1が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (2, D)
  @Test
  @DisplayName("Test BIT 2, D instruction with bit set")
  public void testBIT2D_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x52); // BIT 2, D
    cpu.registers.d = 0x04; // ビット2がセット (1)
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット2が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (3, E)
  @Test
  @DisplayName("Test BIT 3, E instruction with bit reset")
  public void testBIT3E_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x5B); // BIT 3, E
    cpu.registers.e = 0xF7; // ビット3がリセット (0)
    cpu.step();
    assertTrue(cpu.registers.f.zero); // ビット3が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (4, H)
  @Test
  @DisplayName("Test BIT 4, H instruction with bit set")
  public void testBIT4H_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x64); // BIT 4, H
    cpu.registers.h = 0x10; // ビット4がセット (1)
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット4が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (5, L)
  @Test
  @DisplayName("Test BIT 5, L instruction with bit reset")
  public void testBIT5L_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x6D); // BIT 5, L
    cpu.registers.l = 0xDF; // ビット5がリセット (0)
    cpu.step();
    assertTrue(cpu.registers.f.zero); // ビット5が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (6, A)
  @Test
  @DisplayName("Test BIT 6, A instruction with bit set")
  public void testBIT6A_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x77); // BIT 6, A
    cpu.registers.a = 0x40; // ビット6がセット (1)
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット6が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (7, (HL))
  @Test
  @DisplayName("Test BIT 7, (HL) instruction with bit reset")
  public void testBIT7HL_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x7E); // BIT 7, (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x7F); // ビット7がリセット (0)
    cpu.step();
    assertTrue(cpu.registers.f.zero); // ビット7が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (7, (HL)) 1
  @Test
  @DisplayName("Test BIT 7, (HL) instruction with bit set")
  public void testBIT7HL_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x7E); // BIT 7, (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x80); // ビット7がセット (1)
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット7が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    // キャリーフラグは維持される
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (0, A) C
  @Test
  @DisplayName("Test BIT 0, A instruction preserving carry")
  public void testBIT0A_PreserveCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x47); // BIT 0, A
    cpu.registers.a = 0x01; // ビット0がセット (1)
    cpu.registers.f.carry = true; // キャリーフラグを事前にセット
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット0が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertTrue(cpu.registers.f.carry); // キャリーフラグは変更されない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT (0, A)
  @Test
  @DisplayName("Test BIT 0, A instruction preserve other bits")
  public void testBIT0A_PreserveValue() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x47); // BIT 0, A
    cpu.registers.a = 0xA5; // ビット0がセット (1)、他のビットも設定
    cpu.step();
    assertFalse(cpu.registers.f.zero); // ビット0が1なのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertTrue(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にセット
    assertEquals(0xA5, cpu.registers.a); // レジスタの値は変更されない
    assertEquals(0x0002, cpu.pc); // PCが2増加
  }

  // MARK: BIT各ビット位置
  @Test
  @DisplayName("Test BIT instruction with all bit positions on register B")
  public void testBITAllPositions_B() throws Exception {
    CPU cpu = new CPU();
    
    // BIT 0, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x40);
    cpu.registers.b = 0x01; // ビット0がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    assertEquals(0x0002, cpu.pc);
    
    // BIT 1, B
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x48);
    cpu.registers.b = 0x02; // ビット1がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 2, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x50);
    cpu.registers.b = 0x04; // ビット2がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 3, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x58);
    cpu.registers.b = 0x08; // ビット3がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 4, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x60);
    cpu.registers.b = 0x10; // ビット4がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 5, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x68);
    cpu.registers.b = 0x20; // ビット5がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 6, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x70);
    cpu.registers.b = 0x40; // ビット6がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x78);
    cpu.registers.b = 0x80; // ビット7がセット
    cpu.step();
    assertFalse(cpu.registers.f.zero);
  }

  // MARK: BIT全レジスタ
  @Test
  @DisplayName("Test BIT 7 instruction with all registers")
  public void testBIT7_AllRegisters() throws Exception {
    CPU cpu = new CPU();
    
    // BIT 7, B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x78);
    cpu.registers.b = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    assertEquals(0x0002, cpu.pc);
    
    // BIT 7, C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x79);
    cpu.registers.c = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x7A);
    cpu.registers.d = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x7B);
    cpu.registers.e = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x7C);
    cpu.registers.h = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x7D);
    cpu.registers.l = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
    
    // BIT 7, A
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x7F);
    cpu.registers.a = 0x80;
    cpu.step();
    assertFalse(cpu.registers.f.zero);
  }

  // MARK: BIT 複数ビット
  @Test
  @DisplayName("Test BIT instruction with multiple bits set")
  public void testBIT_MultipleSet() throws Exception {
    CPU cpu = new CPU();
    // BIT 3, A (複数ビットセット)
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x5F);
    cpu.registers.a = 0xFF; // 全ビットセット
    cpu.step();
    assertFalse(cpu.registers.f.zero); // 指定ビットがセットされているのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertEquals(0x0002, cpu.pc);
  }
  
  // MARK: BIT 0ビット
  @Test
  @DisplayName("Test BIT instruction with all bits reset")
  public void testBIT_AllReset() throws Exception {
    CPU cpu = new CPU();
    // BIT 3, A (全ビットリセット)
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x5F);
    cpu.registers.a = 0x00; // 全ビットリセット
    cpu.step();
    assertTrue(cpu.registers.f.zero); // 指定ビットがリセットされているのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertEquals(0x0002, cpu.pc);
  }
}