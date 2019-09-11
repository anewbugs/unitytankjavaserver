package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;

public class MsgStartBattle extends MsgBase {
    public MsgStartBattle() {protoName = "MsgStartBattle";}
    //服务端回
    public int result = 0;
}
