package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class CALLTest {
  // MARK: CALL (unconditional)
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

    // CALLの場合、PCはCALL命令の3バイト分進んだ後、命令内のアドレスへジャンプ
    assertEquals(0x1234, cpu.pc);

    // リターンアドレス（0x0100 + 3 = 0x0103）がスタックへプッシュされる（pushの順序に注意）
    int newSP = cpu.sp; // SPが2減少しているはず
    assertEquals((originalSP - 2) & 0xFFFF, newSP);

    int pushedLow = cpu.bus.readByte(newSP);      // push()で下位バイトを書き込んだ位置
    int pushedHigh = cpu.bus.readByte(newSP + 1);   // 次の位置に上位バイト
    int returnAddress = (pushedHigh << 8) | pushedLow;
    assertEquals(0x0103, returnAddress);
  }

  // MARK: CALL with condition true (例：CALL NZ, a16でZフラグがリセットの場合)
  @Test
  @DisplayName("Test CALL with condition true")
  public void testCALLConditionTrue() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0200;
    // 条件付きCALL命令（例：CALL NZ, a16 → 0xC4）
    cpu.bus.writeByte(0x0200, 0xC4); // CALL NZ, a16
    cpu.bus.writeByte(0x0201, 0x78); // 低位バイト
    cpu.bus.writeByte(0x0202, 0x56); // 高位バイト → ジャンプ先 0x5678
    cpu.registers.f.zero = false; // NZ条件成立
    int originalSP = cpu.sp;

    cpu.step();

    assertEquals(0x5678, cpu.pc);
    assertEquals((originalSP - 2) & 0xFFFF, cpu.sp);
    int low = cpu.bus.readByte(cpu.sp);
    int high = cpu.bus.readByte(cpu.sp + 1);
    int retAddr = (high << 8) | low;
    assertEquals(0x0200 + 3, retAddr);
  }

  // MARK: CALL with condition false (例：CALL NZ, a16でZフラグがセットの場合)
  @Test
  @DisplayName("Test CALL with condition false")
  public void testCALLConditionFalse() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x0300;
    // CALL NZ, a16
    cpu.bus.writeByte(0x0300, 0xC4); // CALL NZ, a16
    cpu.bus.writeByte(0x0301, 0x78);
    cpu.bus.writeByte(0x0302, 0x56); // ジャンプ先 0x5678
    cpu.registers.f.zero = true; // NZ条件不成立
    int originalSP = cpu.sp;

    cpu.step();

    // 条件が成立しない場合はCALLをスキップし、命令サイズ分進む
    assertEquals(0x0300 + 3, cpu.pc);
    // スタックへのpushは行われない
    assertEquals(originalSP, cpu.sp);
  }
}
