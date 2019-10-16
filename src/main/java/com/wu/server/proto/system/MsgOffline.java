package com.wu.server.proto.system;

import com.wu.server.proto.base.MsgBase;

public class MsgOffline extends MsgBase {
    public MsgOffline() {
        protoName = "MsgOffline";
    }
    public String id;
}
