package com.syntck;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.concurrent.TimeUnit;

import com.syntck.cartridge.Cartridge;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.syntck.cpu.CPU;
import com.syntck.joypad.Joypad;
import com.syntck.ppu.PPU;

public class GameBoy {
  private GameBoyFrame gameBoyFrame;
  private CPU cpu;
  private Cartridge cartridge;

  public GameBoy(Cartridge cartridge) {
    this.cpu = new CPU(cartridge);
    this.cartridge = cartridge;
    this.gameBoyFrame = new GameBoyFrame(this.cpu.bus.gpu, this.cpu.bus.joypad);
    this.cartridge.dump(0x0104, 0x0133);

    this.run();
  }

  public void run() {
    // 最後のフレーム更新時間
    long lastFrameTime = System.nanoTime();
    final long frameTimeNanos = 1_000_000_00 / 60; // 60FPS
    
    while (true) {
      // CPU実行
      for (int i = 0; i < 70000 / 60; i++) { // 約1フレーム分のCPUサイクル
        this.cpu.step();
      }
      
      // フレーム更新条件
      if (this.cpu.bus.gpu.frameUpdated) {
        // フレームバッファを更新
        this.gameBoyFrame.panel.updateFrame();
        this.cpu.bus.gpu.frameUpdated = false;
        
        // フレームレート制御
        long currentTime = System.nanoTime();
        long elapsedTime = currentTime - lastFrameTime;
        
        if (elapsedTime < frameTimeNanos) {
          try {
              TimeUnit.NANOSECONDS.sleep(frameTimeNanos - elapsedTime);
            } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
        
        lastFrameTime = System.nanoTime();
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

  public GameBoyPanel panel;
  private Joypad joypad;

  GameBoyFrame(PPU gpu, Joypad joypad) {
    this.panel = new GameBoyPanel(gpu);
    this.joypad = joypad;
    add(panel); // Add the panel to the frame

    setTitle("Game Boy Emulator");
    panel.setPreferredSize(new java.awt.Dimension(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE));
    pack(); // Pack the frame to fit the preferred size
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    addKeyListener(this);  // Added key listener to enable key events.
    setVisible(true);
    this.panel.repaint();
  }

  // キー入力処理
  @Override
  public void keyPressed(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ESCAPE: {
        System.exit(0); // ESCキーで終了
        break;
      }
      case KeyEvent.VK_W: {
        joypad.buttonUp = true; // 上ボタンを押す
        break;
      }
      case KeyEvent.VK_S: {
        joypad.buttonDown = true; // 下ボタンを押す
        break;
      }
      case KeyEvent.VK_A: {
        joypad.buttonLeft = true; // 左ボタンを押す
        break;
      }
      case KeyEvent.VK_D: {
        joypad.buttonRight = true; // 右ボタンを押す
        break;
      }
      case KeyEvent.VK_J: {
        joypad.buttonA = true; // Aボタンを押す
        break;
      }
      case KeyEvent.VK_K: {
        joypad.buttonB = true; // Bボタンを押す
        break;
      }
      case KeyEvent.VK_ENTER: {
        joypad.buttonStart = true; // STARTボタンを押す
        break;
      }
      case KeyEvent.VK_BACK_SPACE: {
        joypad.buttonSelect = true; // SELECTボタンを押す
        break;
      }
     }
  }

  @Override
  public void keyTyped(KeyEvent e) {}
  @Override
  public void keyReleased(KeyEvent e) {
    switch (e.getKeyCode()) {
      case KeyEvent.VK_ESCAPE: {
        System.exit(0); // ESCキーで終了
        break;
      }
      case KeyEvent.VK_W: {
        joypad.buttonUp = false; // 上ボタンを押す
        break;
      }
      case KeyEvent.VK_S: {
        joypad.buttonDown = false; // 下ボタンを押す
        break;
      }
      case KeyEvent.VK_A: {
        joypad.buttonLeft = false; // 左ボタンを押す
        break;
      }
      case KeyEvent.VK_D: {
        joypad.buttonRight = false; // 右ボタンを押す
        break;
      }
      case KeyEvent.VK_J: {
        joypad.buttonA = false; // Aボタンを押す
        break;
      }
      case KeyEvent.VK_K: {
        joypad.buttonB = false; // Bボタンを押す
        break;
      }
      case KeyEvent.VK_ENTER: {
        joypad.buttonStart = false; // STARTボタンを押す
        break;
      }
      case KeyEvent.VK_BACK_SPACE: {
        joypad.buttonSelect = false; // SELECTボタンを押す
        break;
      }
     }
  }
}

class GameBoyPanel extends JPanel {
  public static final int SCREEN_WIDTH = 160;
  public static final int SCREEN_HEIGHT = 144;
  public static final int FRAME_SCALE = 3;

  public static final Color[] COLORS = {
    new Color(232, 252, 204), // 0x00: 黒
    new Color(172, 212, 144), // 0x01: ダークグレー
    new Color(84, 140, 112), // 0x02: ライトグレー
    new Color(20, 44, 56), // 0x03: 白
  };
  
  // 描画用バッファ
  private final BufferedImage frameBuffer;
  private final PPU gpu;

  public GameBoyPanel(PPU gpu) {
    this.gpu = gpu;
    // setSize(new java.awt.Dimension(SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE));
    
    // Swingのダブルバッファリングを有効化
    setDoubleBuffered(true);
    
    // バッファ画像を1回だけ作成
    frameBuffer = new BufferedImage(
        SCREEN_WIDTH, 
        SCREEN_HEIGHT, 
        BufferedImage.TYPE_INT_RGB);
  }

  // フレームバッファを更新するメソッド（GameBoyクラスから呼ばれる）
  public void updateFrame() {
    // GPUから最新のフレームデータを取得
    int[] frame = gpu.getFrame();
    
    // バッファの画像データを直接操作
    int[] pixels = ((DataBufferInt) frameBuffer.getRaster().getDataBuffer()).getData();
    
    // フレームバッファを効率的に更新
    for (int y = 0; y < SCREEN_HEIGHT; y++) {
      for (int x = 0; x < SCREEN_WIDTH; x++) {
        int colorIndex = frame[y * SCREEN_WIDTH + x]; // 0x00, 0x01, 0x02, 0x03
        Color color = COLORS[colorIndex]; // 色を取得
        pixels[y * SCREEN_WIDTH + x] = color.getRGB(); // ピクセルデータを更新
      }
    }
    
    // Swingスレッド上で再描画をリクエスト
    SwingUtilities.invokeLater(this::repaint);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    
    // 完成したバッファ画像を拡大して描画
    g.drawImage(frameBuffer, 0, 0, SCREEN_WIDTH * FRAME_SCALE, SCREEN_HEIGHT * FRAME_SCALE, null);
  }
}