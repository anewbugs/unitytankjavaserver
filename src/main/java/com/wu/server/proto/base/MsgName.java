package com.wu.server.proto.base;

public class MsgName {
    public static final class Battle {
        public static final String MSGENTERBATTLE = "MsgEnterBattle";
        public static final String MSGBATTLERESULT = "MsgBattleResult";
        public static final String MSGLEAVEBATTLE = "MsgLeaveBattle";
    }
    public static final class Login {
        public static final String MSGREGISTER = "MsgRegister";
        public static final String MSGLOGIN = "MsgLogin";
        public static final String MSGKICK = "MsgKick";
    }
    public static final class Notepad {
        public static final String MSGGETTEXT = "MsgGetText";
        public static final String MSGSAVETEXT = "MsgSaveText";
    }

    public static final class Room {
        public static final String MSGGETACHIEVE = "MsgGetAchieve";
        public static final String MSGGETROOMLIST = "MsgGetRoomList";
        public static final String MSGCREATEROOM = "MsgCreateRoom";
        public static final String MSGENTERROOM = "MsgEnterRoom";
        public static final String MSGGETROOMINFO = "MsgGetRoomInfo";
        public static final String MSGLEAVEROOM = "MsgLeaveRoom";
        public static final String MSGSTARTBATTLE = "MsgStartBattle";

    }
    public static final class Sync {
        public static final String MSGSYNCTANK = "MsgSyncTank";
        public static final String MSGFIRE = "MsgFire";
        public static final String MSGHIT = "MsgHit";
    }
    public static final class SysMsgId {
        public static final String MSGPING = "MsgPing";
        public static final String MSGPONG = "MsgPong";
    }

}
