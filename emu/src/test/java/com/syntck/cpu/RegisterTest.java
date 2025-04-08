package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RegisterTest {
  // set_hlメソッドのテスト
  @Test
  @DisplayName("Test set_hl")
  public void testSetHL() throws Exception {
    CPU cpu = new CPU();
    cpu.registers.set_hl(0x1234);
    assertEquals(0x34, cpu.registers.l); // 下位バイト
    assertEquals(0x12, cpu.registers.h); // 上位バイト
  }
  

}
