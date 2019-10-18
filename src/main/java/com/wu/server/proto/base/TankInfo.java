package com.wu.server.proto.base;

public class TankInfo {
    public String id = "";	//玩家id
    public int camp = 0;	//阵营
    public int hp = 0;		//生命值

    public float x = 0;		//位置
    public float y = 0;
    public float z = 0;
    public float ex = 0;	//旋转
    public float ey = 0;
    public float ez = 0;

    public TankInfo(String id, int camp, int hp, float x, float y, float z, float ex, float ey, float ez) {
        this.id = id;
        this.camp = camp;
        this.hp = hp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.ex = ex;
        this.ey = ey;
        this.ez = ez;
    }
}
