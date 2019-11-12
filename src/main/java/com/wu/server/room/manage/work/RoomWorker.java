package com.wu.server.room.manage.work;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.Status;
import com.wu.server.bean.User;
import com.wu.server.netty.NetManage;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.base.RoomInfo;
import com.wu.server.proto.net.*;
import com.wu.server.proto.system.MsgOffline;
import com.wu.server.proto.system.MsgReconnect;
import com.wu.server.room.base.MsgLine;
import com.wu.server.room.base.Room;
import com.wu.server.status.DataManage;
import java.util.HashMap;

public class RoomWorker implements Runnable {
   //配置
   /**********************************************************************/
    //工作线程最大工作量
    public static final int THREAD_WORKING_ROOM_MAX = 2;
    //设置坦克的伤害
    public static final int TANK_DAMAGE = 35;
    /**********************************************************************/

    //房间待处理消息
    public MsgLine workerRoomPendingMsg = new MsgLine();
    //注册在该线程下的房间
    private  HashMap<Integer, Room> roomHashMap = new HashMap<>(THREAD_WORKING_ROOM_MAX);
    //管理房间数目
    public volatile int manageRoomNumber = 0;
    //添加房间
    public synchronized void addRoom (int roomId , String userId){
       //初始化房间
        Room room = new Room(roomId,userId);
        roomHashMap.put(roomId,room);
        this.manageRoomNumber ++;
    }
    public synchronized  void removeRoom(int roomId){
        roomHashMap.remove(roomId);
        DataManage.INSTANCE.findRoomWorker.remove(roomId);
        this.manageRoomNumber --;
    }



    public RoomWorker() {
    }

    @Override
    public void run() {
        LogUntil.logger.info("工作线程启动***************************");
        while (!Thread.interrupted()){
            //消息处理
            messageProcessing();
            //ai机器人
            robotAi();
            //房间管理的房间游戏状态：判断游戏结束
            manageRoom();


        }
    }

    /**
     * 处理房间各种消息
     */
    private void messageProcessing(){
        try {

            if (workerRoomPendingMsg.isEmpty())  return;
            MsgBase msgBase = workerRoomPendingMsg.poll();
// todo            LogUntil.logger.debug(this +" Receive" + msgBase);

            switch (msgBase.protoName){
                case MsgName.Room.MSG_GET_ROOM_INFO : onMsgGetRoomInfo(msgBase);    break;
                case MsgName.Room.MSG_ENTER_ROOM :   onMsgEnterRoom(msgBase);   break;
                case MsgName.Room.MSG_LEAVE_ROOM : onMsgLeaveRoom(msgBase); break;
                case MsgName.Room.MSG_START_BATTLE : onMsgStartBattle(msgBase); break;
                case MsgName.Sync.MSG_FIRE : onMsgFire(msgBase);    break;
                case MsgName.Sync.MSG_HIT : onMsgHit(msgBase);  break;
                case MsgName.Sync.MSG_SYNC_TANK : onMsgSyncTank(msgBase);   break;
                case MsgName.Login.MSG_OFF_LINE : onMsgOffline(msgBase);   break;
                case MsgName.Login.MSG_RECONNECT : onMsgReconnect(msgBase); break;
                default: return;
            }
        } catch (Exception e) {
            LogUntil.logger.error(e.toString());
        }
    }



    /**
     * 获取房间状态
     * @param roomId
     * @return
     */
    public  RoomInfo  getRoomStatus(int roomId){
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.id = roomId;
        roomInfo.status = roomHashMap.get(roomId).status;
        roomInfo.count = roomHashMap.get(roomId).playerIds.size();
        return roomInfo;
    }


    /**
     * 接管掉线
     * todo 掉线由机器人控制
     */
    private void robotAi() {
        //todo
    }

    /**
     * 房间管理
     * 1.判断游戏结束
     * 2.其他管理
     */
    private void manageRoom() {
        if(roomHashMap.size() == 0 ) return;

        for (int roomId: roomHashMap.keySet()) {

           Room room = roomHashMap.get(roomId);
           if (room != null && room.status == Status.FIGHT)
               room.Update();
        }
    }


    //处理MsgGetRoomInfo协议
    private void onMsgGetRoomInfo(MsgBase msgBase){
        MsgGetRoomInfo msgGetRoomInfo = (MsgGetRoomInfo) msgBase;
        Room room = roomHashMap.get(DataManage.INSTANCE.onLineUser.get(msgGetRoomInfo.id).roomId);
        if(room == null ) return;
        room.Broadcast( room.ToMsg(msgGetRoomInfo));
    }

    //处理MsgEnterRoom协议
    private void  onMsgEnterRoom(MsgBase msgBase){
        MsgEnterRoom msgEnterRoom = (MsgEnterRoom) msgBase;

        //已经在房间中
        if(DataManage.INSTANCE.onLineUser.get(msgEnterRoom.id).roomId > -1){
            msgEnterRoom.result = 1;
            NetManage.send(msgEnterRoom.id,msgEnterRoom);
            return;
        }

        //获取房间
        Room room = roomHashMap.get(msgEnterRoom.roomId);
        if(room == null ){
            msgEnterRoom.result = 1;
            NetManage.send(msgEnterRoom.id,msgEnterRoom);
            return;
        }
        if (!room.AddPlayer(msgEnterRoom.id)){
            msgEnterRoom.result = 1;
            NetManage.send(msgEnterRoom.id,msgEnterRoom);
            return;
        }

        msgEnterRoom.result = 0;
        NetManage.send(msgEnterRoom.id, msgEnterRoom);

    }

    //处理MsgLeaveRoom协议
    public void onMsgLeaveRoom(MsgBase msgBase){
        MsgLeaveRoom msgLeaveRoom = (MsgLeaveRoom) msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgLeaveRoom.id);

        if (user == null){
            msgLeaveRoom.result = 1;
            NetManage.send(msgLeaveRoom.id, msgLeaveRoom);
            return;
        }
        int roomId = user.roomId;
        Room room = roomHashMap.get(roomId);
        if ( room == null ){
            msgLeaveRoom.result = 1;
            NetManage.send(msgLeaveRoom.id, msgLeaveRoom);
            return;
        }

        if (!room.RemovePlayer(msgLeaveRoom.id)){
            msgLeaveRoom.result = 1;
            NetManage.send(msgLeaveRoom.id, msgLeaveRoom);
            return;
        }


        //玩家为空清除房间
        if (room.playerIds.size() == 0){
            removeRoom(roomId);
        }
        msgLeaveRoom.result = 0;
        NetManage.send(msgLeaveRoom.id,msgLeaveRoom);

    }

    //处理MsgStartBattle协议
    private void onMsgStartBattle(MsgBase msgBase) {
        MsgStartBattle msgStartBattle = (MsgStartBattle) msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgStartBattle.id);

        if(user == null) return;
        //room
        Room room = roomHashMap.get(user.roomId);
        if(room == null){
            msgStartBattle.result = 1;
            NetManage.send(user,msgStartBattle);
            return;
        }
        //checkRoomMember
        if(!room.isRoomMember(user)){
            return;
        }
        //是否是房主
        if(!room.isOwner(msgStartBattle.id)){
            msgStartBattle.result = 1;
            NetManage.send(user,msgStartBattle);
            return;
        }
        //开战
        if(!room.StartBattle()){
            msgStartBattle.result = 1;
            NetManage.send(user,msgStartBattle);
            return;
        }
        //成功
        msgStartBattle.result = 0;
        NetManage.send(user,msgStartBattle);

    }

    //处理MsgFire协议
    private void onMsgFire(MsgBase msgBase) {
        MsgFire msgFire = (MsgFire) msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgFire.id);
        if(user == null) return;
        //room
        Room room = roomHashMap.get(user.roomId);
        if(room == null){
            return;
        }
        //checkRoomMember
        if(!room.isRoomMember(user)){
            return;
        }
        //status
        if(room.status != Status.FIGHT){
            return;
        }
        //广播
        room.Broadcast(msgFire);
    }


    //处理MsgHit协议
    private void onMsgHit(MsgBase msgBase) {
        MsgHit msgHit = (MsgHit)msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgHit.id);
        if(user == null) return;
        //targetPlayer
        User targetPlayer = DataManage.INSTANCE.onLineUser.get(msgHit.targetId);
        if(targetPlayer == null){
            return;
        }
        //room
        Room room = roomHashMap.get(user.roomId);
        if(room == null){
            return;
        }
        //status
        if(room.status != Status.FIGHT){
            return;
        }
        //checkRoomMember
        if(!room.isRoomMember(user)){
            return;
        }
        //状态
        if (!room.roomMemberAttacked(msgHit.targetId)){
            return;
        }
        //广播
        msgHit.damage = TANK_DAMAGE;
        room.Broadcast(msgHit);
    }


    private void onMsgSyncTank(MsgBase msgBase) {
        MsgSyncTank msgSyncTank = (MsgSyncTank) msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgSyncTank.id);
        if(user == null) return;
        //room
        Room room = roomHashMap.get(user.roomId);
        if(room == null){
            return;
        }

        //status
        if(room.status != Status.FIGHT){
            return;
        }

        //checkRoomMember
        if(!room.isRoomMember(user)){
            return;
        }
        //数据验证
        if(!room.dataValidation(msgSyncTank)){
            msgSyncTank.result = 1;

        }

        //广播
        room.Broadcast(msgSyncTank);
    }

    /**
     * 掉线玩家处理
     * @param msgBase
     */
    private void onMsgOffline(MsgBase msgBase) {
        MsgOffline msgOffline = (MsgOffline) msgBase;
        Room room = roomHashMap.get(msgOffline.roomId);
        if (room == null){
            DataManage.INSTANCE.onLineUser.remove(msgOffline.id);
            return;
        }

        if (!room.addOfflineMember(msgOffline.id)){
            DataManage.INSTANCE.onLineUser.remove(msgOffline.id);
            //玩家为空清除房间
            if (room.playerIds.size() == 0){
                removeRoom(room.roomId);
            }
            return;
        }


        MsgBattleText msgBattleText = new MsgBattleText();
        msgBattleText.reconnectText(msgOffline.id);
        //room.Broadcast(msgBattleText);

    }

    private void onMsgReconnect(MsgBase msgBase) {
        MsgReconnect msgReconnect = (MsgReconnect) msgBase;
        User user = DataManage.INSTANCE.onLineUser.get(msgReconnect.id);
        if(user == null) return;
        //room
        Room room = roomHashMap.get(user.roomId);
        if(room == null){
            return;
        }

        if(!room.removeOfflineMember(user.getId())){
            return;
        }

        NetManage.send(user,room.recoomectBattle());
    }


}
