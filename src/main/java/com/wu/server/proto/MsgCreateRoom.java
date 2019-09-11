package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgCreateRoom extends MsgBase {
    public MsgCreateRoom() {protoName = "MsgCreateRoom";}
    //服务端回
    public int result = 0;
}
