package com.wu.server.proto.net;

import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.RoomInfo;

public class MsgGetRoomList extends MsgBase {
    public MsgGetRoomList() {protoName = "MsgGetRoomList";}
    public String id;
    //服务端回
    public RoomInfo[] rooms;
}
