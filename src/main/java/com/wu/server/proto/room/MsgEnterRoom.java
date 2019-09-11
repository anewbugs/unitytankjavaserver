package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;

public class MsgEnterRoom extends MsgBase {
    public MsgEnterRoom() {protoName = "MsgEnterRoom";}
    //客户端发
    public int id = 0;
    //服务端回
    public int result = 0;
}
