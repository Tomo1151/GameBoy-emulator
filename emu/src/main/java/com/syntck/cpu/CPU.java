package com.syntck.cpu;

import com.syntck.memory.MemoryBus;

public class CPU {
  public Registers registers; // CPU レジスタ
  public MemoryBus bus; // メモリバス
  public int pc; // プログラムカウンタ
  public int sp; // スタックポインタ

  public CPU() {
    this.registers = new Registers();
    this.registers.f = new FlagsRegister();
    this.bus = new MemoryBus();
    this.pc = 0x0000; // プログラムカウンタの初期値
    this.sp = 0xFFFF; // スタックポインタの初期値
  }

  // MARK: execute()
  int execute(Instruction instruction) throws IllegalArgumentException {
    switch (instruction.getName()) {
      // MARK: ADD命令
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
      // MARK: JP命令
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
            break;
          default:
            throw new IllegalArgumentException("Invalid jump test"); 
        }
        return jump(condition);
      }
      // MARK: LD命令
      case LD: {
        LoadByteTarget target = ((Instruction.LD) instruction).getTarget();
        LoadByteSource source = ((Instruction.LD) instruction).getSource();
        int sourceValue;
        
        switch (source) {
          case A: {
            sourceValue = this.registers.a;
            break;
          }
          case B: {
            sourceValue = this.registers.b;
            break;
          }
          case C: {
            sourceValue = this.registers.c;
            break;
          }
          case D: {
            sourceValue = this.registers.d;
            break;
          }
          case E: {
            sourceValue = this.registers.e;
            break;
          }
          case H: {
            sourceValue = this.registers.h;
            break;
          }
          case L: {
            sourceValue = this.registers.l;
            break;
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

      // MARK: PUSH命令
      case PUSH: {
        StackTarget target = ((Instruction.PUSH) instruction).getTarget();
        switch (target) {
          case BC:
            push(this.registers.get_bc());
            return Functions.overflowing_add(this.pc, 1).value;
          case DE:
            push(this.registers.get_de());
            return Functions.overflowing_add(this.pc, 1).value;
          case HL:
            push(this.registers.get_hl());
            return Functions.overflowing_add(this.pc, 1).value;
          default:
            throw new IllegalArgumentException("Invalid stack target");
        }
      }

      // MARK: POP命令
      case POP: {
        StackTarget target = ((Instruction.POP) instruction).getTarget();
        int result = pop();
        switch (target) {
          case BC:
            this.registers.set_bc(result);
            return Functions.overflowing_add(this.pc, 1).value;
          case DE:
            this.registers.set_de(result);
            return Functions.overflowing_add(this.pc, 1).value;
          case HL:
            this.registers.set_hl(result);
            return Functions.overflowing_add(this.pc, 1).value;
          default:
            throw new IllegalArgumentException("Invalid stack source");
        }
      }

      // MARK: CALL命令
      case CALL: {
        JumpTest test = ((Instruction.CALL) instruction).getTest();
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
            break;
          default:
            throw new IllegalArgumentException("Invalid jump test"); 
        }
        return call(condition);
      }

      // MARK: RET命令
      case RET: {
        JumpTest test = ((Instruction.RET) instruction).getTest();
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
            break;
          default:
            throw new IllegalArgumentException("Invalid jump test");
        }
        return return_(condition);
      }

      default:
        return this.pc;
    }

  }

  // MARK: step()
  public void step() {
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

  // MARK: add()
  int add(int value) {
    OverflowingAddResult result = Functions.overflowing_add(this.registers.a, value);

    this.registers.f.zero = result.value == 0x00;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = ((this.registers.a & 0x0F) + (value & 0x0F)) > 0x0F;
    this.registers.f.carry = result.overflow;
    return result.value;
  }

  // MARK: jump()
  int jump(boolean condition) {
    if (condition) {
      return readNextWord(); // 次のワード(2byte)を読み込む
    } else {
      return Functions.overflowing_add(this.pc, 3).value; // 3バイト足す
    }
  }

  // MARK: push()
  void push(int value) {
    this.sp = Functions.overflowing_subtract(this.sp, 1).value; // スタックポインタを1バイト分減らす
    this.bus.writeByte(this.sp, ((value & 0xFF00) >> 8)); // スタックに上位バイトを書き込む
    this.sp = Functions.overflowing_subtract(this.sp, 1).value; // スタックポインタを1バイト分減らす
    this.bus.writeByte(this.sp, (value & 0x00FF)); // スタックに下位バイトを書き込む
  }

  // MARK: pop()
  int pop() {
    int lsb = this.bus.readByte(this.sp); // スタックから下位バイトを読み込む
    this.sp = Functions.overflowing_add(this.sp, 1).value; // スタックポインタを1バイト分増やす
    int msb = this.bus.readByte(this.sp); // スタックから上位バイトを読み込む
    this.sp = Functions.overflowing_add(this.sp, 1).value; // スタックポインタを1バイト分増やす
    return ((msb << 8) | lsb); // リトルエンディアンで結合
  }


  // MARK: call()
  int call(boolean condition) {
    int nextPc = Functions.overflowing_add(this.pc, 3).value; // 次のPCのアドレスを計算
    if (condition) {
      push(nextPc); // 次のPCをスタックにプッシュ
      return readNextWord(); // 次のワードを読み込む
    } else {
      return nextPc;
    }
  }

  // MARK: ret()
  int return_(boolean condition) {
    if (condition) {
      return pop(); // スタックからポップしてPCを更新
    } else {
      return Functions.overflowing_add(this.pc, 1).value; // PCを1バイト進める
    }
  }

  // MARK: readNextByte()
  int readNextByte() {
    return this.bus.readByte(pc+1);
  }

  int readNextWord() {
    int leastSignificantByte = this.bus.readByte(this.pc + 1);
    int mostSignificantByte = this.bus.readByte(this.pc + 2);
    return (mostSignificantByte << 8) | leastSignificantByte; // リトルエンディアンで結合
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
