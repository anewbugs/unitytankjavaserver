package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgGetAchieve extends MsgBase {
    public MsgGetAchieve() {protoName = "MsgGetAchieve";}
    //服务端回
    public int win = 0;
    public int lost = 0;
}
