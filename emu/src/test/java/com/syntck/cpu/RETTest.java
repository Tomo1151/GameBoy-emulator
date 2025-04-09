package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RETTest {
  // MARK: RET (無条件)
  @Test
  @DisplayName("Test RET unconditional instruction")
  public void testRETUnconditional() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0100; // 初期PC
    cpu.bus.writeByte(0x0100, 0xC9); // RET命令
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x1234); // リターンアドレス0x1234をスタックに積む
    
    // RET命令を実行
    cpu.step();
    
    // PCがスタックからポップしたアドレスに変更されているか
    assertEquals(0x1234, cpu.pc);
    
    // スタックポインタが元の位置に戻っているか
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET NZ (NZ成立)
  @Test
  @DisplayName("Test RET NZ when Z=0")
  public void testRETNZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0200;
    cpu.bus.writeByte(0x0200, 0xC0); // RET NZ
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x3456);
    
    // Zフラグをリセット（NZ条件成立）
    cpu.registers.f.zero = false;
    
    cpu.step();
    
    // 条件が成立したので、PCがスタックからポップしたアドレスに変更される
    assertEquals(0x3456, cpu.pc);
    
    // スタックポインタが元の位置に戻る
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET NZ (NZ不成立)
  @Test
  @DisplayName("Test RET NZ when Z=1")
  public void testRETNZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0200;
    cpu.bus.writeByte(0x0200, 0xC0); // RET NZ
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x3456);
    
    // Zフラグをセット（NZ条件不成立）
    cpu.registers.f.zero = true;
    
    cpu.step();
    
    // 条件が不成立なので、PCは次の命令（PC+1）に進む
    assertEquals(0x0201, cpu.pc);
    
    // スタックポインタは変化しない
    assertEquals(originalSP - 2, cpu.sp);
  }
  
  // MARK: RET Z (Z成立)
  @Test
  @DisplayName("Test RET Z when Z=1")
  public void testRETZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0300;
    cpu.bus.writeByte(0x0300, 0xC8); // RET Z
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x4567);
    
    // Zフラグをセット（Z条件成立）
    cpu.registers.f.zero = true;
    
    cpu.step();
    
    // 条件が成立したので、PCがスタックからポップしたアドレスに変更される
    assertEquals(0x4567, cpu.pc);
    
    // スタックポインタが元の位置に戻る
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET Z (Z不成立)
  @Test
  @DisplayName("Test RET Z when Z=0")
  public void testRETZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0300;
    cpu.bus.writeByte(0x0300, 0xC8); // RET Z
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x4567);
    
    // Zフラグをリセット（Z条件不成立）
    cpu.registers.f.zero = false;
    
    cpu.step();
    
    // 条件が不成立なので、PCは次の命令（PC+1）に進む
    assertEquals(0x0301, cpu.pc);
    
    // スタックポインタは変化しない
    assertEquals(originalSP - 2, cpu.sp);
  }
  
  // MARK: RET NC (NC成立)
  @Test
  @DisplayName("Test RET NC when C=0")
  public void testRETNCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0400;
    cpu.bus.writeByte(0x0400, 0xD0); // RET NC
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x5678);
    
    // Cフラグをリセット（NC条件成立）
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    // 条件が成立したので、PCがスタックからポップしたアドレスに変更される
    assertEquals(0x5678, cpu.pc);
    
    // スタックポインタが元の位置に戻る
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET NC (NC不成立)
  @Test
  @DisplayName("Test RET NC when C=1")
  public void testRETNCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0400;
    cpu.bus.writeByte(0x0400, 0xD0); // RET NC
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x5678);
    
    // Cフラグをセット（NC条件不成立）
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // 条件が不成立なので、PCは次の命令（PC+1）に進む
    assertEquals(0x0401, cpu.pc);
    
    // スタックポインタは変化しない
    assertEquals(originalSP - 2, cpu.sp);
  }
  
  // MARK: RET C (C成立)
  @Test
  @DisplayName("Test RET C when C=1")
  public void testRETCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0500;
    cpu.bus.writeByte(0x0500, 0xD8); // RET C
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x6789);
    
    // Cフラグをセット（C条件成立）
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // 条件が成立したので、PCがスタックからポップしたアドレスに変更される
    assertEquals(0x6789, cpu.pc);
    
    // スタックポインタが元の位置に戻る
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET C (C不成立)
  @Test
  @DisplayName("Test RET C when C=0")
  public void testRETCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0500;
    cpu.bus.writeByte(0x0500, 0xD8); // RET C
    
    // スタックにリターン先アドレスをプッシュ
    int originalSP = cpu.sp;
    cpu.push(0x6789);
    
    // Cフラグをリセット（C条件不成立）
    cpu.registers.f.carry = false;
    
    cpu.step();
    
    // 条件が不成立なので、PCは次の命令（PC+1）に進む
    assertEquals(0x0501, cpu.pc);
    
    // スタックポインタは変化しない
    assertEquals(originalSP - 2, cpu.sp);
  }
  
  // MARK: RET スタック境界
  @Test
  @DisplayName("Test RET at stack boundary")
  public void testRETAtStackBoundary() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0700;
    cpu.bus.writeByte(0x0700, 0xC9); // RET
    
    // スタックポインタを設定
    cpu.sp = 0xFFFD;
    
    // スタックに値を設定（直接書き込み）
    cpu.bus.writeByte(0xFFFD, 0xAB); // 下位バイト
    cpu.bus.writeByte(0xFFFE, 0xCD); // 上位バイト
    
    cpu.step();
    
    // PCがスタックからポップしたアドレス0xCDABに変更される
    assertEquals(0xCDAB, cpu.pc);
    
    // スタックポインタがラップアラウンドする
    assertEquals(0xFFFF, cpu.sp);
  }
  
  // MARK: RET フラグ変化なし
  @Test
  @DisplayName("Test RET doesn't modify flags")
  public void testRETDoesNotModifyFlags() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0800;
    cpu.bus.writeByte(0x0800, 0xC9); // RET
    
    // スタックにリターン先アドレスをプッシュ
    cpu.push(0x89AB);
    
    // フラグを設定
    cpu.registers.f.zero = true;
    cpu.registers.f.subtract = true;
    cpu.registers.f.halfCarry = true;
    cpu.registers.f.carry = true;
    
    cpu.step();
    
    // PCがスタックからポップしたアドレスに変更される
    assertEquals(0x89AB, cpu.pc);
    
    // フラグは変更されない
    assertTrue(cpu.registers.f.zero);
    assertTrue(cpu.registers.f.subtract);
    assertTrue(cpu.registers.f.halfCarry);
    assertTrue(cpu.registers.f.carry);
  }
  
  // MARK: RET 連続実行
  @Test
  @DisplayName("Test consecutive RET instructions")
  public void testConsecutiveRET() throws Exception {
    CPU cpu = new CPU();
    
    // 1つ目のRET命令
    cpu.pc = 0x1000;
    cpu.bus.writeByte(0x1000, 0xC9); // RET
    
    // 2つ目のRET命令
    cpu.bus.writeByte(0x2000, 0xC9); // リターン先アドレスにもRETを配置
    
    // スタックに2つのリターンアドレスをプッシュ（逆順）
    int originalSP = cpu.sp;
    cpu.push(0x3000); // 2回目のリターン先
    cpu.push(0x2000); // 1回目のリターン先
    
    // 1回目のRET実行
    cpu.step();
    assertEquals(0x2000, cpu.pc);
    assertEquals(originalSP - 2, cpu.sp); // スタックから1つポップされた
    
    // 2回目のRET実行
    cpu.step();
    assertEquals(0x3000, cpu.pc);
    assertEquals(originalSP, cpu.sp); // スタックがすべてポップされ元に戻った
  }
}
