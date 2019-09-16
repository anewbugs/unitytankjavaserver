package com.wu.server.bean;

public class PlayerData {
    //金币
    private int coin = 0;
    //记事本
    private String text = "new text";
    //胜利数
    private int win = 0;
    //失败数
    private int lost = 0;

    public int getCoin() {
        return coin;
    }

    public String getText() {
        return text;
    }

    public int getWin() {
        return win;
    }

    public int getLost() {
        return lost;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }
}
