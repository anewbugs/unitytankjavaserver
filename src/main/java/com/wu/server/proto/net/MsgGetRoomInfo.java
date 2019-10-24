package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.PlayerInfo;

public class MsgGetRoomInfo extends MsgBase {
    public MsgGetRoomInfo() {protoName = "MsgGetRoomInfo";}
    //玩家id
    public String id ;

    //服务端回
    public PlayerInfo[] players;
}
