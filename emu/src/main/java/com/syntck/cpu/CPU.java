package com.syntck.cpu;

import static com.syntck.Functions.*;

import com.syntck.Functions.OverflowingAddResult;
import com.syntck.memory.MemoryBus;

public class CPU {
  public Registers registers; // CPU レジスタ
  public MemoryBus bus; // メモリバス
  public int pc; // プログラムカウンタ
  public int sp; // スタックポインタ
  public int cycles; // サイクル数
  public boolean interruptMasterEnable; // 割り込み許可フラグ
  private boolean halted; // HALTフラグ

  public CPU() {
    this.registers = new Registers();
    this.registers.f = new FlagsRegister();
    this.bus = new MemoryBus(this);
    this.pc = 0x0000; // プログラムカウンタの初期値
    this.sp = 0xFFFF; // スタックポインタの初期値
    this.interruptMasterEnable = false;
    this.halted = false;
  }

  public boolean getInterruptMasterEnable() {
    return this.interruptMasterEnable;
  }

  // HALTフラグのゲッター
  public boolean isHalted() {
    return this.halted;
  }

  // MARK: 命令の実行
  // 引数に与えられた命令を実行し、次のPCを返す
  boolean execute(Instruction instruction) throws IllegalArgumentException {
    switch (instruction.getType()) {
      // MARK: ADD (A, HL, SP), ADC
      case ADD: {
        // レジスタAに対して加算を行う命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = addA(value);
        this.registers.a = newValue;
        return true;
      }

      case ADDHL: {
        // HLレジスタに対して加算を行う命令
        RegisterPair pair = instruction.getRegisterPair();
        int value = getValueForRegisterPair(pair);
        int result = addHL(value);
        this.registers.set_hl(result);
        return true;
      }

      case ADDSP: {
        // スタックポインタに対して加算を行う命令
        int value = readNextByte();
        if (value > 0xFF) value = value - 0x100; // 符号付き8ビットに変換
        this.sp = addSP(value);
        return true;
      }

      case ADC: {
        // キャリーを考慮してレジスタAに加算を行う命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int carryValue = this.registers.f.carry ? 1 : 0;
        int newValue = addWithCarry(value, carryValue);
        this.registers.a = newValue;
        return true;
      }

      // MARK: SUB, SBC
      case SUB: {
        // レジスタAから引き算を行う命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = subtract(value);
        this.registers.a = newValue;
        return true;
      }

      case SBC: {
        // キャリーを考慮してレジスタAから引き算を行う命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int carryValue = this.registers.f.carry ? 1 : 0;
        int newValue = subtractWithCarry(value, carryValue);
        this.registers.a = newValue;
        return true;
      }

      // MARK: INC
      case INC: {
        // 指定したレジスタやアドレスの値をインクリメントする命令 (8bit)
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = increment(value);
        setValueForArithmeticTarget(target, newValue);
        return true;
      }

      case INCRP: {
        // 指定したレジスタやアドレスの値をインクリメントする命令 (16bit)
        RegisterPair pair = instruction.getRegisterPair();
        int value = getValueForRegisterPair(pair);
        int result = overflowingAdd(value, 1).value;
        setValueForRegisterPair(pair, result);
        return true;
      }

      // MARK: DEC
      case DEC: {
        // 指定したレジスタやアドレスの値をデクリメントする命令 (8bit)
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = decrement(value);
        setValueForArithmeticTarget(target, newValue);
        return true;
      }

      case DECRP: {
        // 指定したレジスタやアドレスの値をデクリメントする命令 (16bit)
        RegisterPair pair = instruction.getRegisterPair();
        int value = getValueForRegisterPair(pair);
        int result = wrappingSub16(value, 1);
        setValueForRegisterPair(pair, result);
        return true;
      }

      // MARK: CP
      case CP: {
        // 指定したレジスタやアドレスの値をレジスタAと比較する命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        cp(value); // CPは結果を保存しない
        return true;
      }
      
      // MARK: AND
      case AND: {
        // 指定したレジスタやアドレスの値とレジスタAの論理積を取る命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = and(value);
        this.registers.a = newValue;
        return true;
      }
      
      // MARK: OR
      case OR: {
        // 指定したレジスタやアドレスの値とレジスタAの論理和を取る命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = or(value);
        this.registers.a = newValue;
        return true;
      }
      
      // MARK: XOR
      case XOR: {
        // 指定したレジスタやアドレスの値とレジスタAの排他的論理和を取る命令
        ArithmeticTarget target = instruction.getArithmeticTarget();
        int value = getValueForArithmeticTarget(target);
        int newValue = xor(value);
        this.registers.a = newValue;
        return true;
      }

      // MARK: CCF, SCF, CPL
      case CCF: {
        // Cフラグを反転させる命令
        this.registers.f.carry = !this.registers.f.carry;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        return true;
      }
      
      case SCF: {
        // Cフラグをセットする命令
        this.registers.f.carry = true;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        return true;
      }
      
      case CPL: {
        // レジスタAの値を反転させる命令
        this.registers.a = (~this.registers.a) & 0xFF;
        this.registers.f.subtract = true;
        this.registers.f.halfCarry = true;
        return true;
      }

      // MARK: BIT [prefixed]
      case BIT: {
        // 指定されたレジスタ(operand1)の指定されたビット(operand1)が0かどうかをテストする命令
        BitPosition bitPos = instruction.getBitPosition();
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        int bitNumber = getBitNumber(bitPos);
        boolean bitValue = (value & (1 << bitNumber)) != 0;
        this.registers.f.zero = !bitValue;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = true;
        return true;
      }

      // MARK: RES [prefixed]
      case RES: {
        // 指定されたレジスタ(operand1)の指定されたビット(operand1)を0にする命令
        BitPosition bitPos = instruction.getBitPosition();
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        int bitNumber = getBitNumber(bitPos);
        value &= ~(1 << bitNumber);
        setValueForRotateTarget(target, value);
        return true;
      }

      // MARK: SET [prefixed]
      case SET: {
        // 指定されたレジスタ(operand1)の指定されたビット(operand1)を1にする命令
        BitPosition bitPos = instruction.getBitPosition();
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        int bitNumber = getBitNumber(bitPos);

        value |= (1 << bitNumber);
        setValueForRotateTarget(target, value);
        return true;
      }

      // MARK: SWAP [prefixed]
      case SWAP: {
        // 指定されたレジスタ(operand1)の上位4ビットと下位4ビットを入れ替える命令
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        int high = (value & 0xF0) >> 4;
        int low = (value & 0x0F);
        int result = (low << 4) | high;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = false;
        setValueForRotateTarget(target, result);
        return true;
      }

      // MARK: RLA, RRA, RLCA, RRCA
      case RLA: {
        // レジスタAを左に回転させる命令 (元のCフラグが回転後の1ビット目に入る)
        RotateTarget target = RotateTarget.A;
        int value = getValueForRotateTarget(target);
        boolean currentCflag = this.registers.f.carry;
        this.registers.f.carry = ((value & 0x80) != 0);
        value = (value << 1) & 0xFF;
        int result = value | (currentCflag ? 0x01 : 0);
        this.registers.f.zero = false;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        setValueForRotateTarget(target, result);
        return true;
      }

      case RLCA: {
        // レジスタAを左に回転させる命令 (Cフラグ関係なくAを回転してCフラグを判定)
        RotateTarget target = RotateTarget.A;
        int value = getValueForRotateTarget(target);
        boolean msb = (value & 0x80) != 0;
        int result = ((value << 1) | (msb ? 0x01 : 0)) & 0xFF;
        this.registers.f.zero = false;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = msb;
        setValueForRotateTarget(target, result);
        return true;
      }

      case RRA: {
        // レジスタAを右に回転させる命令 (元のCフラグが回転後の8ビット目に入る)
        RotateTarget target = RotateTarget.A;
        int value = getValueForRotateTarget(target);
        value = (this.registers.a >> 1) & 0xFF;
        if (this.registers.f.carry) {
          value |= 0x80; // キャリーがセットされている場合、MSBを1にする
        }
        this.registers.f.zero = false;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = (this.registers.a & 0x01) != 0;
        setValueForRotateTarget(target, value);
        return true;
      }

      case RRCA: {
        // レジスタAを右に回転させる命令 (Cフラグ関係なくAを回転してCフラグを判定)
        RotateTarget target = RotateTarget.A;
        int value = getValueForRotateTarget(target);
        boolean lsb = (value & 0x01) != 0;
        int result = ((value >> 1) | (lsb ? 0x80 : 0)) & 0xFF;
        this.registers.f.zero = false;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = lsb;
        setValueForRotateTarget(target, result);
        return true;
      }

      // MARK: RL, RLC, RR, RRC [prefixed]
      case RL: {
        // 指定されたレジスタ(operand1)を左に回転させる命令 (元のCフラグが回転後の1ビット目に入る)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean currentCflag = this.registers.f.carry;
        this.registers.f.carry = ((value & 0x80) != 0);
        value = (value << 1) & 0xFF;
        int result = value | (currentCflag ? 0x01 : 0);
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        setValueForRotateTarget(target, result);
        return true;
      }

      case RLC: {
        // 指定されたレジスタ(operand1)を左に回転させる命令 (Cフラグ関係なくAを回転してCフラグを判定)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean msb = (value & 0x80) != 0;
        int result = ((value << 1) | (msb ? 0x01 : 0)) & 0xFF;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = msb;
        setValueForRotateTarget(target, result);
        return true;
      }

      case RR: {
        // 指定されたレジスタ(operand1)を右に回転させる命令 (元のCフラグが回転後の8ビット目に入る)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean nextCflag = (value & 0x01) != 0;
        value = (value >> 1) & 0xFF;
        if (this.registers.f.carry) {
          value |= 0x80; // キャリーがセットされている場合、MSBを1にする
        }
        this.registers.f.zero = value == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = nextCflag;
        setValueForRotateTarget(target, value);
        return true;
      }

      case RRC: {
        // 指定されたレジスタ(operand1)を右に回転させる命令 (Cフラグ関係なくAを回転してCフラグを判定)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean lsb = (value & 0x01) != 0;
        int result = ((value >> 1) | (lsb ? 0x80 : 0)) & 0xFF;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = lsb;
        setValueForRotateTarget(target, result);
        return true;
      }

      // MARK: SLA, SRA, SRL [prefixed]
      case SLA: {
        // 指定されたレジスタ(operand1)を左に論理シフトする命令
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean msb = (value & 0x80) != 0;
        int result = (value << 1) & 0xFF;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = msb;
        setValueForRotateTarget(target, result);
        return true;
      }

      case SRA: {
        // 指定されたレジスタ(operand1)を右に算術シフトする命令 (負の値にも対応する)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean msb = (value & 0x80) != 0;
        boolean lsb = (value & 0x01) != 0;
        int result = (value >> 1) & 0xFF;
        result |= msb ? 0x80 : 0;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = lsb;
        setValueForRotateTarget(target, result);
        return true;
      }

      case SRL: {
        // 指定されたレジスタ(operand1)を右に論理シフトする命令 (符号ビットは考慮しない)
        RotateTarget target = instruction.getRotateTarget();
        int value = getValueForRotateTarget(target);
        boolean lsb = (value & 0x01) != 0;
        int result = (value >> 1) & 0xFF;
        this.registers.f.zero = result == 0;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = false;
        this.registers.f.carry = lsb;
        setValueForRotateTarget(target, result);
        return true;
      }

      // Add implementation for DAA instruction
      case DAA: {
        int a = this.registers.a;
        boolean carry = this.registers.f.carry;
        boolean halfCarry = this.registers.f.halfCarry;
        boolean subtract = this.registers.f.subtract;

        if (!subtract) {
          // after an addition, adjust if (half-)carry occurred or if result is out of bounds
          if (carry || a > 0x99) {
            a = (a + 0x60) & 0xFF;
            carry = true;
          }
          if (halfCarry || (a & 0x0F) > 0x09) {
            a = (a + 0x06) & 0xFF;
          }
        } else {
          // after a subtraction, only adjust if (half-)carry occurred
          if (carry) {
            a = (a - 0x60) & 0xFF;
          }
          if (halfCarry) {
            a = (a - 0x06) & 0xFF;
          }
        }

        this.registers.a = a;
        this.registers.f.zero = a == 0;
        // subtract flag remains unchanged
        this.registers.f.halfCarry = false;
        this.registers.f.carry = carry;

        return true;
      }

      // MARK: JP, JPHL, JR
      case JP: {
        JumpTest test = instruction.getJumpTest();
        boolean condition = testJumpCondition(test);
        int pc = jump(condition);
        this.pc = pc - 3; // 共通処理としてJP命令のバイト数分進められるため，それを考慮して引く
        return true;
      }

      case JPHL: {
        int pc = jumpHL(); //this.bus.readByte(this.registers.get_hl())
        this.pc = pc - 1; // 共通処理としてJPHL命令のバイト数分進められるため，それを考慮して引く
        return true;
      }

      case JR: {
        JumpTest test = instruction.getJumpTest();
        boolean condition = testJumpCondition(test);
        int pc = jumpRelative(condition);
        this.pc = pc - 2; // 共通処理としてJR命令のバイト数分進められるため，それを考慮して引く, 一時的にマイナスになっても確実に戻されるためwrappingしない
        return true;
      }

      // MARK: LD, LDHL
      case LD: {
        LoadTarget target = instruction.getLoadTarget();
        LoadSource source = instruction.getLoadSource();
        int sourceValue = getValueForLoadSource(source);
        setValueForLoadTarget(target, sourceValue, source == LoadSource.A);
        if (target == LoadTarget.HLI_ADDR || source == LoadSource.HLI_ADDR) {
          this.registers.set_hl(wrappingAdd(this.registers.get_hl(), 1));
        }
        if (target == LoadTarget.HLD_ADDR || source == LoadSource.HLD_ADDR) {
          this.registers.set_hl(wrappingSub(this.registers.get_hl(), 1));
        }
        return true;
      }

      case LDHL: {
        int r8 = readNextByte();
        this.registers.f.zero = false;
        this.registers.f.subtract = false;
        this.registers.f.halfCarry = ((this.sp & 0x0F) + (r8 & 0x0F)) > 0x0F;
        this.registers.f.carry = ((this.sp & 0xFF) + (r8 & 0xFF)) > 0xFF;
        if (r8 > 127) r8 = r8 - 256; // 符号付き8ビットに変換
        int result = this.sp + r8;
        this.registers.set_hl(result & 0xFFFF);
        return true;
      }

      // MARK: PUSH
      case PUSH: {
        StackTarget target = instruction.getStackTarget();
        int value;
        switch (target) {
          case BC:
            value = this.registers.get_bc();
            break;
          case DE:
            value = this.registers.get_de();
            break;
          case HL:
            value = this.registers.get_hl();
            break;
          case AF:
            value = (this.registers.a << 8) | FlagsRegister.convertToByte(this.registers.f);
            break;
          default:
            throw new IllegalArgumentException("Invalid stack target");
        }
        push(value);
        return true;
      }

      // MARK: POP
      case POP: {
        StackTarget target = instruction.getStackTarget();
        int value = pop();
        switch (target) {
          case BC:
            this.registers.set_bc(value);
            break;
          case DE:
            this.registers.set_de(value);
            break;
          case HL:
            this.registers.set_hl(value);
            break;
          case AF:
            this.registers.set_af(value);
            break;
          default:
            throw new IllegalArgumentException("Invalid stack target");
        }
        return true;
      }

      // MARK: CALL
      case CALL: {
        JumpTest test = instruction.getJumpTest();
        boolean condition = testJumpCondition(test);
        int pc = call(condition);
        this.pc = pc - 3; // 共通処理としてCALL命令のバイト数分進められるため，それを考慮して引く
        return true;
      }

      // MARK: RET
      case RET: {
        JumpTest test = instruction.getJumpTest();
        boolean condition = testJumpCondition(test);
        int pc = return_(condition); // return_メソッドは条件に基づいてPCを返す
        this.pc = pc - 1; // 共通処理としてRET命令のバイト数分進められるため，それを考慮して引く
        return true;
      }

      case RETI: {
        // TODO 未実装
        // this.interruptMasterEnable = true;
        // return pop();
        return true;
      }

      case RST: {
        // TODO 未実装
        // Integer vector = instruction.getImmediateValue();
        // push(overflowingAdd(this.pc, 1).value);
        // return vector;
        return true;
      }

      // MARK: NOP
      case NOP: {
        return true;
      }

      // MARK: HALT
      case HALT: {
        this.halted = true;
        return true;
      }

      // MARK: STOP
      case STOP: {
        // TODO 未実装
        return true;
      }

      // MARK: DI, EI
      case DI: {
        // TODO 未実装
        // this.interruptMasterEnable = false;
        return true;
      }

      case EI: {
        // TODO 未実装
        // this.interruptMasterEnable = true;
        return true;
      }

      default:
        // TODO 未実装の命令
        throw new IllegalArgumentException("Unimplemented instruction: " + instruction.getType());
    }
  }

  // MARK: *** step() ***
  public void step() {
    if (this.halted) {
      // HALT状態では命令を実行せずにPCを進める
      return;
    }
    
    // プログラムカウンタから命令を取得
    int instructionByte = this.bus.readByte(this.pc);

    // プレフィックス命令か判別
    boolean isPrefixed = instructionByte == 0xCB;

    if (isPrefixed) {
      // プレフィックス命令の場合、次のバイトを取得
      instructionByte = this.bus.readByte(this.pc + 1);
    }

    // 命令をデコード
    Instruction instruction = Instruction.fromByte(instructionByte, isPrefixed);

    if (instruction != null && instruction.isValid()) {
      // 命令を実行，実行されたかどうかを取得 (CALL / JR / RET など)
      boolean condition = execute(instruction);
      if (condition) {
        this.pc += InstructionLengthUtil.getInstructionLength(instructionByte, isPrefixed);
      }

      int cycles = InstructionLengthUtil.getInstructionCycles(instructionByte, isPrefixed, condition);


      log("Cycles taken: " + cycles);
    } else {
      // Handle invalid instruction
      throw new IllegalArgumentException("Invalid instruction: " + String.format("%02X", instructionByte) + " isPrefixed: " + isPrefixed);
    }
  }

  // MARK: *** 命令関数 ***

  // MARK: add()
  int addA(int value) {
    OverflowingAddResult result = overflowingAdd(this.registers.a, value);

    this.registers.f.zero = result.value == 0x00;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = ((this.registers.a & 0x0F) + (value & 0x0F)) > 0x0F;
    this.registers.f.carry = result.overflow;
    return result.value;
  }

  int addHL(int value) {
    int hl = this.registers.get_hl();
    int result = overflowingAdd(hl, value).value;
    boolean overflow = overflowingAdd(hl, value).overflow;

    this.registers.f.subtract = false;
    this.registers.f.halfCarry = ((hl & 0x0FFF) + (value & 0x0FFF)) > 0x0FFF;
    this.registers.f.carry = overflow;

    return result & 0xFFFF;
  }

  int addSP(int value) {
    // 16ビット演算を確実に行うため、直接計算
    int result = (this.sp + value) & 0xFFFF;
    
    // フラグを設定（zeroとsubtractはADDSP命令では常にfalse）
    this.registers.f.zero = false;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = (this.sp & 0x0F) + (value & 0x0F) > 0x0F;
    this.registers.f.carry = (this.sp & 0xFF) + (value & 0xFF) > 0xFF;
    
    return result;
  }

  int addWithCarry(int value, int carry) {
    // キャリーを含めて一度に計算
    int result = this.registers.a + value + carry;
    
    // フラグ設定
    this.registers.f.zero = (result & 0xFF) == 0;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = ((this.registers.a & 0x0F) + (value & 0x0F) + carry) > 0x0F;
    this.registers.f.carry = result > 0xFF;
    
    return result & 0xFF;
  }
  
  // MARK: subtract()
  int subtract(int value) {
    this.registers.f.zero = this.registers.a == value;
    this.registers.f.subtract = true;
    this.registers.f.halfCarry = (this.registers.a & 0x0F) < (value & 0x0F);
    this.registers.f.carry = this.registers.a < value;
    return (this.registers.a - value) & 0xFF;
  }
  
  int subtractWithCarry(int value, int carry) {
    int total = this.registers.a - value - carry;
    this.registers.f.zero = (total & 0xFF) == 0;
    this.registers.f.subtract = true;
    this.registers.f.halfCarry = ((this.registers.a & 0x0F) - (value & 0x0F) - carry) < 0;
    this.registers.f.carry = total < 0;
    return total & 0xFF;
  }
  
  // MARK: bitwise operations
  int and(int value) {
    int result = this.registers.a & value;
    this.registers.f.zero = result == 0;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = true;
    this.registers.f.carry = false;
    return result;
  }
  
  int or(int value) {
    int result = this.registers.a | value;
    this.registers.f.zero = (result & 0xFF) == 0x00;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = false;
    this.registers.f.carry = false;
    return result;
  }
  
  int xor(int value) {
    int result = this.registers.a ^ value;
    this.registers.f.zero = (result & 0xFF) == 0x00;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = false;
    this.registers.f.carry = false;
    return result;
  }
  
  // MARK: cp()
  void cp(int value) {
    this.registers.f.zero = this.registers.a == value;
    this.registers.f.subtract = true;
    this.registers.f.halfCarry = (this.registers.a & 0x0F) < (value & 0x0F);
    this.registers.f.carry = this.registers.a < value;
  }
  
  // MARK: increment/decrement
  int increment(int value) {
    int result = (value + 1) & 0xFF;
    this.registers.f.zero = result == 0;
    this.registers.f.subtract = false;
    this.registers.f.halfCarry = (value & 0x0F) == 0x0F;
    return result;
  }
  
  int decrement(int value) {
    int result = wrappingSub(value, 1) & 0xFF;
    this.registers.f.zero = result == 0;
    this.registers.f.subtract = true;
    this.registers.f.halfCarry = (value & 0x0F) == 0x00;
    return result;
  }

  // MARK: jump()
  int jump(boolean condition) {
    if (condition) {
      return readNextWord(); // 次のワード(2byte)を読み込む
    } else {
      return overflowingAdd(this.pc, 3).value; // 3バイト足す
    }
  }
 
  // MARK: jumpHL()
  int jumpHL() {
    return this.bus.readByte(this.registers.get_hl()); // HLレジスタの値がジャンプ先アドレス
  }
  
  // MARK: jumpRelative()
  int jumpRelative(boolean condition) {
    if (condition) {
      int offset = readNextByte();
      // 符号付き8ビットとして扱う
      if ((offset & 0x80) != 0) offset -= 256;
      return wrappingAdd16(wrappingAdd16(this.pc, 2), offset);
    } else {
      return wrappingAdd16(this.pc, 2); // 2バイト足す
    }
  }

  // MARK: push()
  void push(int value) {
    this.sp = wrappingSub16(this.sp, 1); // スタックポインタを1バイト分減らす
    this.bus.writeByte(this.sp, ((value & 0xFF00) >> 8)); // スタックに上位バイトを書き込む
    this.sp = wrappingSub16(this.sp, 1); // スタックポインタを1バイト分減らす
    this.bus.writeByte(this.sp, (value & 0x00FF)); // スタックに下位バイトを書き込む
  }

  // MARK: pop()
  int pop() {
    int lsb = this.bus.readByte(this.sp); // スタックから下位バイトを読み込む
    this.sp = overflowingAdd(this.sp, 1).value; // スタックポインタを1バイト分増やす
    int msb = this.bus.readByte(this.sp); // スタックから上位バイトを読み込む
    this.sp = overflowingAdd(this.sp, 1).value; // スタックポインタを1バイト分増やす
    return ((msb << 8) | lsb); // リトルエンディアンで結合
  }

  // MARK: call()
  int call(boolean condition) {
    int nextPc = overflowingAdd(this.pc, 3).value; // 次のPCのアドレスを計算
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
      return overflowingAdd(this.pc, 1).value; // PCを1バイト進める
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

    // MARK: ヘルパー関数群

  // MARK: get ArithmeticTarget
  private int getValueForArithmeticTarget(ArithmeticTarget target) {
    // アドレスから算術演算のターゲットの値を取得
    switch (target) {
      case A: return this.registers.a;
      case B: return this.registers.b;
      case C: return this.registers.c;
      case D: return this.registers.d;
      case E: return this.registers.e;
      case H: return this.registers.h;
      case L: return this.registers.l;
      case HL_ADDR: return this.bus.readByte(this.registers.get_hl());
      case D8: return readNextByte();
      default:
        throw new IllegalArgumentException("Invalid arithmetic target: " + target);
    }
  }
  
  // MARK: set ArithmeticTarget
  private void setValueForArithmeticTarget(ArithmeticTarget target, int value) {
    // 算術演算のターゲットに値を設定
    switch (target) {
      case A:
        this.registers.a = value;
        break;
      case B:
        this.registers.b = value;
        break;
      case C:
        this.registers.c = value;
        break;
      case D:
        this.registers.d = value;
        break;
      case E:
        this.registers.e = value;
        break;
      case H:
        this.registers.h = value;
        break;
      case L:
        this.registers.l = value;
        break;
      case HL_ADDR:
        this.bus.writeByte(this.registers.get_hl(), value);
        break;
      default:
        throw new IllegalArgumentException("Cannot set value for target: " + target);
    }
  }

  // MARK: get RotateTarget
  private int getValueForRotateTarget(RotateTarget target) {
    // 回転操作ターゲットの値を取得
    switch (target) {
      case A: return this.registers.a;
      case B: return this.registers.b;
      case C: return this.registers.c;
      case D: return this.registers.d;
      case E: return this.registers.e;
      case H: return this.registers.h;
      case L: return this.registers.l;
      case HL_ADDR: return this.bus.readByte(this.registers.get_hl());
      default: throw new IllegalArgumentException("Invalid rotate target: " + target);
    }
  }

  // MARK: set RotateTarget
  private void setValueForRotateTarget(RotateTarget target, int value) {
    // 回転操作ターゲットに値を設定
    switch (target) {
      case A:
        this.registers.a = value;
        break;
      case B:
        this.registers.b = value;
        break;
      case C:
        this.registers.c = value;
        break;
      case D:
        this.registers.d = value;
        break;
      case E:
        this.registers.e = value;
        break;
      case H:
        this.registers.h = value;
        break;
      case L:
        this.registers.l = value;
        break;
      case HL_ADDR:
        this.bus.writeByte(this.registers.get_hl(), value);
        break;
    }
  }

  // MARK: get 16bit RegisterPair
  private int getValueForRegisterPair(RegisterPair pair) {
    // レジスタペアの値を取得
    switch (pair) {
      case BC: return this.registers.get_bc();
      case DE: return this.registers.get_de();
      case HL: return this.registers.get_hl();
      case SP: return this.sp;
      default:
        throw new IllegalArgumentException("Invalid register pair: " + pair);
    }
  }

  // MARK: set 16bit RegisterPair
  private void setValueForRegisterPair(RegisterPair pair, int value) {
    // レジスタペアに値を設定
    switch (pair) {
      case BC:
        this.registers.set_bc(value);
        break;
      case DE:
        this.registers.set_de(value);
        break;
      case HL:
        this.registers.set_hl(value);
        break;
      case SP:
        this.sp = value;
        break;
    }
  }

  // MARK: get LoadTarget
  private void setValueForLoadTarget(LoadTarget target, int value, boolean isByte) {
    // ロードターゲットに値を設定
    if (isByte) {
      value &= 0xFF; // 8ビットに制限
    } else {
      value &= 0xFFFF; // 16ビットに制限
    }

    switch (target) {
      case A:
        this.registers.a = value & 0xFF;
        break;
      case B:
        this.registers.b = value & 0xFF;
        break;
      case C:
        this.registers.c = value & 0xFF;
        break;
      case D:
        this.registers.d = value & 0xFF;
        break;
      case E:
        this.registers.e = value & 0xFF;
        break;
      case H:
        this.registers.h = value & 0xFF;
        break;
      case L:
        this.registers.l = value & 0xFF;
        break;
      case BC:
        this.registers.set_bc(value);
        break;
      case DE:
        this.registers.set_de(value);
        break;
      case HL:
        this.registers.set_hl(value);
        break;
      case SP:
        this.sp = value;
        break;
      case BC_ADDR:
        this.bus.writeByte(this.registers.get_bc(), value);
        break;
      case DE_ADDR:
        this.bus.writeByte(this.registers.get_de(), value);
        break;
      case HL_ADDR:
        this.bus.writeByte(this.registers.get_hl(), value);
        break;
      case HLI_ADDR:
        this.bus.writeByte(this.registers.get_hl(), value);
        break;
      case HLD_ADDR:
        this.bus.writeByte(this.registers.get_hl(), value);
        break;
      case A16_ADDR:
        int address = readNextWord();
        if (isByte) {
          this.bus.writeByte(address, value & 0xFF);
        } else {
          this.bus.writeWord(address, value);
        }
        break;
      case FF00_A8:
        int offset = readNextByte();
        this.bus.writeByte(0xFF00 + offset, value);
        break;
      case FF00_C:
        this.bus.writeByte(0xFF00 + this.registers.c, value);
        break;
    }
  }

  // MARK: get LoadSource
  private int getValueForLoadSource(LoadSource source) {
    // ロードソースから値を取得
    switch (source) {
      case A: return this.registers.a;
      case B: return this.registers.b;
      case C: return this.registers.c;
      case D: return this.registers.d;
      case E: return this.registers.e;
      case H: return this.registers.h;
      case L: return this.registers.l;
      case BC: return this.registers.get_bc();
      case DE: return this.registers.get_de();
      case HL: return this.registers.get_hl();
      case SP: return this.sp;
      case BC_ADDR: return this.bus.readByte(this.registers.get_bc());
      case DE_ADDR: return this.bus.readByte(this.registers.get_de());
      case HL_ADDR: return this.bus.readByte(this.registers.get_hl());
      case HLI_ADDR: return this.bus.readByte(this.registers.get_hl());
      case HLD_ADDR: return this.bus.readByte(this.registers.get_hl());
      case A16_ADDR: {
        int address = readNextWord();
        return this.bus.readByte(address);
      }
      case FF00_A8: {
        int offset = readNextByte();
        return this.bus.readByte(0xFF00 + offset);
      }
      case FF00_C: {
        return this.bus.readByte(0xFF00 + this.registers.c);
      }
      case D8: return readNextByte();
      case D16: return readNextWord();
      default:
        throw new IllegalArgumentException("Invalid load source: " + source);
    }
  }


  // MARK: get BitPosition
  private int getBitNumber(BitPosition bitPos) {
    // ビット位置を数値に変換
    switch (bitPos) {
      case BIT0: return 0;
      case BIT1: return 1;
      case BIT2: return 2;
      case BIT3: return 3;
      case BIT4: return 4;
      case BIT5: return 5;
      case BIT6: return 6;
      case BIT7: return 7;
      default:
        throw new IllegalArgumentException("Invalid bit position: " + bitPos);
    }
  }

  // MARK: get JumpTest
  private boolean testJumpCondition(JumpTest test) {
    // ジャンプ条件のテスト
    switch (test) {
      case NotZero: return !this.registers.f.zero;
      case Zero: return this.registers.f.zero;
      case NotCarry: return !this.registers.f.carry;
      case Carry: return this.registers.f.carry;
      case Always: return true;
      default:
        throw new IllegalArgumentException("Invalid jump test: " + test);
    }
  }

  public void log(String message) {
    System.out.println(message);
  }
}
