package com.wu.server.proto.login;

import com.wu.server.proto.MsgBase;

public class MsgRegister extends MsgBase {
    public MsgRegister() {protoName = "MsgRegister";}
    //客户端发
    public String id = "";
    public String pw = "";
    //服务端回（0-成功，1-失败）
    public int result = 0;
}
