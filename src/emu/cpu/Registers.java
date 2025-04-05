package emu.cpu;

public class Registers {
  public int a;
  public int b;
  public int c;
  public int d;
  public int e;
  public FlagsRegister f;
  public int h;
  public int l;

  int get_bc() {
    return (this.b << 8) | this.c;
  }

  int set_bc(int value) {
    this.b = (value & 0xFF00) >> 8;
    this.c = value & 0x00FF;
    return get_bc();
  }

  int get_de() {
    return (this.d << 8) | this.e;
  }

  int set_de(int value) {
    this.d = (value & 0xFF00) >> 8;
    this.e = value & 0x00FF;
    return get_de();
  }

  int get_hl() {
    return (this.h << 8) | this.l;
  }

  int set_hl(int value) {
    this.h = (value & 0xFF00) >> 8;
    this.l = value & 0x00FF;
    return get_hl();
  }
}

class FlagsRegister {
  public static final int ZERO_FLAG_BYTE_POSITION = 7;
  public static final int SUBTRACT_FLAG_BYTE_POSITION = 6;
  public static final int HALF_CARRY_FLAG_BYTE_POSITION = 5;
  public static final int CARRY_FLAG_BYTE_POSITION = 4;

  public boolean zero; // Z flag
  public boolean subtract; // N flag
  public boolean halfCarry; // H flag
  public boolean carry; // C flag

  public static int convertToInt(FlagsRegister flag) {

    int result = 0x0000;
    if (flag.zero) result |= (1 << ZERO_FLAG_BYTE_POSITION);
    if (flag.subtract) result |= (1 << SUBTRACT_FLAG_BYTE_POSITION);
    if (flag.halfCarry) result |= (1 << HALF_CARRY_FLAG_BYTE_POSITION);
    if (flag.carry) result |= (1 << CARRY_FLAG_BYTE_POSITION);
    return result;
  }

  public static FlagsRegister convertToFlagsRegister(int value) {
    FlagsRegister flag = new FlagsRegister();
    flag.zero = (value & (1 << ZERO_FLAG_BYTE_POSITION)) != 0x0000;
    flag.subtract = (value & (1 << SUBTRACT_FLAG_BYTE_POSITION)) != 0x0000;
    flag.halfCarry = (value & (1 << HALF_CARRY_FLAG_BYTE_POSITION)) != 0x0000;
    flag.carry = (value & (1 << CARRY_FLAG_BYTE_POSITION)) != 0x0000;
    return flag;
  }
}