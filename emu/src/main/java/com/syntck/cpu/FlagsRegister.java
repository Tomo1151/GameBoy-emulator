package com.syntck.cpu;

public class FlagsRegister {
  public static final int ZERO_FLAG_BYTE_POSITION = 7;
  public static final int SUBTRACT_FLAG_BYTE_POSITION = 6;
  public static final int HALF_CARRY_FLAG_BYTE_POSITION = 5;
  public static final int CARRY_FLAG_BYTE_POSITION = 4;

  public boolean zero; // Z flag
  public boolean subtract; // N flag
  public boolean halfCarry; // H flag
  public boolean carry; // C flag

  public FlagsRegister() {
    this.zero = false;
    this.subtract = false;
    this.halfCarry = false;
    this.carry = false;
  }

  public FlagsRegister(boolean zero, boolean subtract, boolean halfCarry, boolean carry) {
    this.zero = zero;
    this.subtract = subtract;
    this.halfCarry = halfCarry;
    this.carry = carry;
  }

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

  public void dump() {
    System.out.println("Flags Register:");
    System.out.println("  Zero: " + (zero ? "1" : "0"));
    System.out.println("  Subtract: " + (subtract ? "1" : "0"));
    System.out.println("  Half Carry: " + (halfCarry ? "1" : "0"));
    System.out.println("  Carry: " + (carry ? "1" : "0"));
    System.out.println("  Value: " + String.format("0x%04X", convertToInt(this)));
    System.out.println("  Value (binary): " + String.format("%16s", Integer.toBinaryString(convertToInt(this))).replace(' ', '0'));
    System.out.println("  Value (hex): " + String.format("0x%04X", convertToInt(this)));
    System.out.println("  Value (decimal): " + convertToInt(this));
  }
}
