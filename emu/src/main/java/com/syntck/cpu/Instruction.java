package com.syntck.cpu;

public class Instruction {
  private final InstructionType type;
  private final Object operand0;  // 命令ごとの第1オペランド
  private final Object operand1;  // 命令ごとの第2オペランド（必要な場合）

  private Instruction(InstructionType type, Object operand0, Object operand1) {
    this.type = type;
    this.operand0 = operand0;
    this.operand1 = operand1;
  }

  public InstructionType getType() {
    return type;
  }

  // MARK: ゲッター
  // 各命令のオペランドに合わせたキャストを行う
  public ArithmeticTarget getArithmeticTarget() {
    return (ArithmeticTarget)operand0;
  }

  public JumpTest getJumpTest() {
    return (JumpTest)operand0;
  }

  public LoadByteTarget getLoadByteTarget() {
    return (LoadByteTarget)operand0;
  }

  public LoadByteSource getLoadByteSource() {
    return (LoadByteSource)operand1;
  }

  public StackTarget getStackTarget() {
    return (StackTarget)operand0;
  }

  // ファクトリーメソッド群
  // MARK: ADD命令の定義
  // 1つのArithmeticTargetをオペランドとして持つ
  public static Instruction createADD(ArithmeticTarget target) {
    return new Instruction(InstructionType.ADD, target, null);
  }

  // MARK: JP命令の定義
  // 条件（JumpTest）を第1オペランドに持つ
  public static Instruction createJP(JumpTest test) {
    return new Instruction(InstructionType.JP, test, null);
  }

  // MARK: LD命令の定義
  // LoadByteTarget と LoadByteSource の2オペランドを持つ
  public static Instruction createLD(LoadByteTarget target, LoadByteSource source) {
    return new Instruction(InstructionType.LD, target, source);
  }



  public static Instruction createPUSH(StackTarget target) {
    return new Instruction(InstructionType.PUSH, target, null);
  }



  public static Instruction createPOP(StackTarget target) {
    return new Instruction(InstructionType.POP, target, null);
  }


  public static Instruction createCALL(StackTarget target) {
    return new Instruction(InstructionType.CALL, target, null);
  }

  public static Instruction createRET() {
    return new Instruction(InstructionType.RET, null, null);
  }

  // MARK: 命令デコード
  public static Instruction fromByte(int instructionByte, boolean isPrefixed) {
    if (isPrefixed) {
      return fromBytePrefixed(instructionByte);
    } else {
      return fromByteNotPrefixed(instructionByte);
    }
  }

  // MARK: プレフィックス命令
  // 0xCB プレフィックスがある場合、特定の命令をデコードする
  private static Instruction fromBytePrefixed(int instructionByte) {
    switch (instructionByte) {
      case 0x00: return createADD(ArithmeticTarget.B);
      case 0x01: return createADD(ArithmeticTarget.C);
      case 0x02: return createADD(ArithmeticTarget.D);
      case 0x03: return createADD(ArithmeticTarget.E);
      case 0x04: return createADD(ArithmeticTarget.H);
      case 0x05: return createADD(ArithmeticTarget.L);
      case 0x07: return createADD(ArithmeticTarget.A);
      default: return null;
    }
  }

  // MARK: 通常命令
  private static Instruction fromByteNotPrefixed(int instructionByte) {
    switch (instructionByte) {
      // ADD命令例: 0x80～0x87
      case 0x80: return createADD(ArithmeticTarget.B);
      case 0x81: return createADD(ArithmeticTarget.C);
      case 0x82: return createADD(ArithmeticTarget.D);
      case 0x83: return createADD(ArithmeticTarget.E);
      case 0x84: return createADD(ArithmeticTarget.H);
      case 0x85: return createADD(ArithmeticTarget.L);
      case 0x87: return createADD(ArithmeticTarget.A);
      // JP命令例
      case 0xCA: return createJP(JumpTest.Zero);
      case 0xC2: return createJP(JumpTest.NotZero);
      case 0xD2: return createJP(JumpTest.NotCarry);
      case 0xDA: return createJP(JumpTest.Carry);
      case 0xC3: return createJP(JumpTest.Always);
      // その他の命令...
      default: return null;
    }
  }

  public boolean isValid() {
    return true;
  }
}

// MARK: 命令タイプの列挙型
enum InstructionType {
  ADD, JP, LD, PUSH, POP, CALL, RET, NOP, HALT
  // …その他の命令タイプを追加
}

// MARK: 各命令のオペランドに対応する列挙型
enum ArithmeticTarget {
  A, B, C, D, E, H, L
}

enum LoadByteTarget {
  A, B, C, D, E, H, L, HLI
}

enum LoadByteSource {
  A, B, C, D, E, H, L, D8, HLI,
}

enum JumpTest {
  NotZero, Zero, NotCarry, Carry, Always,
}

enum StackTarget {
  BC, DE, HL, AF, HLIndirect,
}
