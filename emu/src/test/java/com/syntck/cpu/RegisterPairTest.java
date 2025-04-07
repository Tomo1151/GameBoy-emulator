package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RegisterPairTest {

  // ADD HL,rr命令のテスト
  @Test
  @DisplayName("Test ADD HL, BC")
  public void testAddHLBC() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x09); // ADD HL, BC
    cpu.registers.set_hl(0x8A23);
    cpu.registers.set_bc(0x0605);
    cpu.step();
    assertEquals(0x9028, cpu.registers.get_hl()); // 0x8A23 + 0x0605 = 0x9028
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0000, FlagsRegister.convertToInt(cpu.registers.f)); // フラグは設定されない
  }

  @Test
  @DisplayName("Test ADD HL, BC with carry")
  public void testAddHLBC_Carry() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x09); // ADD HL, BC
    cpu.registers.set_hl(0xFFFF);
    cpu.registers.set_bc(0x0001);
    cpu.step();
    assertEquals(0x0000, cpu.registers.get_hl()); // 0xFFFF + 0x0001 = 0x10000 (0x0000 with carry)
    assertEquals(0x0001, cpu.pc);
    assertEquals(0x0030, FlagsRegister.convertToInt(cpu.registers.f)); // ハーフキャリーとキャリーフラグがセット
  }

  // INC rr命令のテスト
  @Test
  @DisplayName("Test INC DE")
  public void testIncDE() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x13); // INC DE
    cpu.registers.set_de(0xFFFF);
    cpu.step();
    assertEquals(0x0000, cpu.registers.get_de()); // 0xFFFF + 1 = 0x10000 (0x0000 with overflow)
    assertEquals(0x0001, cpu.pc);
  }

  // DEC rr命令のテスト
  @Test
  @DisplayName("Test DEC HL")
  public void testDecHL() throws Exception {
    CPU cpu = new CPU();
    cpu.bus.writeByte(0x0000, 0x2B); // DEC HL
    cpu.registers.set_hl(0x0000);
    cpu.step();
    assertEquals(0xFFFF, cpu.registers.get_hl()); // 0x0000 - 1 = 0xFFFF (with underflow)
    assertEquals(0x0001, cpu.pc);
  }
}
