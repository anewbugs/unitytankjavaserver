package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgSaveText extends MsgBase {
    public MsgSaveText() {protoName = "MsgSaveText";}
    //客户端发
    public String text = "";
    //服务端回（0-成功 1-文字太长）
    public int result = 0;
}
