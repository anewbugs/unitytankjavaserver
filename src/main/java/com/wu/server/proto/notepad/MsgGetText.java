package com.wu.server.proto.notepad;

import com.wu.server.proto.MsgBase;

public class MsgGetText extends MsgBase {
    public MsgGetText() {protoName = "MsgGetText";}
    //服务端回
    public String text = "";
}
