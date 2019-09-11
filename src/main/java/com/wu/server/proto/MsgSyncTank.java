package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgSyncTank extends MsgBase {
    public MsgSyncTank() {protoName = "MsgSyncTank";}
    //位置、旋转、炮塔旋转
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float ex = 0f;
    public float ey = 0f;
    public float ez = 0f;
    public float turretY = 0f;
    public float gunX = 0f;
    //服务端补充
    public String id = "";		//哪个坦克
}
