package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class LDTest {

  // MARK: LD (A, B)
  @Test
  @DisplayName("Test LD A, B")
  public void testLDAB() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x78); // LD A, B
    cpu.registers.a = 0x00;
    cpu.registers.b = 0x42;
    cpu.step();
    assertEquals(0x42, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD (C, d8)
  @Test
  @DisplayName("Test LD C, d8")
  public void testLDCd8() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0E); // LD C, d8
    cpu.bus.writeByte(0x0001, 0xAB); // 即値
    cpu.registers.c = 0x00;
    cpu.step();
    assertEquals(0xAB, cpu.registers.c);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: LD ((HL), D)
  @Test
  @DisplayName("Test LD (HL), D")
  public void testLDHLAddrD() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x72); // LD (HL), D
    cpu.registers.set_hl(0xC000);
    cpu.registers.d = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.bus.readByte(0xC000));
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD (A, (BC))
  @Test
  @DisplayName("Test LD A, (BC)")
  public void testLDABCAddr() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x0A); // LD A, (BC)
    cpu.registers.set_bc(0xC000);
    cpu.bus.writeByte(0xC000, 0x42);
    cpu.step();
    assertEquals(0x42, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD ((DE), A)
  @Test
  @DisplayName("Test LD (DE), A")
  public void testLDDEAddrA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x12); // LD (DE), A
    cpu.registers.set_de(0xC000);
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.bus.readByte(0xC000));
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD ((HL+), A)
  @Test
  @DisplayName("Test LD (HL+), A")
  public void testLDHLPlusA() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x22); // LD (HL+), A
    cpu.registers.set_hl(0xC000);
    cpu.registers.a = 0x5A;
    assertEquals(cpu.registers.get_hl(), 0xC000);
    cpu.step();
    assertEquals(0x5A, cpu.bus.readByte(0xC000));
    assertEquals(0xC001, cpu.registers.get_hl()); // HLが増加
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD (A, (HL-))
  @Test
  @DisplayName("Test LD A, (HL-)")
  public void testLDAHLMinus() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x3A); // LD A, (HL-)
    cpu.registers.set_hl(0xC000);
    cpu.bus.writeByte(0xC000, 0x42);
    cpu.step();
    assertEquals(0x42, cpu.registers.a);
    assertEquals(0xBFFF, cpu.registers.get_hl()); // HLが減少
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD ((a16), A)
  @Test
  @DisplayName("Test LD (a16), A")
  public void testLDa16A() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xEA); // LD (a16), A
    cpu.bus.writeByte(0x0001, 0x00);
    cpu.bus.writeByte(0x0002, 0xC0); // アドレス0xC000
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.bus.readByte(0xC000));
    assertEquals(0x0003, cpu.pc);
  }

  // MARK: LD (A, (a16))
  @Test
  @DisplayName("Test LD A, (a16)")
  public void testLDAa16() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xFA); // LD A, (a16)
    cpu.bus.writeByte(0x0001, 0x00);
    cpu.bus.writeByte(0x0002, 0xC0); // アドレス0xC000
    cpu.bus.writeByte(0xC000, 0x42);
    cpu.step();
    assertEquals(0x42, cpu.registers.a);
    assertEquals(0x0003, cpu.pc);
  }

  // MARK: LDH ((a8), A)
  @Test
  @DisplayName("Test LDH (a8), A")
  public void testLDHa8A() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xE0); // LDH (a8), A
    cpu.bus.writeByte(0x0001, 0x80); // 0xFF80のオフセット
    cpu.registers.a = 0x5A;
    cpu.step();
    assertEquals(0x5A, cpu.bus.readByte(0xFF80));
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: LDH (A, (C))
  @Test
  @DisplayName("Test LDH A, (C)")
  public void testLDHAC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF2); // LDH A, (C)
    cpu.registers.c = 0x80; // 0xFF80のオフセット
    cpu.bus.writeByte(0xFF80, 0x42);
    cpu.step();
    assertEquals(0x42, cpu.registers.a);
    assertEquals(0x0001, cpu.pc);
  }

  // MARK: LD (HL, SP+r8) +
  @Test
  @DisplayName("Test LD HL, SP+r8 positive")
  public void testLDHLSPr8_Positive() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF8); // LD HL, SP+r8
    cpu.bus.writeByte(0x0001, 0x02); // オフセット +2
    cpu.sp = 0xC000;
    cpu.step();
    assertEquals(0xC002, cpu.registers.get_hl());
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry);
    assertEquals(false, cpu.registers.f.carry);
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: LD (HL, SP+r8) -
  @Test
  @DisplayName("Test LD HL, SP+r8 negative")
  public void testLDHLSPr8_Negative() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0xF8); // LD HL, SP+r8
    cpu.bus.writeByte(0x0001, 0xFF); // オフセット -1
    cpu.sp = 0x1040;
    cpu.step();
    assertEquals(0x1040 - 1, cpu.registers.get_hl());
    assertEquals(false, cpu.registers.f.zero);
    assertEquals(false, cpu.registers.f.subtract);
    assertEquals(false, cpu.registers.f.halfCarry); // ハーフキャリーが発生
    assertEquals(true, cpu.registers.f.carry); // キャリーが発生
    assertEquals(0x0002, cpu.pc);
  }

  // MARK: LD ((a16), SP)
  @Test
  @DisplayName("Test LD (a16), SP")
  public void testLDa16SP() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x08); // LD (a16), SP
    cpu.bus.writeByte(0x0001, 0x02);
    cpu.bus.writeByte(0x0002, 0xC0); // アドレス0xC002
    cpu.sp = 0x1234;
    cpu.step();
    assertEquals(0x1234, cpu.bus.readWord(0xC002)); // SPの値がメモリに書き込まれる
    assertEquals(0x0003, cpu.pc);
  }
}
