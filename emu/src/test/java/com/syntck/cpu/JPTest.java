package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class JPTest {
  @Test
  @DisplayName("Test JP Z instruction")
  public void testJP_Zero() throws Exception {
    // JP Zフラグ時のみ, 16ビットアドレスにジャンプする
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, 16ビットアドレス
    cpu.bus.writeByte(0x0001, 0x01);
    cpu.bus.writeByte(0x0002, 0x02); // JP先アドレス
    cpu.registers.f.zero = true; // Zeroフラグをセット
    cpu.step();
    assertEquals(0x0201, cpu.pc); // 0xCA によりその後16ビットをアドレスとして読み込み，ジャンプ(リトルエンディアン)
  }

  @Test
  @DisplayName("Test JP Z instruction with no jump")
  public void testJP() throws Exception {
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, 16ビットアドレス
    cpu.bus.writeByte(0x0001, 0x01);
    cpu.bus.writeByte(0x0002, 0x02); // JP先アドレス
    cpu.registers.f.zero = false; // Zeroフラグをセット
    cpu.step();
    assertEquals(0x0003, cpu.pc); // Zeroフラグが立っていないので，PCはインクリメント(命令長)される
  }

  @Test
  @DisplayName("Test JP NZ instruction")
  public void testJP_NotZero() throws Exception {
    // JP Zフラグ時のみ, 16ビットアドレスにジャンプする
    CPU cpu = new CPU();
    cpu.log("CPU initialized.");
    cpu.bus.writeByte(0x0000, 0xCA); // JP Z, 16ビットアドレス
    cpu.bus.writeByte(0x0001, 0x01);
    cpu.bus.writeByte(0x0002, 0x02); // JP先アドレス
    cpu.registers.f.zero = true; // Zeroフラグをセット
    cpu.step();
    assertEquals(0x0201, cpu.pc); // 0xCA によりその後16ビットをアドレスとして読み込み，ジャンプ(リトルエンディアン)
  }
}
