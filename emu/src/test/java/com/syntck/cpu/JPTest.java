package com.syntck.cpu;

import static com.syntck.Functions.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JPTest {

  // JP命令のテスト
  @Test
  @DisplayName("Test JP BC, a16")
  public void testJP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x01); // JP BC, a16
    cpu.bus.writeByte(0x0001, 0x34); // args
    cpu.bus.writeByte(0x0002, 0x12); // args
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが0x1234に変更される
    assertTrue(compareFlagsRegister(cpu.registers.f, false, false, false, false)); // フラグレジスタの状態を確認
  }
}
