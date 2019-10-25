package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgStartBattle extends MsgBase {
    public MsgStartBattle() {protoName = "MsgStartBattle";}
    //tank id
    public String id ;
    //服务端回
    public int result = 0;
}
