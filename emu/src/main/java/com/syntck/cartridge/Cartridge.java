package com.syntck.cartridge;

import java.io.FileInputStream;

public class Cartridge {
  public int[] binaryData; // ROMのバイナリデータを格納する配列

  public Cartridge(String file) {
    try (FileInputStream fis = new FileInputStream(file)) {
      // ROMのサイズを取得
      int fileSize = (int) fis.getChannel().size();
      binaryData = new int[fileSize]; // バイナリデータの配列を初期化

      // ROMのバイナリデータを読み込む
      for (int i = 0; i < fileSize; i++) {
        binaryData[i] = fis.read(); // 1バイトずつ読み込む
      }
    } catch (Exception e) {
      e.printStackTrace(); // エラーが発生した場合はスタックトレースを表示
    }
  }

  public void dump(int start, int end) {
    if (this.binaryData == null) {
      System.out.println("No binary data loaded.");
      return;
    }

    if (start < 0 || end > binaryData.length || start >= end) {
      throw new IllegalArgumentException("Invalid range for dump: " + String.format("0x%04X - 0x%04X", start, end));
    }

    for (int i = start; i <= end; i++) {
      System.out.printf("%02X ", binaryData[i]); // 16進数で表示
      if ((i - start + 1) % 16 == 0) {
        System.out.println(); // 16バイトごとに改行
      }
    }
    System.out.println(); // 最後に改行
  }
}
