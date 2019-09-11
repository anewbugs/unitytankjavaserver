package com.wu.server.proto.battlemsg;

import com.wu.server.proto.MsgBase;

public class MsgBattleResult extends MsgBase {
    public MsgBattleResult() {protoName = "MsgBattleResult";}
    //服务端回
    public int winCamp = 0;	 //获胜的阵营
}
