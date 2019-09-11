package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;

public class MsgCreateRoom extends MsgBase {
    public MsgCreateRoom() {protoName = "MsgCreateRoom";}
    //服务端回
    public int result = 0;
}
