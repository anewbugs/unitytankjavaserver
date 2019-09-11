package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;
import com.wu.server.proto.room.info.PlayerInfo;

public class MsgGetRoomInfo extends MsgBase {
    public MsgGetRoomInfo() {protoName = "MsgGetRoomInfo";}
    //服务端回
    public PlayerInfo[] players;
}
