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

  public Registers() {
    this.a = 0x11; // Aレジスタの初期値
    this.b = 0x00; // Bレジスタの初期値
    this.c = 0x00; // Cレジスタの初期値
    this.d = 0xFF; // Dレジスタの初期値
    this.e = 0x56; // Eレジスタの初期値
    this.h = 0x00; // Hレジスタの初期値
    this.l = 0x0D; // Lレジスタの初期値
    this.f = new FlagsRegister(); // Fレジスタの初期値
  }

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

  public void clear() {
    this.a = 0x00; // Aレジスタをクリア
    this.b = 0x00; // Bレジスタをクリア
    this.c = 0x00; // Cレジスタをクリア
    this.d = 0x00; // Dレジスタをクリア
    this.e = 0x00; // Eレジスタをクリア
    this.h = 0x00; // Hレジスタをクリア
    this.l = 0x00; // Lレジスタをクリア
    this.f.clear(); // Fレジスタをクリア
  }
}
