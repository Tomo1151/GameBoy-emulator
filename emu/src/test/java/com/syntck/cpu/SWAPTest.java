package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SWAPTest {
  // MARK: SWAP (A)
  @Test
  @DisplayName("Test SWAP A instruction")
  public void testSWAPA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB); // CB prefix
    cpu.bus.writeByte(0x0001, 0x37); // SWAP A
    cpu.registers.a = 0x12; // 0001 0010
    cpu.step();
    assertEquals(0x21, cpu.registers.a); // スワップ後: 0010 0001
    assertFalse(cpu.registers.f.zero); // 結果が0でないのでゼロフラグはリセット
    assertFalse(cpu.registers.f.subtract); // サブトラクトフラグは常にリセット
    assertFalse(cpu.registers.f.halfCarry); // ハーフキャリーフラグは常にリセット
    assertFalse(cpu.registers.f.carry); // キャリーフラグは常にリセット
    assertEquals(0x0002, cpu.pc); // PCが2増加（CBプレフィックスのため）
  }

  // MARK: SWAP (B)
  @Test
  @DisplayName("Test SWAP B instruction")
  public void testSWAPB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x30); // SWAP B
    cpu.registers.b = 0xAB; // 1010 1011
    cpu.step();
    assertEquals(0xBA, cpu.registers.b); // スワップ後: 1011 1010
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (C)
  @Test
  @DisplayName("Test SWAP C instruction")
  public void testSWAPC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x31); // SWAP C
    cpu.registers.c = 0xF0; // 1111 0000
    cpu.step();
    assertEquals(0x0F, cpu.registers.c); // スワップ後: 0000 1111
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (D)
  @Test
  @DisplayName("Test SWAP D instruction")
  public void testSWAPD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x32); // SWAP D
    cpu.registers.d = 0x0F; // 0000 1111
    cpu.step();
    assertEquals(0xF0, cpu.registers.d); // スワップ後: 1111 0000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (E)
  @Test
  @DisplayName("Test SWAP E instruction")
  public void testSWAPE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x33); // SWAP E
    cpu.registers.e = 0x35; // 0011 0101
    cpu.step();
    assertEquals(0x53, cpu.registers.e); // スワップ後: 0101 0011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (H)
  @Test
  @DisplayName("Test SWAP H instruction")
  public void testSWAPH() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x34); // SWAP H
    cpu.registers.h = 0x72; // 0111 0010
    cpu.step();
    assertEquals(0x27, cpu.registers.h); // スワップ後: 0010 0111
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (L)
  @Test
  @DisplayName("Test SWAP L instruction")
  public void testSWAPL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x35); // SWAP L
    cpu.registers.l = 0x19; // 0001 1001
    cpu.step();
    assertEquals(0x91, cpu.registers.l); // スワップ後: 1001 0001
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP ((HL))
  @Test
  @DisplayName("Test SWAP (HL) instruction")
  public void testSWAPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x36); // SWAP (HL)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x5A); // メモリ位置C000に0x5A (0101 1010)を書き込む
    cpu.step();
    assertEquals(0xA5, cpu.bus.readByte(0xC000)); // スワップ後: 1010 0101
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (A) → 0
  @Test
  @DisplayName("Test SWAP A resulting in zero")
  public void testSWAPA_Zero() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x37); // SWAP A
    cpu.registers.a = 0x00; // すべてのビットが0
    cpu.step();
    assertEquals(0x00, cpu.registers.a); // スワップ後も0
    assertTrue(cpu.registers.f.zero); // 結果が0なのでゼロフラグはセット
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (B) 上位のみ
  @Test
  @DisplayName("Test SWAP B with upper nibble only")
  public void testSWAPB_UpperNibbleOnly() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x30); // SWAP B
    cpu.registers.b = 0x40; // 0100 0000 - 上位4ビットのみ値あり
    cpu.step();
    assertEquals(0x04, cpu.registers.b); // スワップ後: 0000 0100
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (C) 下位のみ
  @Test
  @DisplayName("Test SWAP C with lower nibble only")
  public void testSWAPC_LowerNibbleOnly() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x31); // SWAP C
    cpu.registers.c = 0x08; // 0000 1000 - 下位4ビットのみ値あり
    cpu.step();
    assertEquals(0x80, cpu.registers.c); // スワップ後: 1000 0000
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (D) フラグ保存確認
  @Test
  @DisplayName("Test SWAP D preserves original flags")
  public void testSWAPD_PreserveFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x32); // SWAP D
    cpu.registers.d = 0x67; // 0110 0111
    
    // 事前にフラグをセット
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    assertEquals(0x76, cpu.registers.d); // スワップ後: 0111 0110
    assertFalse(cpu.registers.f.zero); // リセットされる (結果は0ではない)
    assertFalse(cpu.registers.f.subtract); // リセットされる (SWAP命令ではサブトラクトは常に0)
    assertFalse(cpu.registers.f.halfCarry); // リセットされる (SWAP命令ではハーフキャリーは常に0)
    assertFalse(cpu.registers.f.carry); // リセットされる (SWAP命令ではキャリーは常に0)
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (E) 上下同値
  @Test
  @DisplayName("Test SWAP E with same upper and lower nibbles")
  public void testSWAPE_SameNibbles() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x33); // SWAP E
    cpu.registers.e = 0x33; // 0011 0011 - 上位と下位が同じ
    cpu.step();
    assertEquals(0x33, cpu.registers.e); // スワップ後も同じ: 0011 0011
    assertFalse(cpu.registers.f.zero);
    assertFalse(cpu.registers.f.subtract);
    assertFalse(cpu.registers.f.halfCarry);
    assertFalse(cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (複数レジスタ連続)
  @Test
  @DisplayName("Test multiple consecutive SWAP operations")
  public void testMultipleSWAP() throws Exception {
    CPU cpu = new CPU();
    
    // 最初のSWAP命令: SWAP B
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x30); // SWAP B
    cpu.registers.b = 0x12; // 0001 0010
    cpu.step();
    assertEquals(0x21, cpu.registers.b); // スワップ後: 0010 0001
    assertEquals(0x0002, cpu.pc);
    
    // 2番目のSWAP命令: SWAP B
    cpu.pc = 0x0000; // PCをリセット
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x30); // SWAP B
    cpu.registers.b = 0x21; // 前の操作の結果
    cpu.step();
    assertEquals(0x12, cpu.registers.b); // 元に戻る: 0001 0010
    assertFalse(cpu.registers.f.zero);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (全レジスタ同一値)
  @Test
  @DisplayName("Test SWAP on all registers with the same value")
  public void testSWAPAllRegisters() throws Exception {
    CPU cpu = new CPU();
    final int testValue = 0x3C; // 0011 1100
    final int swappedValue = 0xC3; // 1100 0011
    
    // SWAP A
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x37); // SWAP A
    cpu.registers.a = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.a);
    assertEquals(0x0002, cpu.pc);
    
    // SWAP B
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x30); // SWAP B
    cpu.registers.b = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.b);
    
    // SWAP C
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x31); // SWAP C
    cpu.registers.c = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.c);
    
    // SWAP D
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x32); // SWAP D
    cpu.registers.d = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.d);
    
    // SWAP E
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x33); // SWAP E
    cpu.registers.e = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.e);
    
    // SWAP H
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x34); // SWAP H
    cpu.registers.h = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.h);
    
    // SWAP L
    cpu.pc = 0x0000;
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x35); // SWAP L
    cpu.registers.l = testValue;
    cpu.step();
    assertEquals(swappedValue, cpu.registers.l);
  }

  // MARK: SWAP ((HL)) メモリ
  @Test
  @DisplayName("Test SWAP (HL) memory operation")
  public void testSWAPHL_Memory() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x36); // SWAP (HL)
    cpu.registers.set_hl(0xC000);
    
    // メモリの複数の場所に値をセット (関連しない場所は変更されないことを確認)
    cpu.bus.writeByte(0xBFFF, 0x11);
    cpu.bus.writeByte(0xC000, 0x1F); // 対象の値: 0001 1111
    cpu.bus.writeByte(0xC001, 0x22);
    
    cpu.step();
    
    assertEquals(0xF1, cpu.bus.readByte(0xC000)); // スワップ後: 1111 0001
    assertEquals(0x11, cpu.bus.readByte(0xBFFF)); // 変更されていない
    assertEquals(0x22, cpu.bus.readByte(0xC001)); // 変更されていない
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: SWAP (A) 2バイト使用確認
  @Test
  @DisplayName("Test SWAP instruction uses 2 bytes")
  public void testSWAP_TwoBytesUsage() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCB);
    cpu.bus.writeByte(0x0001, 0x37); // SWAP A
    cpu.bus.writeByte(0x0002, 0x00); // NOP (次の命令)
    cpu.registers.a = 0x45;
    cpu.step();
    assertEquals(0x54, cpu.registers.a);
    assertEquals(0x0002, cpu.pc); // PCが2バイト進む
    
    // 次のステップでNOPが実行される
    cpu.step();
    assertEquals(0x0003, cpu.pc); // さらに1バイト進む (NOP命令)
  }
}