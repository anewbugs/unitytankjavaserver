package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;

public class MsgBattleText extends MsgBase {
    public MsgBattleText() {protoName = "MsgBattleText";}
    public String text;

    //玩家掉线消息设置
    public void offLineText(String id ){
        this.text = "玩家： “" + id +"” 掉线";
    }

    public void reconnectText(String id ){
        this.text = "玩家： “" + id +"” 重新连接";
    }
}
