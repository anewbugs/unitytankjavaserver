package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgEnterRoom extends MsgBase {
    public MsgEnterRoom() {protoName = "MsgEnterRoom";}
    //客户端发
    public int roomId = 0;
    public String id ;
    //服务端回
    public int result = 0;
}
