package com.wu.server.proto.battlemsg;

import com.wu.server.proto.MsgBase;
import com.wu.server.proto.battlemsg.info.TankInfo;

public class MsgEnterBattle extends MsgBase {
    public MsgEnterBattle() {protoName = "MsgEnterBattle";}
    //服务端回
    public TankInfo[] tanks;
    public int mapId = 1;	//地图，只有一张
}
