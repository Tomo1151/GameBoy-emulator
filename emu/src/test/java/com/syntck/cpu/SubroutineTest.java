package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class SubroutineTest {

  // CALL命令のテスト
  @Test
  @DisplayName("Test CALL a16")
  public void testCall() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCD); // CALL a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0x00, cpu.bus.readByte(0xFFFC)); // PCの上位バイト
    assertEquals(0x03, cpu.bus.readByte(0xFFFD)); // PCの下位バイト
    assertEquals(0x1234, cpu.pc); // ジャンプ先
  }

  @Test
  @DisplayName("Test CALL Z, a16 with Z flag set")
  public void testCallZ_FlagSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCC); // CALL Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = true;
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0x00, cpu.bus.readByte(0xFFFC)); // PCの上位バイト
    assertEquals(0x03, cpu.bus.readByte(0xFFFD)); // PCの下位バイト
    assertEquals(0x1234, cpu.pc); // ジャンプ先
  }

  @Test
  @DisplayName("Test CALL Z, a16 with Z flag reset")
  public void testCallZ_FlagReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xCC); // CALL Z, a16
    cpu.bus.writeByte(0x0001, 0x34);
    cpu.bus.writeByte(0x0002, 0x12); // アドレス0x1234
    cpu.registers.f.zero = false;
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFE, cpu.sp); // SPは変わらない
    assertEquals(0x0003, cpu.pc); // 次の命令へ
  }

  // RET命令のテスト
  @Test
  @DisplayName("Test RET")
  public void testRet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC9); // RET
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x34);
    cpu.bus.writeByte(0xFFFD, 0x12); // アドレス0x1234
    cpu.step();
    assertEquals(0xFFFE, cpu.sp); // SPが2バイト増加
    assertEquals(0x1234, cpu.pc); // スタックからポップされた値
  }

  @Test
  @DisplayName("Test RET NZ with NZ flag set")
  public void testRetNZ_FlagSet() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC0); // RET NZ
    cpu.registers.f.zero = false;
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x34);
    cpu.bus.writeByte(0xFFFD, 0x12); // アドレス0x1234
    cpu.step();
    assertEquals(0xFFFE, cpu.sp); // SPが2バイト増加
    assertEquals(0x1234, cpu.pc); // スタックからポップされた値
  }

  @Test
  @DisplayName("Test RET NZ with NZ flag reset")
  public void testRetNZ_FlagReset() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC0); // RET NZ
    cpu.registers.f.zero = true;
    cpu.sp = 0xFFFC;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPは変わらない
    assertEquals(0x0001, cpu.pc); // 次の命令へ
  }

  // RETI命令のテスト
  @Test
  @DisplayName("Test RETI")
  public void testReti() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xD9); // RETI
    cpu.sp = 0xFFFC;
    cpu.bus.writeByte(0xFFFC, 0x34);
    cpu.bus.writeByte(0xFFFD, 0x12); // アドレス0x1234
    cpu.interruptMasterEnable = false;
    cpu.step();
    assertEquals(0xFFFE, cpu.sp); // SPが2バイト増加
    assertEquals(0x1234, cpu.pc); // スタックからポップされた値
    assertEquals(true, cpu.getInterruptMasterEnable()); // IMEがセット
  }

  // RST命令のテスト
  @Test
  @DisplayName("Test RST 00H")
  public void testRst00() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xC7); // RST 00H
    cpu.pc = 0x1000;
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0x10, cpu.bus.readByte(0xFFFC)); // PCの上位バイト
    assertEquals(0x01, cpu.bus.readByte(0xFFFD)); // PCの下位バイト
    assertEquals(0x0000, cpu.pc); // ジャンプ先
  }

  @Test
  @DisplayName("Test RST 38H")
  public void testRst38() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xFF); // RST 38H
    cpu.pc = 0x1000;
    cpu.sp = 0xFFFE;
    cpu.step();
    assertEquals(0xFFFC, cpu.sp); // SPが2バイト減少
    assertEquals(0x10, cpu.bus.readByte(0xFFFC)); // PCの上位バイト
    assertEquals(0x01, cpu.bus.readByte(0xFFFD)); // PCの下位バイト
    assertEquals(0x0038, cpu.pc); // ジャンプ先
  }
}
