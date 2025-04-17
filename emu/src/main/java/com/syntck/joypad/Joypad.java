package com.syntck.joypad;

public class Joypad {
  public boolean buttonLeft;
  public boolean buttonRight;
  public boolean buttonUp;
  public boolean buttonDown;
  public boolean buttonA;
  public boolean buttonB;
  public boolean buttonSelect;
  public boolean buttonStart;

  public boolean selectAction;
  public boolean selectDirection;

  public Joypad() {
    this.buttonLeft = false;
    this.buttonRight = false;
    this.buttonUp = false;
    this.buttonDown = false;
    this.buttonA = false;
    this.buttonB = false;
    this.buttonSelect = false;
    this.buttonStart = false;

    this.selectDirection = true;
    this.selectAction = true;
  }

  public void write(int value) {
    this.selectDirection = (value & 0x10) == 0; // 4ビット目
    this.selectAction = (value & 0x20) == 0; // 5ビット目
  }

  public int read() { 
    int value = 0xC0 | (this.selectDirection ? 0x00 : 0x10) | (this.selectAction ? 0x00 : 0x20); // 4ビット目と5ビット目を設定

    if (this.selectDirection == this.selectAction) return (value | 0x0F);

    if (this.selectDirection) {
      value = value |
        (this.buttonRight ? 0x00 : 0x01) |
        (this.buttonLeft ? 0x00 : 0x02) |
        (this.buttonUp ? 0x00 : 0x04) |
        (this.buttonDown ? 0x00 : 0x08); // 上下左右のボタンの状態を設定
    }

    if (this.selectAction) {
      value = value |
        (this.buttonA ? 0x00 : 0x01) |
        (this.buttonB ? 0x00 : 0x02) |
        (this.buttonSelect ? 0x00 : 0x04) |
        (this.buttonStart ? 0x00 : 0x08); // Aボタン、Bボタン、SELECTボタン、STARTボタンの状態を設定
    }

    return (value & 0xFF); // すべてのボタンが押されていない場合はtrueを返す
  }
}
