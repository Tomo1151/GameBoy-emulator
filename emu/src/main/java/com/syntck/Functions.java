package com.syntck;

import com.syntck.cpu.FlagsRegister;

public class Functions {
  // Fix overflowingAdd to handle 16-bit values
  public static OverflowingResult overflowingAdd(int a, int b) {
    int fullSum = a + b;
    int result = fullSum & 0xFF; // 8ビットにマスク
    boolean overflow = fullSum > 0xFF;
    return new OverflowingResult(result, overflow);
  }

  public static OverflowingResult overflowingAdd16(int a, int b) {
    int fullSum = a + b;
    boolean overflow = fullSum > 0xFFFF;
    int result = fullSum & 0xFFFF;
    return new OverflowingResult(result, overflow);
  }

  // Fix overflowingSubtract to handle 16-bit values
  public static OverflowingResult overflowingSub(int a, int b) {
    int fullResult = a - b;
    int result = fullResult & 0xFF; // 8ビットにマスク
    boolean overflow = fullResult < 0;
    return new OverflowingResult(result, overflow);
  }

  public static OverflowingResult overflowingSub16(int a, int b) {
    int fullResult = a - b;
    int result = fullResult & 0xFFFF; // 16ビットにマスク
    boolean overflow = fullResult < 0;
    return new OverflowingResult(result, overflow);
  }

  public static int wrappingSub16(int a, int b) {
    int result = a - b;
    if (result < 0) {
      result &= 0xFFFF; // 必ず16ビットでマスク
    }
    return result;
  }

  public static int wrappingAdd16(int a, int b) {
    int result = a + b;
    if (result > 0xFFFF) {
      result &= 0xFFFF; // 必ず16ビットでマスク
    }
    return result;
  }

  public static int wrappingAdd(int a, int b) {
    // 入力値に基づいて8ビットか16ビット演算かを判断
    boolean is16Bit = a > 0xFF || b > 0xFF;
    return is16Bit ? (a + b) & 0xFFFF : (a + b) & 0xFF;
  }

  public static int wrappingSub(int a, int b) {
    // 入力値に基づいて8ビットか16ビット演算かを判断
    boolean is16Bit = a > 0xFF || b > 0xFF;
    return is16Bit ? (a - b) & 0xFFFF : (a - b) & 0xFF;
  }

  public static boolean compareFlagsRegister(FlagsRegister actual, boolean zero, boolean subtract, boolean halfCarry, boolean carry) {
    return zero == actual.zero &&
           subtract == actual.subtract &&
           halfCarry == actual.halfCarry &&
           carry == actual.carry;
  }


  public static class OverflowingResult {
    public int value;
    public boolean overflow;

    public OverflowingResult(int value, boolean overflow) {
      this.value = value;
      this.overflow = overflow;
    }
  }
}