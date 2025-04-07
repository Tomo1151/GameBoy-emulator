package com.syntck.cpu;

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

  int get_af() {
    return (this.a << 8) | FlagsRegister.convertToByte(this.f);
  }

  int set_af(int value) {
    this.a = (value & 0xFF00) >> 8;
    this.f = FlagsRegister.fromByte(value & 0x00FF);
    return get_af();
  }
}
