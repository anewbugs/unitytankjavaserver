package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgFire extends MsgBase {
    public MsgFire() {protoName = "MsgFire";}
    //炮弹初始位置、旋转
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float ex = 0f;
    public float ey = 0f;
    public float ez = 0f;
    //服务端补充
    public String id = "";		//哪个坦克
}
