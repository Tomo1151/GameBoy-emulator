package com.syntck;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.syntck.cartridge.Cartridge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.syntck.cpu.CPU;
import com.syntck.gpu.GPU;

public class GameBoy {
  private GameBoyFrame gameBoyFrame;
  private CPU cpu;
  private Cartridge cartridge;

  public GameBoy(Cartridge cartridge) {
    this.cpu = new CPU(cartridge);
    this.cartridge = cartridge;
    this.gameBoyFrame = new GameBoyFrame(this.cpu.bus.gpu);
    this.cartridge.dump(0x0104, 0x0133);

    this.run();
  }

  public void run() {
    while (true) {
      // CPUのクロックサイクルを実行
      this.cpu.step();

      // try {
      //   Thread.sleep(0, 1); // フレームレートに合わせてスリープ
      // } catch (InterruptedException e) {
      //   e.printStackTrace();
      // }

      // System.out.println("Status: " + String.format("0x%04X", this.cpu.bus.readByte(0xFF01)));
      // System.out.println("LY: " + cpu.bus.gpu.ly);

      if (this.cpu.bus.gpu.frameUpdated) {
        this.gameBoyFrame.repaint();
        this.cpu.bus.gpu.frameUpdated = false;
        // break;
      }
    }
  }
}

class GameBoyFrame extends JFrame implements KeyListener {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_RATE = 60; // 60 FPS
  public static final int FRAME_SCALE = 3; // ウィンドウサイズを拡大する倍率

  public Graphics g;

  private GameBoyPanel gameBoyPanel;

  GameBoyFrame(GPU gpu) {
    this.gameBoyPanel = new GameBoyPanel(gpu);
    add(gameBoyPanel); // Add the panel to the frame

    setTitle("Game Boy Emulator");
    setSize(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);  // Added key listener to enable key events.
    setVisible(true);
    this.gameBoyPanel.repaint();
  }

  // public void repaint(Graphics g, int[] frames) {

    // super.repaint();

    // g = getContentPane().getGraphics();
    // for (int y = 0; y < SCREEN_HEIGHT; y++) {
    //   for (int x = 0; x < SCREEN_WIDTH; x++) {
    //     int color = frames[y * SCREEN_WIDTH + x];

    //     g.setColor(colors[color]);
    //     g.fillRect(x * FRAME_SCALE, y * FRAME_SCALE, FRAME_SCALE, FRAME_SCALE);
    //   }
    // }
    // g.dispose();
  // }


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

class GameBoyPanel extends JPanel {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_RATE = 60; // 60 FPS
  public static final int FRAME_SCALE = 3; // ウィンドウサイズを拡大する倍率
  private final Color[] COLORS = {
    new Color(232, 252, 204),
    new Color(172, 212, 144),
    new Color(84, 140, 112),
    new Color(20, 44, 56),
  };
  private GPU gpu;

  public GameBoyPanel(GPU gpu) {
    this.gpu = gpu;
    setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // g.clearRect(0, 0, SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE);

    int[] frame = gpu.getFrames(); // GPUからフレームを取得
    for (int y = 0; y < SCREEN_HEIGHT; y++) {
      for (int x = 0; x < SCREEN_WIDTH; x++) {
        int color = frame[y * SCREEN_WIDTH + x]; // フレームの色を取得
        // System.out.print(color + " ");
        g.setColor(COLORS[color]); // 色を設定
        for (int i = 0; i < FRAME_SCALE; i++) {
          for (int j = 0; j < FRAME_SCALE; j++) {
            g.fillRect(x * FRAME_SCALE + i, y * FRAME_SCALE + j, FRAME_SCALE, FRAME_SCALE); // 拡大して描画
          }
        }
      }
      // System.out.println();
    }
    // g.dispose(); // グラフィックスオブジェクトを解放
  }
}