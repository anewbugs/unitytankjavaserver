package com.wu.server.room.base;

/**
 * 房间成员
 * @author wu
 * @version 1.0
 */
public class RoomMember {

    //*位置
    private float x;
    private float y;
    private float z;
    private float ex;
    private float ey;
    private float ez;

    //阵营
    private int camp = 1;
    //坦克生命值
    private int hp = 100;

    //tanid
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCamp() {
        return camp;
    }



    /**
     * 初始化带阵营
     * @param camp
     */
    public RoomMember(int camp ,String id) {
        this.camp = camp;
        this.id  = id;
    }
    //重新定位
    public void setPosition(float x , float y , float z , float ex , float ey,float ez){
        this.x = x;
        this.y = y;
        this.z = z;
        this.ex = ex;
        this.ey = ey;
        this.ez = ez;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getEx() {
        return ex;
    }

    public float getEy() {
        return ey;
    }

    public float getEz() {
        return ez;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }


}
