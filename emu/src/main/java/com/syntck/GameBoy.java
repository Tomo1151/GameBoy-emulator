package com.syntck;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

import com.syntck.cartridge.Cartridge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.syntck.cpu.CPU;

public class GameBoy {
  private GameBoyFrame gameBoyFrame;
  private CPU cpu;
  private Cartridge cartridge;

  public GameBoy(Cartridge cartridge) {
    this.gameBoyFrame = new GameBoyFrame();
    this.cpu = new CPU(cartridge);
    this.cartridge = cartridge;
    cartridge.dump(0x0104, 0x0133);

    this.run();
  }

  public void run() {
    while (true) {
      this.cpu.step();
      System.out.println("Status: " + String.format("0x%04X", this.cpu.bus.readByte(0xFF01)));
      // if (cpu.bus.gpu.ly == 144) {
        // int[] frames = cpu.bus.gpu.getFrames();
        // gameBoyFrame.repaint(this.gameBoyFrame.getGraphics(), frames);
      // }
    }
  }
}

class GameBoyFrame extends JFrame implements KeyListener {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_RATE = 60; // 60 FPS
  public static final int FRAME_SCALE = 3; // ウィンドウサイズを拡大する倍率

  public Graphics g;

  GameBoyFrame() {
    setTitle("Game Boy Emulator");
    setSize(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);  // Added key listener to enable key events.
    setVisible(true);
  }

  public void repaint(Graphics g, int[] frames) {
    Color[] colors = {new Color(255, 255, 255), new Color(192, 192, 192), new Color(96, 96, 96), new Color(0, 0, 0)};
    super.repaint();

    g = getContentPane().getGraphics();
    for (int y = 0; y < SCREEN_HEIGHT; y++) {
      for (int x = 0; x < SCREEN_WIDTH; x++) {
        int color = frames[y * SCREEN_WIDTH + x];

        g.setColor(colors[color]);
        g.fillRect(x * FRAME_SCALE, y * FRAME_SCALE, FRAME_SCALE, FRAME_SCALE);
      }
    }
    g.dispose();
  }

  // 描画
  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.g = g;
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
