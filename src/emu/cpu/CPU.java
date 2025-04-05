package emu.cpu;

import emu.memory.MemoryBus;

public class CPU {
  private Registers registers; // CPU レジスタ
  private MemoryBus bus; // メモリバス
  private int pc; // プログラムカウンタ

  // MARK: execute()
  int execute(Instruction instruction) {
    switch (instruction.getName()) {
      case ADD: {
        ArithmeticTarget target = ((Instruction.ADD) instruction).getTarget();
        switch (target) {
          case A:
            return this.pc;
          case B:
            return this.pc;
          case C:
            int value = this.registers.c;
            int new_value = add(value);
            this.registers.a = new_value;
            return Functions.overflowing_add(this.pc, 1).value;
          case D:
            return this.pc;
          case E:
            return this.pc;
          case H:
            return this.pc;
          case L:
            return this.pc;
          default:
            throw new IllegalArgumentException("Invalid target");
        }
      }
      case JP: {
        JumpTest test = ((Instruction.JP) instruction).getTest();
        boolean condition;
        switch (test) {
          case NotZero:
            condition = !this.registers.f.zero;
            break;
          case Zero:
            condition = this.registers.f.zero;
            break;
          case NotCarry:
            condition = !this.registers.f.carry;
            break;
          case Carry:
            condition = this.registers.f.carry;
            break;
          case Always:
            condition = true;
          default:
            throw new IllegalArgumentException("Invalid jump test"); 
        }
        jump(condition);
      }

      case LD: {
        LoadByteTarget target = ((Instruction.LD) instruction).getTarget();
        LoadByteSource source = ((Instruction.LD) instruction).getSource();
        int sourceValue;
        
        switch (source) {
          case A: {
            sourceValue = this.registers.a;
          }
          case B: {
            sourceValue = this.registers.b;
          }
          case C: {
            sourceValue = this.registers.c;
          }
          case D: {
            sourceValue = this.registers.d;
          }
          case E: {
            sourceValue = this.registers.e;
          }
          case H: {
            sourceValue = this.registers.h;
          }
          case L: {
            sourceValue = this.registers.l;
          }
          case D8: {
            sourceValue = this.readNextByte();
            break;
          }
          case HLI: {
            sourceValue = this.bus.readByte(this.registers.get_hl());
            break;
          }
          default:
            throw new IllegalArgumentException("Invalid load source");
        }

        switch (target) {
          case A: {
            this.registers.a = sourceValue;
            break;
          }
          case B: {
            this.registers.b = sourceValue;
            break;
          }
          case C: {
            this.registers.c = sourceValue;
            break;
          }
          case D: {
            this.registers.d = sourceValue;
            break;
          }
          case E: {
            this.registers.e = sourceValue;
            break;
          }
          case H: {
            this.registers.h = sourceValue;
            break;
          }
          case L: {
            this.registers.l = sourceValue;
            break;
          }
          case HLI: {
            this.bus.writeByte(this.registers.get_hl(), sourceValue);
            break;
          }
          default:
            throw new IllegalArgumentException("Invalid load target");
        }

        if (source == LoadByteSource.D8) {
          return Functions.overflowing_add(this.pc, 2).value;
        } else {
          return Functions.overflowing_add(this.pc, 1).value;
        }
      }

      default:
        return this.pc;
    }

  }

  void step() {
    // プログラムカウンタから命令を取得
    int instructionByte = this.bus.readByte(this.pc);

    // プレフィックス命令か判別
    boolean isPrefixed = instructionByte == 0xCB;

    if (isPrefixed) {
      // プレフィックス命令の場合、次のバイトを取得
      instructionByte = this.bus.readByte(this.pc + 1);
    }

    Instruction instruction = Instruction.fromByte(instructionByte, isPrefixed);

    if (instruction != null && instruction.isValid()) {
      // Execute the instruction
      int nextPc = execute(instruction);
      this.pc = nextPc;
    } else {
      // Handle invalid instruction
      throw new IllegalArgumentException("Invalid instruction: " + String.format("%04X", instructionByte) + " isPrefixed: " + isPrefixed);
    }
  }

  int add(int value) {
    OverflowingAddResult result = Functions.overflowing_add(this.registers.a, value);

    this.registers.f.zero = result.value == 0x00;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = ((this.registers.a & 0x0F) + (value & 0x0F)) > 0x0F;
    this.registers.f.carry = result.overflow;
    return result.value;
  }

  int jump(boolean condition) {
    if (condition) {
      int leastSignificantByte = this.bus.readByte(this.pc + 1);
      int mostSignificantByte = this.bus.readByte(this.pc + 2);
      return (mostSignificantByte << 8) | leastSignificantByte; // 2バイト足す（リトルエンディアン）
    } else {
      return Functions.overflowing_add(this.pc, 3).value; // 3バイト足す
    }
  }

  int readNextByte() {
    return this.bus.readByte(pc+1);
  }


  public void log(String message) {
    System.out.println(message);
  }
  // public static void main(String[] args) {
  //   CPU cpu = new CPU();
  //   Instruction instruction = new Instruction.ADD(ArithmeticTarget.A);
  //   cpu.execute(instruction);
  // }
}
