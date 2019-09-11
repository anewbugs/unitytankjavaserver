package com.wu.server.proto;

import com.wu.server.proto.base.MsgBase;

public class MsgKick extends MsgBase {
    public MsgKick() {protoName = "MsgKick";}
    //原因（0-其他人登陆同一账号）
    public int reason = 0;
}
