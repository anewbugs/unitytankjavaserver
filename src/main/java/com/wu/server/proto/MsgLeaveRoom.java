package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgLeaveRoom extends MsgBase {
    public MsgLeaveRoom() {protoName = "MsgLeaveRoom";}
    //服务端回
    public int result = 0;
}
