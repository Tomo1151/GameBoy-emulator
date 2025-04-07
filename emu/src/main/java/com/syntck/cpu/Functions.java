package com.syntck.cpu;

public class Functions {
  // Fix overflowingAdd to handle 16-bit values
  public static OverflowingAddResult overflowingAdd(int a, int b) {
    int fullSum = a + b;
    // For 8-bit operations
    boolean overflow8 = (a & 0xFF) + (b & 0xFF) > 0xFF;
    // For 16-bit operations
    boolean overflow16 = (a & 0xFFFF) + (b & 0xFFFF) > 0xFFFF;
    
    // Determine if this is an 8-bit or 16-bit operation based on the inputs
    boolean is16Bit = a > 0xFF || b > 0xFF;
    int result = is16Bit ? fullSum & 0xFFFF : fullSum & 0xFF;
    boolean overflow = is16Bit ? overflow16 : overflow8;
    
    return new OverflowingAddResult(result, overflow);
  }

  // Fix overflowingSubtract to handle 16-bit values
  public static OverflowSubtractResult overflowingSubtract(int a, int b) {
    int result = a - b;
    // Determine if this is an 8-bit or 16-bit operation based on the inputs
    boolean is16Bit = a > 0xFF || b > 0xFF;
    
    if (is16Bit) {
      if (result < 0) {
        result &= 0xFFFF; // Keep only the lower 16 bits
      }
    } else {
      if (result < 0) {
        result &= 0xFF; // Keep only the lower 8 bits
      }
    }
    
    return new OverflowSubtractResult(result);
  }

  public static int wrappingAdd(int a, int b) {
    return (a + b) & 0xFF;
  }

  public static int wrappingSub(int a, int b) {
    return (a - b) & 0xFF;
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