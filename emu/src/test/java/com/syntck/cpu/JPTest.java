package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JPTest {

  // JP命令のテスト
  @Test
  @DisplayName("Test JP a16")
  public void testJP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC3); // JP a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12);
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが0x1234に変更される
  }

  @Test
  @DisplayName("Test JP Z, a16 with Z flag set")
  public void testJP_Z_Set() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12);
    cpu.registers.f.zero = true;
    cpu.step();
    assertEquals(0x1234, cpu.pc); // PCが0x1234に変更される
  }

  @Test
  @DisplayName("Test JP Z, a16 with Z flag reset")
  public void testJP_Z_Reset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12);
    cpu.registers.f.zero = false;
    cpu.step();
    assertEquals(0x0003, cpu.pc); // 条件が満たされないので、PCは次の命令へ
  }

  // JP HL命令のテスト
  @Test
  @DisplayName("Test JP HL")
  public void testJPHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE9); // JP HL
    cpu.registers.set_hl(0xABCD);
    cpu.step();
    assertEquals(0xABCD, cpu.pc); // PCがHLの値に変更される
  }

  // JR命令のテスト
  @Test
  @DisplayName("Test JR r8 forward")
  public void testJRForward() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x18); // JR r8
    cpu.bus.writeByte(0x0001, 0x10); // 相対ジャンプ値: +16
    cpu.step();
    assertEquals(0x0012, cpu.pc); // 0x0002 + 0x10 = 0x0012
  }

  @Test
  @DisplayName("Test JR r8 backward")
  public void testJRBackward() throws Exception {
    CPU cpu = new CPU();
    cpu.pc = 0x1000;
    cpu.bus.writeByte(0x1000, 0x18); // JR r8
    cpu.bus.writeByte(0x1001, 0xFE); // 相対ジャンプ値: -2 (0xFE as signed byte)
    cpu.step();
    assertEquals(0x1000, cpu.pc); // 0x1002 - 2 = 0x1000
  }

  @Test
  @DisplayName("Test JR NZ, r8 with NZ condition met")
  public void testJR_NZ_Met() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x20); // JR NZ, r8
    cpu.bus.writeByte(0x0001, 0x10); // 相対ジャンプ値: +16
    cpu.registers.f.zero = false;
    cpu.step();
    assertEquals(0x0012, cpu.pc); // 0x0002 + 0x10 = 0x0012
  }

  @Test
  @DisplayName("Test JR NZ, r8 with NZ condition not met")
  public void testJR_NZ_NotMet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x20); // JR NZ, r8
    cpu.bus.writeByte(0x0001, 0x10); // 相対ジャンプ値: +16
    cpu.registers.f.zero = true;
    cpu.step();
    assertEquals(0x0002, cpu.pc); // 条件が満たされないので、PCは次の命令へ
  }
}
