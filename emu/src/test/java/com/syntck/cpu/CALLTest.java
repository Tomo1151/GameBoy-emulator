package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CALLTest {
  // MARK: CALL (無条件)
  @Test
  @DisplayName("Test CALL unconditional instruction")
  public void testCALLUnconditional() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0100; // 初期PC
    // CALL命令（無条件）：GBでは0xCDとなる
    cpu.bus.writeByte(0x0100, 0xCD); // CALL a16
    // 次の2バイトにジャンプ先アドレス（例：0x1234）
    cpu.bus.writeByte(0x0101, 0x34); // 低位バイト
    cpu.bus.writeByte(0x0102, 0x12); // 高位バイト
    int originalSP = cpu.sp; // 例：初期値は0xFFFF

    cpu.step(); // CALL命令を実行

    // CALLの場合、PCはジャンプ先アドレスに設定される
    assertEquals(0x1234, cpu.pc);

    // リターンアドレス（0x0100 + 3 = 0x0103）がスタックへプッシュされる
    int newSP = cpu.sp; // SPが2減少しているはず
    assertEquals((originalSP - 2) & 0xFFFF, newSP);

    int pushedLow = cpu.bus.readByte(newSP);      // push()で下位バイトを書き込んだ位置
    int pushedHigh = cpu.bus.readByte(newSP + 1);   // 次の位置に上位バイト
    int returnAddress = (pushedHigh << 8) | pushedLow;
    assertEquals(0x0103, returnAddress);
  }

  // MARK: CALL NZ (NZ成立)
  @Test
  @DisplayName("Test CALL NZ, a16 when Z=0")
  public void testCALLNZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0200;
    // CALL NZ, a16 (Z=0の場合ジャンプ)
    cpu.bus.writeByte(0x0200, 0xC4); // CALL NZ, a16
    cpu.bus.writeByte(0x0201, 0x78); // 低位バイト
    cpu.bus.writeByte(0x0202, 0x56); // 高位バイト → ジャンプ先 0x5678
    cpu.registers.f.zero = false; // Z=0（NZ条件成立）
    int originalSP = cpu.sp;

    cpu.step();

    assertEquals(0x5678, cpu.pc); // 条件成立なのでジャンプ
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp); // SPが2減少
    int low = cpu.bus.readByte(cpu.sp);
    int high = cpu.bus.readByte(cpu.sp + 1);
    int retAddr = (high << 8) | low;
    assertEquals(0x0203, retAddr); // リターンアドレスは0x0200 + 3
  }

  // MARK: CALL NZ (NZ不成立)
  @Test
  @DisplayName("Test CALL NZ, a16 when Z=1")
  public void testCALLNZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0300;
    // CALL NZ, a16 (Z=0の場合ジャンプ)
    cpu.bus.writeByte(0x0300, 0xC4); // CALL NZ, a16
    cpu.bus.writeByte(0x0301, 0x78); 
    cpu.bus.writeByte(0x0302, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.zero = true; // Z=1（NZ条件不成立）
    int originalSP = cpu.sp;

    cpu.step();

    // 条件不成立なのでジャンプせず、命令後のアドレスに進む
    assertEquals(0x0303, cpu.pc);
    // スタックへのプッシュは行われない
    assertEquals(originalSP, cpu.sp);
  }

  // MARK: CALL Z (Z成立)
  @Test
  @DisplayName("Test CALL Z, a16 when Z=1")
  public void testCALLZWhenZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0400;
    // CALL Z, a16 (Z=1の場合ジャンプ)
    cpu.bus.writeByte(0x0400, 0xCC); // CALL Z, a16
    cpu.bus.writeByte(0x0401, 0x78);
    cpu.bus.writeByte(0x0402, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.zero = true; // Z=1（Z条件成立）
    int originalSP = cpu.sp;

    cpu.step();

    assertEquals(0x5678, cpu.pc); // 条件成立なのでジャンプ
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp); // SPが2減少
    int low = cpu.bus.readByte(cpu.sp);
    int high = cpu.bus.readByte(cpu.sp + 1);
    int retAddr = (high << 8) | low;
    assertEquals(0x0403, retAddr); // リターンアドレスは0x0400 + 3
  }

  // MARK: CALL Z (Z不成立)
  @Test
  @DisplayName("Test CALL Z, a16 when Z=0")
  public void testCALLZWhenNotZero() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0500;
    // CALL Z, a16 (Z=1の場合ジャンプ)
    cpu.bus.writeByte(0x0500, 0xCC); // CALL Z, a16
    cpu.bus.writeByte(0x0501, 0x78);
    cpu.bus.writeByte(0x0502, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.zero = false; // Z=0（Z条件不成立）
    int originalSP = cpu.sp;

    cpu.step();

    // 条件不成立なのでジャンプせず、命令後のアドレスに進む
    assertEquals(0x0503, cpu.pc);
    // スタックへのプッシュは行われない
    assertEquals(originalSP, cpu.sp);
  }

  // MARK: CALL NC (NC成立)
  @Test
  @DisplayName("Test CALL NC, a16 when C=0")
  public void testCALLNCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0600;
    // CALL NC, a16 (C=0の場合ジャンプ)
    cpu.bus.writeByte(0x0600, 0xD4); // CALL NC, a16
    cpu.bus.writeByte(0x0601, 0x78);
    cpu.bus.writeByte(0x0602, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.carry = false; // C=0（NC条件成立）
    int originalSP = cpu.sp;

    cpu.step();

    assertEquals(0x5678, cpu.pc); // 条件成立なのでジャンプ
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp); // SPが2減少
    int low = cpu.bus.readByte(cpu.sp);
    int high = cpu.bus.readByte(cpu.sp + 1);
    int retAddr = (high << 8) | low;
    assertEquals(0x0603, retAddr); // リターンアドレスは0x0600 + 3
  }

  // MARK: CALL NC (NC不成立)
  @Test
  @DisplayName("Test CALL NC, a16 when C=1")
  public void testCALLNCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0700;
    // CALL NC, a16 (C=0の場合ジャンプ)
    cpu.bus.writeByte(0x0700, 0xD4); // CALL NC, a16
    cpu.bus.writeByte(0x0701, 0x78);
    cpu.bus.writeByte(0x0702, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.carry = true; // C=1（NC条件不成立）
    int originalSP = cpu.sp;

    cpu.step();

    // 条件不成立なのでジャンプせず、命令後のアドレスに進む
    assertEquals(0x0703, cpu.pc);
    // スタックへのプッシュは行われない
    assertEquals(originalSP, cpu.sp);
  }

  // MARK: CALL C (C成立)
  @Test
  @DisplayName("Test CALL C, a16 when C=1")
  public void testCALLCWhenCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0800;
    // CALL C, a16 (C=1の場合ジャンプ)
    cpu.bus.writeByte(0x0800, 0xDC); // CALL C, a16
    cpu.bus.writeByte(0x0801, 0x78);
    cpu.bus.writeByte(0x0802, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.carry = true; // C=1（C条件成立）
    int originalSP = cpu.sp;

    cpu.step();

    assertEquals(0x5678, cpu.pc); // 条件成立なのでジャンプ
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp); // SPが2減少
    int low = cpu.bus.readByte(cpu.sp);
    int high = cpu.bus.readByte(cpu.sp + 1);
    int retAddr = (high << 8) | low;
    assertEquals(0x0803, retAddr); // リターンアドレスは0x0800 + 3
  }

  // MARK: CALL C (C不成立)
  @Test
  @DisplayName("Test CALL C, a16 when C=0")
  public void testCALLCWhenNotCarry() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0900;
    // CALL C, a16 (C=1の場合ジャンプ)
    cpu.bus.writeByte(0x0900, 0xDC); // CALL C, a16
    cpu.bus.writeByte(0x0901, 0x78);
    cpu.bus.writeByte(0x0902, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.carry = false; // C=0（C条件不成立）
    int originalSP = cpu.sp;

    cpu.step();

    // 条件不成立なのでジャンプせず、命令後のアドレスに進む
    assertEquals(0x0903, cpu.pc);
    // スタックへのプッシュは行われない
    assertEquals(originalSP, cpu.sp);
  }

  // MARK: CALL スタック境界
  @Test
  @DisplayName("Test CALL at stack boundary")
  public void testCALLAtStackBoundary() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0A00;
    cpu.sp = 0x0002; // スタックポインタを境界付近に設定
    // CALL命令
    cpu.bus.writeByte(0x0A00, 0xCD); // CALL a16
    cpu.bus.writeByte(0x0A01, 0x78);
    cpu.bus.writeByte(0x0A02, 0x56); // ジャンプ先 0x5678
    
    cpu.step();

    assertEquals(0x5678, cpu.pc); // ジャンプ先に移動
    assertEquals(0x0000, cpu.sp); // SP=0x0002-2=0x0000（オーバーフロー/アンダーフローなし）
    
    // リターンアドレスの確認
    int low = cpu.bus.readByte(0x0000);
    int high = cpu.bus.readByte(0x0001);
    int retAddr = (high << 8) | low;
    assertEquals(0x0A03, retAddr);
  }

  // MARK: CALL 連続呼び出し
  @Test
  @DisplayName("Test consecutive CALL instructions")
  public void testConsecutiveCALLs() throws Exception {
    CPU cpu = new CPU();
    // 最初のCALL命令の設定
    cpu.pc = 0x1000;
    cpu.bus.writeByte(0x1000, 0xCD); // CALL a16
    cpu.bus.writeByte(0x1001, 0x50);
    cpu.bus.writeByte(0x1002, 0x20); // ジャンプ先 0x2050
    
    // ジャンプ先に別のCALL命令を設定
    cpu.bus.writeByte(0x2050, 0xCD); // CALL a16
    cpu.bus.writeByte(0x2051, 0x70);
    cpu.bus.writeByte(0x2052, 0x30); // ジャンプ先 0x3070
    
    int originalSP = cpu.sp;
    
    // 最初のCALL実行
    cpu.step();
    assertEquals(0x2050, cpu.pc);
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    
    // スタックの内容を確認
    int low1 = cpu.bus.readByte(cpu.sp);
    int high1 = cpu.bus.readByte(cpu.sp + 1);
    int retAddr1 = (high1 << 8) | low1;
    assertEquals(0x1003, retAddr1); // 最初のリターンアドレス
    
    // 2番目のCALL実行
    cpu.step();
    assertEquals(0x3070, cpu.pc);
    assertEquals((originalSP - 4) & 0xFFFF, cpu.sp);
    
    // スタックの内容を確認（新しいリターンアドレス）
    int low2 = cpu.bus.readByte(cpu.sp);
    int high2 = cpu.bus.readByte(cpu.sp + 1);
    int retAddr2 = (high2 << 8) | low2;
    assertEquals(0x2053, retAddr2); // 2番目のリターンアドレス
    
    // 元のリターンアドレスも確認
    int low1Check = cpu.bus.readByte(cpu.sp + 2);
    int high1Check = cpu.bus.readByte(cpu.sp + 3);
    int retAddr1Check = (high1Check << 8) | low1Check;
    assertEquals(0x1003, retAddr1Check); // 最初のリターンアドレスはまだスタック上にある
  }
}
