package emu.cpu;

public abstract class Instruction {
  public abstract InstructionName getName();
  public boolean isValid() {
    return true;
  }

  public static Instruction fromByte(int instructionByte, boolean isPrefixed) {
    // バイトコードから命令をでコードする
    if (isPrefixed) {
      return fromBytePrefixed(instructionByte);
    } else {
      return fromByteNotPrefixed(instructionByte);
    }
  }

  public static Instruction fromBytePrefixed(int instructionByte) {
    // プレフィックス命令のデコード
    switch (instructionByte) {
      case 0x00:
        return new ADD(ArithmeticTarget.B);
      case 0x01:
        return new ADD(ArithmeticTarget.C);
      case 0x02:
        return new ADD(ArithmeticTarget.D);
      case 0x03:
        return new ADD(ArithmeticTarget.E);
      case 0x04:
        return new ADD(ArithmeticTarget.H);
      case 0x05:
        return new ADD(ArithmeticTarget.L);
      case 0x07:
        return new ADD(ArithmeticTarget.A);
      default:
        return null; // Invalid instruction
    }
  }

  public static Instruction fromByteNotPrefixed(int instructionByte) {
    // プレフィックス命令でない場合のデコード
    switch (instructionByte) {
      case 0x80:
        return new ADD(ArithmeticTarget.B);
      case 0x81:
        return new ADD(ArithmeticTarget.C);
      case 0x82:
        return new ADD(ArithmeticTarget.D);
      case 0x83:
        return new ADD(ArithmeticTarget.E);
      case 0x84:
        return new ADD(ArithmeticTarget.H);
      case 0x85:
        return new ADD(ArithmeticTarget.L);
      case 0x87:
        return new ADD(ArithmeticTarget.A);
      default:
        return null; // Invalid instruction
    }
  }

  // ADD命令の実装
  public static final class ADD extends Instruction {
    public final InstructionName name = InstructionName.ADD;
    private final ArithmeticTarget target;

    public ADD(ArithmeticTarget target) {
      this.target = target;
    }

    public ArithmeticTarget getTarget() {
      return target;
    }

    @Override
    public InstructionName getName() {
      return name;
    }
  };

  // JP命令の実装
  public static final class JP extends Instruction {
    public final InstructionName name = InstructionName.JP;
    private final JumpTest test;

    public JP(JumpTest test) {
      this.test = test;
    }

    public JumpTest getTest() {
      return test;
    }

    @Override
    public InstructionName getName() {
      return name;
    }
  };

  // LD命令の実装
  public static final class LD extends Instruction {
    public final InstructionName name = InstructionName.LD;
    private final LoadByteTarget target;
    private final LoadByteSource source;

    public LD(LoadByteTarget target, LoadByteSource source) {
      this.target = target;
      this.source = source;
    }

    public LoadByteTarget getTarget() {
      return target;
    }

    public LoadByteSource getSource() {
      return source;
    }

    @Override
    public InstructionName getName() {
      return name;
    }
  };

}

enum InstructionName {
  ADD,
  JP,
  LD,
}

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
  NotZero,
  Zero,
  NotCarry,
  Carry,
  Always,
}