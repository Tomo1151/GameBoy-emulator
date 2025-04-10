package com.syntck.cpu;

/**
 * GameBoy CPU命令のバイト長を提供するユーティリティクラス
 */
public class InstructionLengthUtil {
    // 通常命令(0x00-0xFF)のバイト長テーブル
    private static final int[] INSTRUCTION_LENGTHS = new int[256];
    private static final int[] INSTRUCTION_CYCLES = new int[256];
    
    // CBプレフィックス命令(0xCB 0x00-0xFF)のバイト長（常に2バイト）
    private static final int CB_PREFIX_LENGTH = 2;
    
    // 命令のバイト長を初期化する静的ブロック
    static {
        // デフォルトでほとんどの命令は1バイト
        for (int i = 0; i < 256; i++) {
            INSTRUCTION_LENGTHS[i] = 1;
        }
        
        // 2バイト命令 (オペコード + d8/r8/a8)を設定
        int[] twoByteOpcodes = {
            0x06, 0x0E, 0x16, 0x1E, 0x26, 0x2E, 0x36, 0x3E, // LD r,d8
            0xC6, 0xCE, 0xD6, 0xDE, 0xE6, 0xEE, 0xF6, 0xFE, // Arithmetic with d8
            0xE0, 0xF0, // LDH (a8),A / LDH A,(a8)
            0x10, // STOP
            0x18, 0x20, 0x28, 0x30, 0x38, // JR cc,r8
            0xE8, 0xF8 // ADD SP,r8 / LD HL,SP+r8
        };
        
        for (int opcode : twoByteOpcodes) {
            INSTRUCTION_LENGTHS[opcode] = 2;
        }
        
        // 3バイト命令 (オペコード + d16/a16)を設定
        int[] threeByteOpcodes = {
            0x01, 0x11, 0x21, 0x31, // LD rr,d16
            0xC2, 0xC3, 0xCA, 0xD2, 0xDA, // JP cc,a16
            0xC4, 0xCC, 0xCD, 0xD4, 0xDC, // CALL cc,a16
            0x08, // LD (a16),SP
            0xEA, 0xFA // LD (a16),A / LD A,(a16)
        };
        
        for (int opcode : threeByteOpcodes) {
            INSTRUCTION_LENGTHS[opcode] = 3;
        }
    }

    // 命令のサイクル数を初期化する静的ブロック
    static {
        // デフォルトで未設定命令は0サイクル
        for (int i = 0; i < 256; i++) {
            INSTRUCTION_CYCLES[i] = 0;
        }
        
        // 4サイクル命令
        int[] fourCycleOpcodes = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F,
            0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27, 
            0x29, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F,
            0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 
            0x39, 0x3A, 0x3B, 0x3C, 0x3D, 0x3E, 0x3F,
            0x40, 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47,
            0x48, 0x49, 0x4A, 0x4B, 0x4C, 0x4D, 0x4E, 0x4F,
            0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57,
            0x58, 0x59, 0x5A, 0x5B, 0x5C, 0x5D, 0x5E, 0x5F,
            0x60, 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67,
            0x68, 0x69, 0x6A, 0x6B, 0x6C, 0x6D, 0x6E, 0x6F,
            0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77,
            0x78, 0x79, 0x7A, 0x7B, 0x7C, 0x7D, 0x7E, 0x7F
        };
        
        for (int opcode : fourCycleOpcodes) {
            INSTRUCTION_CYCLES[opcode] = 4;
        }
        
        // 8サイクル命令
        int[] eightCycleOpcodes = {
            0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87,
            0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F,
            0x90, 0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97,
            0x98, 0x99, 0x9A, 0x9B, 0x9C, 0x9D, 0x9E, 0x9F,
            0xA0, 0xA1, 0xA2, 0xA3, 0xA4, 0xA5, 0xA6, 0xA7,
            0xA8, 0xA9, 0xAA, 0xAB, 0xAC, 0xAD, 0xAE, 0xAF,
            0xB0, 0xB1, 0xB2, 0xB3, 0xB4, 0xB5, 0xB6, 0xB7,
            0xB8, 0xB9, 0xBA, 0xBB, 0xBC, 0xBD, 0xBE, 0xBF
        };
        
        for (int opcode : eightCycleOpcodes) {
            INSTRUCTION_CYCLES[opcode] = 8;
        }
        
        // 12サイクル命令
        int[] twelveCycleOpcodes = {
            0xC0, 0xC1, 0xC2, 0xC3, 0xC4, 0xC5, 0xC6, 0xC7,
            0xC8, 0xC9, 0xCA, 0xCC, 0xCD, 0xCE, 0xCF,
            0xD0, 0xD1, 0xD2, 0xD4, 0xD5, 0xD6, 0xD7,
            0xD8, 0xD9, 0xDA, 0xDC, 0xDE, 0xDF,
            0xE0, 0xE1, 0xE2, 0xE5, 0xE6, 0xE7,
            0xE8, 0xE9, 0xEA, 0xEE, 0xEF,
            0xF0, 0xF1, 0xF2, 0xF3, 0xF5, 0xF6, 0xF7,
            0xF8, 0xF9, 0xFA, 0xFB, 0xFE, 0xFF
        };
        
        for (int opcode : twelveCycleOpcodes) {
            INSTRUCTION_CYCLES[opcode] = 12;
        }
        
        // 個別に設定が必要な命令
        // 条件分岐命令（JR cc, r8）- 条件成立時のサイクル数を使用
        INSTRUCTION_CYCLES[0x20] = 12; // JR NZ, r8 (True: 12, False: 8)
        INSTRUCTION_CYCLES[0x28] = 12; // JR Z, r8 (True: 12, False: 8)
        INSTRUCTION_CYCLES[0x30] = 12; // JR NC, r8 (True: 12, False: 8)
        INSTRUCTION_CYCLES[0x38] = 12; // JR C, r8 (True: 12, False: 8)
        
        // 条件付きCALL命令 - 条件成立時のサイクル数を使用
        INSTRUCTION_CYCLES[0xC4] = 24; // CALL NZ, a16 (True: 24, False: 12)
        INSTRUCTION_CYCLES[0xCC] = 24; // CALL Z, a16 (True: 24, False: 12)
        INSTRUCTION_CYCLES[0xD4] = 24; // CALL NC, a16 (True: 24, False: 12)
        INSTRUCTION_CYCLES[0xDC] = 24; // CALL C, a16 (True: 24, False: 12)
        
        // 条件付きRET命令 - 条件成立時のサイクル数を使用
        INSTRUCTION_CYCLES[0xC0] = 20; // RET NZ (True: 20, False: 8)
        INSTRUCTION_CYCLES[0xC8] = 20; // RET Z (True: 20, False: 8)
        INSTRUCTION_CYCLES[0xD0] = 20; // RET NC (True: 20, False: 8)
        INSTRUCTION_CYCLES[0xD8] = 20; // RET C (True: 20, False: 8)
        
        // 条件付きJP命令 - 条件成立時のサイクル数を使用
        INSTRUCTION_CYCLES[0xC2] = 16; // JP NZ, a16 (True: 16, False: 12)
        INSTRUCTION_CYCLES[0xCA] = 16; // JP Z, a16 (True: 16, False: 12)
        INSTRUCTION_CYCLES[0xD2] = 16; // JP NC, a16 (True: 16, False: 12)
        INSTRUCTION_CYCLES[0xDA] = 16; // JP C, a16 (True: 16, False: 12)
        
        // その他の特殊命令
        INSTRUCTION_CYCLES[0x76] = 4;  // HALT
        INSTRUCTION_CYCLES[0xCB] = 4;  // プレフィックスとしての CB (実際のサイクル数は後続命令による)
        INSTRUCTION_CYCLES[0xE8] = 16; // ADD SP, r8
        INSTRUCTION_CYCLES[0xF8] = 12; // LD HL, SP+r8
        
        // 残りの命令は個別に設定
        INSTRUCTION_CYCLES[0x08] = 20; // LD (a16), SP
        INSTRUCTION_CYCLES[0x10] = 4;  // STOP
        INSTRUCTION_CYCLES[0xCD] = 24; // CALL a16
        INSTRUCTION_CYCLES[0xC9] = 16; // RET
        INSTRUCTION_CYCLES[0xD9] = 16; // RETI
        INSTRUCTION_CYCLES[0xE0] = 12; // LDH (a8), A
        INSTRUCTION_CYCLES[0xF0] = 12; // LDH A, (a8)
        INSTRUCTION_CYCLES[0xEA] = 16; // LD (a16), A
        INSTRUCTION_CYCLES[0xFA] = 16; // LD A, (a16)
    }
    
    /**
     * 指定されたオペコードに対応する命令のバイト長を返す
     * @param opcode 命令のオペコード（0x00-0xFF）
     * @param isCBPrefixed CBプレフィックス命令かどうか
     * @return 命令のバイト長
     */
    public static int getInstructionLength(int opcode, boolean isCBPrefixed) {
        if (isCBPrefixed) {
            return CB_PREFIX_LENGTH;
        } else {
            return INSTRUCTION_LENGTHS[opcode & 0xFF];
        }
    }
    
    /**
     * バイト列から命令長を取得する
     * @param memory メモリバイト配列
     * @param address 命令の開始アドレス
     * @return 命令のバイト長
     */
    public static int getInstructionLengthAt(byte[] memory, int address) {
        int opcode = memory[address] & 0xFF;
        if (opcode == 0xCB) {
            return CB_PREFIX_LENGTH;
        } else {
            return INSTRUCTION_LENGTHS[opcode];
        }
    }
    
    /**
     * 指定されたオペコードに対応する命令のサイクル数を返す
     * @param opcode 命令のオペコード（0x00-0xFF）
     * @param isCBPrefixed CBプレフィックス命令かどうか
     * @param condition 命令の条件が成立したかどうか（条件分岐命令の場合に使用）
     * @return 命令のサイクル数
     */
    public static int getInstructionCycles(int opcode, boolean isCBPrefixed, boolean condition) {
        if (isCBPrefixed) {
            // CBプレフィックス命令は基本8サイクル
            // (HL)を使う命令の場合は16サイクル
            return ((opcode & 0x07) == 0x06) ? 16 : 8;
        } else {
            // 条件分岐命令のサイクル数を処理
            switch(opcode) {
                // JP cc, a16 - 条件ジャンプ命令
                case 0xC2: // JP NZ, a16
                case 0xCA: // JP Z, a16
                case 0xD2: // JP NC, a16
                case 0xDA: // JP C, a16
                    return condition ? 16 : 12;

                // JR cc, r8 - 条件相対ジャンプ命令
                case 0x20: // JR NZ, r8
                case 0x28: // JR Z, r8
                case 0x30: // JR NC, r8
                case 0x38: // JR C, r8
                    return condition ? 12 : 8;

                // CALL cc, a16 - 条件コール命令
                case 0xC4: // CALL NZ, a16
                case 0xCC: // CALL Z, a16
                case 0xD4: // CALL NC, a16
                case 0xDC: // CALL C, a16
                    return condition ? 24 : 12;

                // RET cc - 条件リターン命令
                case 0xC0: // RET NZ
                case 0xC8: // RET Z
                case 0xD0: // RET NC
                case 0xD8: // RET C
                    return condition ? 20 : 8;

                // その他の命令は条件に関わらず固定サイクル
                default:
                    return INSTRUCTION_CYCLES[opcode & 0xFF];
            }
        }
    }

    /**
     * 後方互換性のために残すオーバーロードメソッド
     * 条件がtrueの場合のサイクル数を返す
     */
    public static int getInstructionCycles(int opcode, boolean isCBPrefixed) {
        return getInstructionCycles(opcode, isCBPrefixed, true);
    }
}
