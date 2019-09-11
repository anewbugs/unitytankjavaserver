package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;

public class MsgLeaveRoom extends MsgBase {
    public MsgLeaveRoom() {protoName = "MsgLeaveRoom";}
    //服务端回
    public int result = 0;
}
