package com.syntck;

import java.awt.Graphics;
import javax.swing.JFrame;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello, World!");
    GameBoyFrame gameBoyFrame = new GameBoyFrame();
  }
}

class GameBoyFrame extends JFrame {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_RATE = 60; // 60 FPS
  public static final int FRAME_SCALE = 3; // ウィンドウサイズを拡大する倍率

  GameBoyFrame() {
    setTitle("Game Boy Emulator");
    setSize(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics praphics = getContentPane().getGraphics();
  }
}
