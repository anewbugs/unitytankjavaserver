package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgGetText extends MsgBase {
    public MsgGetText() {protoName = "MsgGetText";}
    //服务端回
    public String text = "";
}
