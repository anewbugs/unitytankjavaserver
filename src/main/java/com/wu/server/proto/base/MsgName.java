package com.wu.server.proto.base;

public class MsgName {
    public static final class Battle {
        public static final String MSG_ENTER_BATTLE = "MsgEnterBattle";
        public static final String MSG_BATTLE_RESULT = "MsgBattleResult";
        public static final String MSG_LEAVE_BATTLE = "MsgLeaveBattle";
    }
    public static final class Login {
        public static final String MSG_REGISTER = "MsgRegister";
        public static final String MSG_LOGIN = "MsgLogin";
        public static final String MSG_KICK = "MsgKick";
        public static final String MSG_RECONNECT = "MsgReconnect";
        public static final String MSG_OFF_LINE = "MsgOffline"; //掉线消息
    }
    public static final class User{
        public static final String MSG_GET_ACHIEVE = "MsgGetAchieve";
    }

    public static final class Notepad {
        public static final String MSG_GET_TEXT = "MsgGetText";
        public static final String MSG_SAVE_TEXT = "MsgSaveText";
    }

    public static final class Room {

        public static final String MSG_GET_ROOM_LIST = "MsgGetRoomList";
        public static final String MSG_CREATE_ROOM = "MsgCreateRoom";
        public static final String MSG_ENTER_ROOM = "MsgEnterRoom";
        public static final String MSG_GET_ROOM_INFO = "MsgGetRoomInfo";
        public static final String MSG_LEAVE_ROOM = "MsgLeaveRoom";
        public static final String MSG_START_BATTLE = "MsgStartBattle";

    }
    public static final class Sync {
        public static final String MSG_SYNC_TANK = "MsgSyncTank";
        public static final String MSG_FIRE = "MsgFire";
        public static final String MSG_HIT = "MsgHit";
    }
    public static final class SysMsg {
        public static final String MSG_PING = "MsgPing";
        public static final String MSG_PONG = "MsgPong";
    }

}
