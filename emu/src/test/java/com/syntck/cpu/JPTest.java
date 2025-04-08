package com.syntck.cpu;

import static com.syntck.Functions.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JPTest {
  // JP命令のテスト
  @Test
  @DisplayName("Test JP nn")
  public void testJPnn() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC3); // JP nn
    cpu.bus.writeByte(0x0001, 0x34); // アドレスの下位バイト
    cpu.bus.writeByte(0x0002, 0x12); // アドレスの上位バイト
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが指定されたアドレスにジャンプ
  }
}
