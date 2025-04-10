package com.syntck;

import java.awt.Graphics;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main {
  public static void main(String[] args) {
    System.out.println("Hello, World!");
    GameBoyFrame gameBoyFrame = new GameBoyFrame();
  }
}

class GameBoyFrame extends JFrame implements KeyListener {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_RATE = 60; // 60 FPS
  public static final int FRAME_SCALE = 3; // ウィンドウサイズを拡大する倍率

  GameBoyFrame() {
    setTitle("Game Boy Emulator");
    setSize(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);  // Added key listener to enable key events.
    setVisible(true);
  }

  // 描画
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Graphics praphics = getContentPane().getGraphics();
  }

  // キー入力処理
  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ESCAPE: {
        System.exit(0); // ESCキーで終了
        break;
      }
     }
  }

  @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyReleased(KeyEvent e) {}
}
