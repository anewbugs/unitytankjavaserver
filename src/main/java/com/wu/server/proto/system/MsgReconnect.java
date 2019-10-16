package com.wu.server.proto.system;

import com.wu.server.proto.base.MsgBase;

public class MsgReconnect extends MsgBase {
    public MsgReconnect() {
        protoName = "MsgReconnect";
    }
    //user id
    public String id ;
    //0 成功
    public int result = 0;
}
