package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgHit extends MsgBase {
    public MsgHit() {protoName = "MsgHit";}
    //击中谁
    public String targetId = "";
    //击中点
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    //服务端补充
    public String id = "";		//哪个坦克
    public int damage = 0;		//受到的伤害
}
