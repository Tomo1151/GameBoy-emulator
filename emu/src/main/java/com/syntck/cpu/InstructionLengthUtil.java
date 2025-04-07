package com.syntck.cpu;

/**
 * GameBoy CPU命令のバイト長を提供するユーティリティクラス
 */
public class InstructionLengthUtil {
    // 通常命令(0x00-0xFF)のバイト長テーブル
    private static final int[] INSTRUCTION_LENGTHS = new int[256];
    
    // CBプレフィックス命令(0xCB 0x00-0xFF)のバイト長（常に2バイト）
    private static final int CB_PREFIX_LENGTH = 2;
    
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
}
