package com.syntck.cpu;

public class Functions {
  public static OverflowingAddResult overflowingAdd(int a, int b) {
    int fullSum = a + b;
    // 255を超えるとオーバーフロー
    boolean overflow = fullSum > 0xFF;
    int result = fullSum & 0xFF;
    return new OverflowingAddResult(result, overflow);
}

  public static OverflowSubtractResult overflowingSubtract(int a, int b) {
    int result = a - b;
    if (result < 0x00) {
      result &= 0x00FF; // Keep only the lower 8 bits
    }
    return new OverflowSubtractResult(result); // Return the result and whether there was an overflow
  }

  public static int wrappingAdd(int a, int b) {
    return (a + b) & 0xFF;
  }
}

class OverflowingAddResult {
  public static final int OVERFLOW = 1;
  public static final int NO_OVERFLOW = 0;

  public int value;
  public boolean overflow;

  public OverflowingAddResult(int value, boolean overflow) {
    this.value = value;
    this.overflow = overflow;
  }
}

class OverflowSubtractResult {
  public int value;
  public boolean overflow;

  public OverflowSubtractResult(int value) {
    this.value = value;
    this.overflow = value < 0x00;
  }
}