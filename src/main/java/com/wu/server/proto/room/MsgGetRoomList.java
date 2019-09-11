package com.wu.server.proto.room;

import com.wu.server.proto.MsgBase;
import com.wu.server.proto.room.info.RoomInfo;

public class MsgGetRoomList extends MsgBase {
    public MsgGetRoomList() {protoName = "MsgGetRoomList";}
    //服务端回
    public RoomInfo[] rooms;
}
