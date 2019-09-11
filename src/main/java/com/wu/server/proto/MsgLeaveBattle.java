package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgLeaveBattle extends MsgBase {
    public MsgLeaveBattle() {protoName = "MsgLeaveBattle";}
    //服务端回
    public String id = "";	//玩家id
}
