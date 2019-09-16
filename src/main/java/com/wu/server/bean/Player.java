package com.wu.server.bean;

import io.netty.channel.Channel;

public class Player {
    //id
    private String id = "";
    //指向ClientState
    private Channel channel;
    //构造函数
    public Player(Channel channel){
        this.channel = channel;
    }
    //坐标和旋转
    private float x;
    private float y;
    private float z;
    private float ex;
    private float ey;
    private float ez;

    //在哪个房间
    private int roomId = -1;
    //阵营
    private int camp = 1;
    //坦克生命值
    private int hp = 100;

    //数据库数据
    private PlayerData data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getEx() {
        return ex;
    }

    public void setEx(float ex) {
        this.ex = ex;
    }

    public float getEy() {
        return ey;
    }

    public void setEy(float ey) {
        this.ey = ey;
    }

    public float getEz() {
        return ez;
    }

    public void setEz(float ez) {
        this.ez = ez;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCamp() {
        return camp;
    }

    public void setCamp(int camp) {
        this.camp = camp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public PlayerData getData() {
        return data;
    }

    public void setData(PlayerData data) {
        this.data = data;
    }
}
