package emu.cpu;

public class Functions {
  public static OverflowingAddResult overflowing_add(int a, int b) {
    int result = a + b;
    if (result > 0xFF) {
      result &= 0x00FF; // Keep only the lower 8 bits
    }
    return new OverflowingAddResult(result); // Return the result and whether there was an overflow
  }
}

class OverflowingAddResult {
  public static final int OVERFLOW = 1;
  public static final int NO_OVERFLOW = 0;

  public int value;
  public boolean overflow;

  public OverflowingAddResult(int value) {
    this.value = value;
    this.overflow = value > 0xFF;
  }
}