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
  public ArithmeticTarget getArithmeticTarget() {
    return (ArithmeticTarget)operand0;
  }

  public JumpTest getJumpTest() {
    return (JumpTest)operand0;
  }

  public LoadTarget getLoadTarget() {
    return (LoadTarget)operand0;
  }

  public LoadSource getLoadSource() {
    return (LoadSource)operand1;
  }

  public RegisterPair getRegisterPair() {
    return (RegisterPair)operand0;
  }

  public StackTarget getStackTarget() {
    return (StackTarget)operand0;
  }
  
  // SWAP系とBIT系でターゲット位置を同じにするためにoperand0/1を反転して受け取る
  public BitPosition getBitPosition() {
    return (BitPosition)operand1;
  }

  public RotateTarget getRotateTarget() {
    return (RotateTarget)operand0;
  }

  public Integer getImmediateValue() {
    return (Integer)operand0;
  }

  // MARK: ファクトリーメソッド
  public static Instruction createADD(ArithmeticTarget target) {
    return new Instruction(InstructionType.ADD, target, null);
  }
  
  public static Instruction createADDHL(RegisterPair pair) {
    return new Instruction(InstructionType.ADDHL, pair, null);
  }
  
  public static Instruction createADDSP(Integer value) {
    return new Instruction(InstructionType.ADDSP, value, null);
  }

  public static Instruction createADC(ArithmeticTarget target) {
    return new Instruction(InstructionType.ADC, target, null);
  }

  public static Instruction createSUB(ArithmeticTarget target) {
    return new Instruction(InstructionType.SUB, target, null);
  }

  public static Instruction createSBC(ArithmeticTarget target) {
    return new Instruction(InstructionType.SBC, target, null);
  }

  public static Instruction createAND(ArithmeticTarget target) {
    return new Instruction(InstructionType.AND, target, null);
  }

  public static Instruction createOR(ArithmeticTarget target) {
    return new Instruction(InstructionType.OR, target, null);
  }

  public static Instruction createXOR(ArithmeticTarget target) {
    return new Instruction(InstructionType.XOR, target, null);
  }

  public static Instruction createCP(ArithmeticTarget target) {
    return new Instruction(InstructionType.CP, target, null);
  }

  public static Instruction createINC(ArithmeticTarget target) {
    return new Instruction(InstructionType.INC, target, null);
  }

  public static Instruction createINCRP(RegisterPair pair) {
    return new Instruction(InstructionType.INCRP, pair, null);
  }

  public static Instruction createDEC(ArithmeticTarget target) {
    return new Instruction(InstructionType.DEC, target, null);
  }

  public static Instruction createDECRP(RegisterPair pair) {
    return new Instruction(InstructionType.DECRP, pair, null);
  }

  // フラグ操作命令
  public static Instruction createCCF() {
    return new Instruction(InstructionType.CCF, null, null);
  }

  public static Instruction createSCF() {
    return new Instruction(InstructionType.SCF, null, null);
  }

  public static Instruction createRRA() {
    return new Instruction(InstructionType.RRA, null, null);
  }

  public static Instruction createRLA() {
    return new Instruction(InstructionType.RLA, null, null);
  }

  public static Instruction createRRCA() {
    return new Instruction(InstructionType.RRCA, null, null);
  }

  public static Instruction createRLCA() {
    return new Instruction(InstructionType.RLCA, null, null);
  }

  public static Instruction createCPL() {
    return new Instruction(InstructionType.CPL, null, null);
  }

  public static Instruction createDAA() {
    return new Instruction(InstructionType.DAA, null, null);
  }

  public static Instruction createDI() {
    return new Instruction(InstructionType.DI, null, null);
  }

  public static Instruction createEI() {
    return new Instruction(InstructionType.EI, null, null);
  }

  // ビット操作命令 (CBプレフィックス命令)
  public static Instruction createBIT(BitPosition bit, RotateTarget target) {
    return new Instruction(InstructionType.BIT, target, bit);
  }

  public static Instruction createRES(BitPosition bit, RotateTarget target) {
    return new Instruction(InstructionType.RES, target, bit);
  }

  public static Instruction createSET(BitPosition bit, RotateTarget target) {
    return new Instruction(InstructionType.SET, target, bit);
  }

  public static Instruction createSRL(RotateTarget target) {
    return new Instruction(InstructionType.SRL, target, null);
  }

  public static Instruction createRR(RotateTarget target) {
    return new Instruction(InstructionType.RR, target, null);
  }

  public static Instruction createRL(RotateTarget target) {
    return new Instruction(InstructionType.RL, target, null);
  }

  public static Instruction createRRC(RotateTarget target) {
    return new Instruction(InstructionType.RRC, target, null);
  }

  public static Instruction createRLC(RotateTarget target) {
    return new Instruction(InstructionType.RLC, target, null);
  }

  public static Instruction createSRA(RotateTarget target) {
    return new Instruction(InstructionType.SRA, target, null);
  }

  public static Instruction createSLA(RotateTarget target) {
    return new Instruction(InstructionType.SLA, target, null);
  }

  public static Instruction createSWAP(RotateTarget target) {
    return new Instruction(InstructionType.SWAP, target, null);
  }

  // ジャンプ命令
  public static Instruction createJP(JumpTest test) {
    return new Instruction(InstructionType.JP, test, null);
  }

  public static Instruction createJPHL() {
    return new Instruction(InstructionType.JPHL, null, null);
  }

  public static Instruction createJR(JumpTest test) {
    return new Instruction(InstructionType.JR, test, null);
  }

  // ロード命令
  public static Instruction createLD(LoadTarget target, LoadSource source) {
    return new Instruction(InstructionType.LD, target, source);
  }

  public static Instruction createLDHL(Integer value) {
    return new Instruction(InstructionType.LDHL, value, null);
  }

  // スタック操作命令
  public static Instruction createPUSH(StackTarget target) {
    return new Instruction(InstructionType.PUSH, target, null);
  }

  public static Instruction createPOP(StackTarget target) {
    return new Instruction(InstructionType.POP, target, null);
  }

  // サブルーチン命令
  public static Instruction createCALL(JumpTest test) {
    return new Instruction(InstructionType.CALL, test, null);
  }

  public static Instruction createRET(JumpTest test) {
    return new Instruction(InstructionType.RET, test, null);
  }

  public static Instruction createRETI() {
    return new Instruction(InstructionType.RETI, null, null);
  }

  public static Instruction createRST(Integer vector) {
    return new Instruction(InstructionType.RST, vector, null);
  }

  // その他の命令
  public static Instruction createNOP() {
    return new Instruction(InstructionType.NOP, null, null);
  }

  public static Instruction createHALT() {
    return new Instruction(InstructionType.HALT, null, null);
  }

  public static Instruction createSTOP() {
    return new Instruction(InstructionType.STOP, null, null);
  }

  // MARK: 命令デコード
  public static Instruction fromByte(int instructionByte, boolean isPrefixed) {
    if (isPrefixed) {
      return fromBytePrefixed(instructionByte);
    } else {
      return fromByteNotPrefixed(instructionByte);
    }
  }

  // MARK: プレフィックス命令 (0xCBプレフィックス)
  private static Instruction fromBytePrefixed(int instructionByte) {
    /*
     * プレフィックス命令は、最初の2ビットで命令の種類を決定し、次の3ビットでビット位置を、下位3ビットでターゲットレジスタを決定します。
     * 
     * [参考]
     * http://gbdev.io/pandocs/CPU_Instruction_Set.html#cb-prefix-instructions
     */
    // ビット位置とターゲットレジスタを計算
    int bitPosition = (instructionByte >> 3) & 0x07;
    int targetRegister = instructionByte & 0x07;
    RotateTarget target = mapToRotateTarget(targetRegister);
    BitPosition bit = mapToBitPosition(bitPosition);
    
    // 命令の種類を決定
    int instructionType = instructionByte >> 6;
    
    switch (instructionType) {
      case 0x00: // 回転・シフト命令
        switch (bitPosition) {
          case 0: return createRLC(target);
          case 1: return createRRC(target);
          case 2: return createRL(target);
          case 3: return createRR(target);
          case 4: return createSLA(target);
          case 5: return createSRA(target);
          case 6: return createSWAP(target);
          case 7: return createSRL(target);
        }
        break;
      case 0x01: // BIT命令
        return createBIT(bit, target);
      case 0x02: // RES命令
        return createRES(bit, target);
      case 0x03: // SET命令
        return createSET(bit, target);
    }
    
    throw new IllegalArgumentException("Invalid instruction byte: " + instructionByte);
  }

  // MARK: 通常命令
  private static Instruction fromByteNotPrefixed(int instructionByte) {
    switch (instructionByte) {
      // MARK: NOP
      case 0x00: return createNOP();
      
      // MARK: LD rr,d16
      case 0x01: return createLD(LoadTarget.BC, LoadSource.D16);
      case 0x11: return createLD(LoadTarget.DE, LoadSource.D16);
      case 0x21: return createLD(LoadTarget.HL, LoadSource.D16);
      case 0x31: return createLD(LoadTarget.SP, LoadSource.D16);
      // MARK: LD (BC),A / LD (DE),A
      case 0x02: return createLD(LoadTarget.BC_ADDR, LoadSource.A);
      case 0x12: return createLD(LoadTarget.DE_ADDR, LoadSource.A);
      
      // MARK: LD LD A,(BC) / LD A,(DE)
      case 0x0A: return createLD(LoadTarget.A, LoadSource.BC_ADDR);
      case 0x1A: return createLD(LoadTarget.A, LoadSource.DE_ADDR);
      
      // MARK: LD LD (HL+),A / LD (HL-),A
      case 0x22: return createLD(LoadTarget.HLI_ADDR, LoadSource.A);
      case 0x32: return createLD(LoadTarget.HLD_ADDR, LoadSource.A);
      
      // MARK: LD LD A,(HL+) / LD A,(HL-)
      case 0x2A: return createLD(LoadTarget.A, LoadSource.HLI_ADDR);
      case 0x3A: return createLD(LoadTarget.A, LoadSource.HLD_ADDR);

      // MARK: LD (a16),SP
      case 0x08: return createLD(LoadTarget.A16_ADDR, LoadSource.SP);

      // MARK: LD LD (a16),A / LD A,(a16)
      case 0xEA: return createLD(LoadTarget.A16_ADDR, LoadSource.A);
      case 0xFA: return createLD(LoadTarget.A, LoadSource.A16_ADDR);
      
      // MARK: LD LDH (a8),A / LDH A,(a8)
      case 0xE0: return createLD(LoadTarget.FF00_A8, LoadSource.A);
      case 0xF0: return createLD(LoadTarget.A, LoadSource.FF00_A8);
      
      // MARK: LD LDH (C),A / LDH A,(C)
      case 0xE2: return createLD(LoadTarget.FF00_C, LoadSource.A);
      case 0xF2: return createLD(LoadTarget.A, LoadSource.FF00_C);
      
      // MARK: LD LD r,r'
      case 0x40: return createLD(LoadTarget.B, LoadSource.B);
      case 0x41: return createLD(LoadTarget.B, LoadSource.C);
      case 0x42: return createLD(LoadTarget.B, LoadSource.D);
      case 0x43: return createLD(LoadTarget.B, LoadSource.E);
      case 0x44: return createLD(LoadTarget.B, LoadSource.H);
      case 0x45: return createLD(LoadTarget.B, LoadSource.L);
      case 0x46: return createLD(LoadTarget.B, LoadSource.HL_ADDR);
      case 0x47: return createLD(LoadTarget.B, LoadSource.A);
      
      case 0x48: return createLD(LoadTarget.C, LoadSource.B);
      case 0x49: return createLD(LoadTarget.C, LoadSource.C);
      case 0x4A: return createLD(LoadTarget.C, LoadSource.D);
      case 0x4B: return createLD(LoadTarget.C, LoadSource.E);
      case 0x4C: return createLD(LoadTarget.C, LoadSource.H);
      case 0x4D: return createLD(LoadTarget.C, LoadSource.L);
      case 0x4E: return createLD(LoadTarget.C, LoadSource.HL_ADDR);
      case 0x4F: return createLD(LoadTarget.C, LoadSource.A);
      
      case 0x50: return createLD(LoadTarget.D, LoadSource.B);
      case 0x51: return createLD(LoadTarget.D, LoadSource.C);
      case 0x52: return createLD(LoadTarget.D, LoadSource.D);
      case 0x53: return createLD(LoadTarget.D, LoadSource.E);
      case 0x54: return createLD(LoadTarget.D, LoadSource.H);
      case 0x55: return createLD(LoadTarget.D, LoadSource.L);
      case 0x56: return createLD(LoadTarget.D, LoadSource.HL_ADDR);
      case 0x57: return createLD(LoadTarget.D, LoadSource.A);
      
      case 0x58: return createLD(LoadTarget.E, LoadSource.B);
      case 0x59: return createLD(LoadTarget.E, LoadSource.C);
      case 0x5A: return createLD(LoadTarget.E, LoadSource.D);
      case 0x5B: return createLD(LoadTarget.E, LoadSource.E);
      case 0x5C: return createLD(LoadTarget.E, LoadSource.H);
      case 0x5D: return createLD(LoadTarget.E, LoadSource.L);
      case 0x5E: return createLD(LoadTarget.E, LoadSource.HL_ADDR);
      case 0x5F: return createLD(LoadTarget.E, LoadSource.A);
      
      case 0x60: return createLD(LoadTarget.H, LoadSource.B);
      case 0x61: return createLD(LoadTarget.H, LoadSource.C);
      case 0x62: return createLD(LoadTarget.H, LoadSource.D);
      case 0x63: return createLD(LoadTarget.H, LoadSource.E);
      case 0x64: return createLD(LoadTarget.H, LoadSource.H);
      case 0x65: return createLD(LoadTarget.H, LoadSource.L);
      case 0x66: return createLD(LoadTarget.H, LoadSource.HL_ADDR);
      case 0x67: return createLD(LoadTarget.H, LoadSource.A);
      
      case 0x68: return createLD(LoadTarget.L, LoadSource.B);
      case 0x69: return createLD(LoadTarget.L, LoadSource.C);
      case 0x6A: return createLD(LoadTarget.L, LoadSource.D);
      case 0x6B: return createLD(LoadTarget.L, LoadSource.E);
      case 0x6C: return createLD(LoadTarget.L, LoadSource.H);
      case 0x6D: return createLD(LoadTarget.L, LoadSource.L);
      case 0x6E: return createLD(LoadTarget.L, LoadSource.HL_ADDR);
      case 0x6F: return createLD(LoadTarget.L, LoadSource.A);
      
      case 0x70: return createLD(LoadTarget.HL_ADDR, LoadSource.B);
      case 0x71: return createLD(LoadTarget.HL_ADDR, LoadSource.C);
      case 0x72: return createLD(LoadTarget.HL_ADDR, LoadSource.D);
      case 0x73: return createLD(LoadTarget.HL_ADDR, LoadSource.E);
      case 0x74: return createLD(LoadTarget.HL_ADDR, LoadSource.H);
      case 0x75: return createLD(LoadTarget.HL_ADDR, LoadSource.L);
      case 0x77: return createLD(LoadTarget.HL_ADDR, LoadSource.A);
      
      case 0x78: return createLD(LoadTarget.A, LoadSource.B);
      case 0x79: return createLD(LoadTarget.A, LoadSource.C);
      case 0x7A: return createLD(LoadTarget.A, LoadSource.D);
      case 0x7B: return createLD(LoadTarget.A, LoadSource.E);
      case 0x7C: return createLD(LoadTarget.A, LoadSource.H);
      case 0x7D: return createLD(LoadTarget.A, LoadSource.L);
      case 0x7E: return createLD(LoadTarget.A, LoadSource.HL_ADDR);
      case 0x7F: return createLD(LoadTarget.A, LoadSource.A);
      
      // MARK: LD r,d8
      case 0x06: return createLD(LoadTarget.B, LoadSource.D8);
      case 0x0E: return createLD(LoadTarget.C, LoadSource.D8);
      case 0x16: return createLD(LoadTarget.D, LoadSource.D8);
      case 0x1E: return createLD(LoadTarget.E, LoadSource.D8);
      case 0x26: return createLD(LoadTarget.H, LoadSource.D8);
      case 0x2E: return createLD(LoadTarget.L, LoadSource.D8);
      case 0x36: return createLD(LoadTarget.HL_ADDR, LoadSource.D8);
      case 0x3E: return createLD(LoadTarget.A, LoadSource.D8);
      
      // MARK: INC r
      case 0x04: return createINC(ArithmeticTarget.B);
      case 0x0C: return createINC(ArithmeticTarget.C);
      case 0x14: return createINC(ArithmeticTarget.D);
      case 0x1C: return createINC(ArithmeticTarget.E);
      case 0x24: return createINC(ArithmeticTarget.H);
      case 0x2C: return createINC(ArithmeticTarget.L);
      case 0x34: return createINC(ArithmeticTarget.HL_ADDR);
      case 0x3C: return createINC(ArithmeticTarget.A);
      
      // MARK: DEC r
      case 0x05: return createDEC(ArithmeticTarget.B);
      case 0x0D: return createDEC(ArithmeticTarget.C);
      case 0x15: return createDEC(ArithmeticTarget.D);
      case 0x1D: return createDEC(ArithmeticTarget.E);
      case 0x25: return createDEC(ArithmeticTarget.H);
      case 0x2D: return createDEC(ArithmeticTarget.L);
      case 0x35: return createDEC(ArithmeticTarget.HL_ADDR);
      case 0x3D: return createDEC(ArithmeticTarget.A);
      
      // MARK: INC rr
      case 0x03: return createINCRP(RegisterPair.BC);
      case 0x13: return createINCRP(RegisterPair.DE);
      case 0x23: return createINCRP(RegisterPair.HL);
      case 0x33: return createINCRP(RegisterPair.SP);
      
      // MARK: DEC rr
      case 0x0B: return createDECRP(RegisterPair.BC);
      case 0x1B: return createDECRP(RegisterPair.DE);
      case 0x2B: return createDECRP(RegisterPair.HL);
      case 0x3B: return createDECRP(RegisterPair.SP);
      
      // MARK: ADD A,r
      case 0x80: return createADD(ArithmeticTarget.B);
      case 0x81: return createADD(ArithmeticTarget.C);
      case 0x82: return createADD(ArithmeticTarget.D);
      case 0x83: return createADD(ArithmeticTarget.E);
      case 0x84: return createADD(ArithmeticTarget.H);
      case 0x85: return createADD(ArithmeticTarget.L);
      case 0x86: return createADD(ArithmeticTarget.HL_ADDR);
      case 0x87: return createADD(ArithmeticTarget.A);
      case 0xC6: return createADD(ArithmeticTarget.D8);
      
      // MARK: ADC A,r
      case 0x88: return createADC(ArithmeticTarget.B);
      case 0x89: return createADC(ArithmeticTarget.C);
      case 0x8A: return createADC(ArithmeticTarget.D);
      case 0x8B: return createADC(ArithmeticTarget.E);
      case 0x8C: return createADC(ArithmeticTarget.H);
      case 0x8D: return createADC(ArithmeticTarget.L);
      case 0x8E: return createADC(ArithmeticTarget.HL_ADDR);
      case 0x8F: return createADC(ArithmeticTarget.A);
      case 0xCE: return createADC(ArithmeticTarget.D8);
      
      // MARK: SUB r
      case 0x90: return createSUB(ArithmeticTarget.B);
      case 0x91: return createSUB(ArithmeticTarget.C);
      case 0x92: return createSUB(ArithmeticTarget.D);
      case 0x93: return createSUB(ArithmeticTarget.E);
      case 0x94: return createSUB(ArithmeticTarget.H);
      case 0x95: return createSUB(ArithmeticTarget.L);
      case 0x96: return createSUB(ArithmeticTarget.HL_ADDR);
      case 0x97: return createSUB(ArithmeticTarget.A);
      case 0xD6: return createSUB(ArithmeticTarget.D8);
      
      // MARK: SBC A,r
      case 0x98: return createSBC(ArithmeticTarget.B);
      case 0x99: return createSBC(ArithmeticTarget.C);
      case 0x9A: return createSBC(ArithmeticTarget.D);
      case 0x9B: return createSBC(ArithmeticTarget.E);
      case 0x9C: return createSBC(ArithmeticTarget.H);
      case 0x9D: return createSBC(ArithmeticTarget.L);
      case 0x9E: return createSBC(ArithmeticTarget.HL_ADDR);
      case 0x9F: return createSBC(ArithmeticTarget.A);
      case 0xDE: return createSBC(ArithmeticTarget.D8);
      
      // MARK: AND r
      case 0xA0: return createAND(ArithmeticTarget.B);
      case 0xA1: return createAND(ArithmeticTarget.C);
      case 0xA2: return createAND(ArithmeticTarget.D);
      case 0xA3: return createAND(ArithmeticTarget.E);
      case 0xA4: return createAND(ArithmeticTarget.H);
      case 0xA5: return createAND(ArithmeticTarget.L);
      case 0xA6: return createAND(ArithmeticTarget.HL_ADDR);
      case 0xA7: return createAND(ArithmeticTarget.A);
      case 0xE6: return createAND(ArithmeticTarget.D8);
      
      // MARK: XOR r
      case 0xA8: return createXOR(ArithmeticTarget.B);
      case 0xA9: return createXOR(ArithmeticTarget.C);
      case 0xAA: return createXOR(ArithmeticTarget.D);
      case 0xAB: return createXOR(ArithmeticTarget.E);
      case 0xAC: return createXOR(ArithmeticTarget.H);
      case 0xAD: return createXOR(ArithmeticTarget.L);
      case 0xAE: return createXOR(ArithmeticTarget.HL_ADDR);
      case 0xAF: return createXOR(ArithmeticTarget.A);
      case 0xEE: return createXOR(ArithmeticTarget.D8);
      
      // MARK: OR r
      case 0xB0: return createOR(ArithmeticTarget.B);
      case 0xB1: return createOR(ArithmeticTarget.C);
      case 0xB2: return createOR(ArithmeticTarget.D);
      case 0xB3: return createOR(ArithmeticTarget.E);
      case 0xB4: return createOR(ArithmeticTarget.H);
      case 0xB5: return createOR(ArithmeticTarget.L);
      case 0xB6: return createOR(ArithmeticTarget.HL_ADDR);
      case 0xB7: return createOR(ArithmeticTarget.A);
      case 0xF6: return createOR(ArithmeticTarget.D8);
      
      // MARK: CP r
      case 0xB8: return createCP(ArithmeticTarget.B);
      case 0xB9: return createCP(ArithmeticTarget.C);
      case 0xBA: return createCP(ArithmeticTarget.D);
      case 0xBB: return createCP(ArithmeticTarget.E);
      case 0xBC: return createCP(ArithmeticTarget.H);
      case 0xBD: return createCP(ArithmeticTarget.L);
      case 0xBE: return createCP(ArithmeticTarget.HL_ADDR);
      case 0xBF: return createCP(ArithmeticTarget.A);
      case 0xFE: return createCP(ArithmeticTarget.D8);
      
      // MARK: ADD HL,rr
      case 0x09: return createADDHL(RegisterPair.BC);
      case 0x19: return createADDHL(RegisterPair.DE);
      case 0x29: return createADDHL(RegisterPair.HL);
      case 0x39: return createADDHL(RegisterPair.SP);
      
      // MARK: ADD SP,r8
      case 0xE8: return createADDSP(null); // 実際の値はCPUのexecuteで取得
      
      // LD HL,SP+r8
      case 0xF8: return createLDHL(null); // 実際の値はCPUのexecuteで取得
      case 0xF9: return createLD(LoadTarget.SP, LoadSource.HL);
      
      // MARK: JP cc,a16
      case 0xC3: return createJP(JumpTest.Always);
      case 0xC2: return createJP(JumpTest.NotZero);
      case 0xCA: return createJP(JumpTest.Zero);
      case 0xD2: return createJP(JumpTest.NotCarry);
      case 0xDA: return createJP(JumpTest.Carry);
      
      // MARK: JP HL
      case 0xE9: return createJPHL();
      
      // MARK: JR cc,r8
      case 0x18: return createJR(JumpTest.Always);
      case 0x20: return createJR(JumpTest.NotZero);
      case 0x28: return createJR(JumpTest.Zero);
      case 0x30: return createJR(JumpTest.NotCarry);
      case 0x38: return createJR(JumpTest.Carry);
      
      // MARK: CALL cc,a16
      case 0xCD: return createCALL(JumpTest.Always);
      case 0xC4: return createCALL(JumpTest.NotZero);
      case 0xCC: return createCALL(JumpTest.Zero);
      case 0xD4: return createCALL(JumpTest.NotCarry);
      case 0xDC: return createCALL(JumpTest.Carry);
      
      // MARK: RET cc
      case 0xC9: return createRET(JumpTest.Always);
      case 0xC0: return createRET(JumpTest.NotZero);
      case 0xC8: return createRET(JumpTest.Zero);
      case 0xD0: return createRET(JumpTest.NotCarry);
      case 0xD8: return createRET(JumpTest.Carry);
      
      // MARK: RETI
      case 0xD9: return createRETI();
      
      // MARK: RST n
      case 0xC7: return createRST(0x00);
      case 0xCF: return createRST(0x08);
      case 0xD7: return createRST(0x10);
      case 0xDF: return createRST(0x18);
      case 0xE7: return createRST(0x20);
      case 0xEF: return createRST(0x28);
      case 0xF7: return createRST(0x30);
      case 0xFF: return createRST(0x38);
      
      // MARK: PUSH rr
      case 0xC5: return createPUSH(StackTarget.BC);
      case 0xD5: return createPUSH(StackTarget.DE);
      case 0xE5: return createPUSH(StackTarget.HL);
      case 0xF5: return createPUSH(StackTarget.AF);
      
      // MARK: POP rr
      case 0xC1: return createPOP(StackTarget.BC);
      case 0xD1: return createPOP(StackTarget.DE);
      case 0xE1: return createPOP(StackTarget.HL);
      case 0xF1: return createPOP(StackTarget.AF);
      
      // MARK: その他の命令
      case 0x07: return createRLCA();
      case 0x0F: return createRRCA();
      case 0x17: return createRLA();
      case 0x1F: return createRRA();
      case 0x27: return createDAA();
      case 0x2F: return createCPL();
      case 0x37: return createSCF();
      case 0x3F: return createCCF();
      case 0x76: return createHALT();
      case 0x10: return createSTOP();
      case 0xF3: return createDI();
      case 0xFB: return createEI();
      
      default:
        return null; // Invalid instruction
    }
  }

  private static RotateTarget mapToRotateTarget(int registerCode) {
    switch (registerCode) {
      case 0: return RotateTarget.B;
      case 1: return RotateTarget.C;
      case 2: return RotateTarget.D;
      case 3: return RotateTarget.E;
      case 4: return RotateTarget.H;
      case 5: return RotateTarget.L;
      case 6: return RotateTarget.HL_ADDR;
      case 7: return RotateTarget.A;
      default: return null;
    }
  }
  
  private static BitPosition mapToBitPosition(int bitCode) {
    switch (bitCode) {
      case 0: return BitPosition.BIT0;
      case 1: return BitPosition.BIT1;
      case 2: return BitPosition.BIT2;
      case 3: return BitPosition.BIT3;
      case 4: return BitPosition.BIT4;
      case 5: return BitPosition.BIT5;
      case 6: return BitPosition.BIT6;
      case 7: return BitPosition.BIT7;
      default: return null;
    }
  }

  public boolean isValid() {
    return true;
  }
}

// MARK: 命令タイプの列挙型
enum InstructionType {
  ADD, ADDHL, ADDSP, ADC, SUB, SBC, AND, OR, XOR, CP,
  INC, INCRP, DEC, DECRP,
  CCF, SCF, RRA, RLA, RRCA, RLCA, CPL, DAA, DI, EI,
  BIT, RES, SET, SRL, RR, RL, RRC, RLC, SRA, SLA, SWAP,
  JP, JPHL, JR, LD, LDHL, PUSH, POP,
  CALL, RET, RETI, RST, NOP, HALT, STOP
}

// MARK: 算術演算のターゲット
enum ArithmeticTarget {
  A, B, C, D, E, H, L, HL_ADDR, D8
}

// MARK: ロード命令のターゲット
enum LoadTarget {
  A, B, C, D, E, H, L,
  BC, DE, HL, SP,
  BC_ADDR, DE_ADDR, HL_ADDR, A16_ADDR,
  HLI_ADDR, HLD_ADDR, FF00_A8, FF00_C
}

// MARK: ロード命令のソース
enum LoadSource {
  A, B, C, D, E, H, L,
  BC, DE, HL, SP,
  BC_ADDR, DE_ADDR, HL_ADDR, A16_ADDR,
  HLI_ADDR, HLD_ADDR, FF00_A8, FF00_C,
  D8, D16, A8
}

// MARK: レジスタペア
enum RegisterPair {
  BC, DE, HL, SP
}

// MARK: ジャンプ条件
enum JumpTest {
  NotZero, Zero, NotCarry, Carry, Always,
}

// MARK: スタック操作対象
enum StackTarget {
  BC, DE, HL, AF
}

// MARK: ビット操作位置
enum BitPosition {
  BIT0, BIT1, BIT2, BIT3, BIT4, BIT5, BIT6, BIT7
}

// MARK: 回転操作対象
enum RotateTarget {
  A, B, C, D, E, H, L, HL_ADDR
}
