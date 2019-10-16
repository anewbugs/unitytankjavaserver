package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public  class MsgBattleResult extends MsgBase {
    public MsgBattleResult() {protoName = "MsgBattleResult";}
    //服务端回
    public int winCamp = 0;	 //获胜的阵营
}
