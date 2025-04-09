package com.syntck.cpu;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class RETTest {
  // MARK: RET unconditional
  @Test
  @DisplayName("Test RET unconditional instruction")
  public void testRETUnconditional() throws Exception {
    CPU cpu = new CPU();
    int originalSP = cpu.sp; // 例: 0xFFFF
    // 呼び出し命令実行時にpush()でリターンアドレスがスタックにプッシュされる
    cpu.push(0x1234); // SPは2減少 (例: 0xFFFD)
    // RET命令（無条件）の実行
    cpu.pc = 0x0100; // RET命令自体は1バイト命令と仮定
    int retAddress = cpu.return_(true); // 条件成立 → スタックからポップしてPCを設定
    // RET後、戻り先はpush時にプッシュした値となる
    assertEquals(0x1234, retAddress);
    // RETによりSPは2バイト分戻り、元の値 (originalSP) に戻っているはず
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET with condition true
  @Test
  @DisplayName("Test RET with condition true")
  public void testRETConditionTrue() throws Exception {
    CPU cpu = new CPU();
    int originalSP = cpu.sp;
    // push()により戻り先(例:0x5678)をスタックへプッシュ
    cpu.push(0x5678);
    cpu.pc = 0x0200;
    int retAddress = cpu.return_(true); // 条件が成立 → スタックから値をポップ
    assertEquals(0x5678, retAddress);
    assertEquals(originalSP, cpu.sp);
  }
  
  // MARK: RET with condition false
  @Test
  @DisplayName("Test RET with condition false")
  public void testRETConditionFalse() throws Exception {
    CPU cpu = new CPU();
    int originalSP = cpu.sp;
    // push()によりスタックに値をプッシュしておく (例:0x9ABC)
    cpu.push(0x9ABC);
    cpu.pc = 0x0300;
    // 条件が不成立の場合、RET命令はスタックから値をポップせず、命令サイズ分 (1バイト) だけPCを進める
    int retAddress = cpu.return_(false);
    assertEquals(0x0300 + 1, retAddress);
    // SPは変更されず、pushで減少したまま (originalSP - 2)
    assertEquals(originalSP - 2, cpu.sp);
  }
}
