package com.wu.server.room.manage.work;

import com.wu.server.Until.LogUntil;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.base.RoomInfo;
import com.wu.server.proto.net.MsgGetRoomInfo;
import com.wu.server.room.base.MsgLine;
import com.wu.server.room.base.Room;
import com.wu.server.status.DataManage;

import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

public class RoomWorker implements Runnable {
   //配置
   /**********************************************************************/
    //工作线程最大工作量
    public static final int THREAD_WORKING_ROOM_MAX = 2;
    //消息处理最大量
    private static final int THREAD_MSG_MAX = 10;
    /**********************************************************************/


    //房间待处理消息
    public ArrayBlockingQueue<MsgBase> pendingMsg = new ArrayBlockingQueue<MsgBase>(THREAD_MSG_MAX);
    //注册在该线程下的房间
    private HashMap<Integer, Room> roomHashMap = new HashMap<>(THREAD_WORKING_ROOM_MAX);
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
        this.manageRoomNumber --;
    }
    public synchronized int getNumberOfRoom(){
        return manageRoomNumber;
    }



    public RoomWorker() {
    }

    @Override
    public void run() {
        LogUntil.logger.info(this.toString());
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
            while(true){
                System.out.println(pendingMsg.peek());
                if(pendingMsg.peek() != null) break;
                Thread.sleep(1000);
            }

            MsgBase msgBase = pendingMsg.take();
            switch (msgBase.protoName){
                case MsgName.Room.MSG_GET_ROOM_INFO :
                    //  获取房间内信息 todo test
                    MsgGetRoomInfo msgGetRoomInfo = (MsgGetRoomInfo) msgBase;
                    Room room = roomHashMap.get(DataManage.INSTANCE.onLineUser.get(msgGetRoomInfo.id).roomId);
                    room.Broadcast( room.ToMsg(msgGetRoomInfo));
                    break;
                case MsgName.Room.MSG_ENTER_ROOM :
                    // todo 进入房间消息处理
                    break;
                case  MsgName.Room.MSG_LEAVE_ROOM :
                    //todo 离开房间消息处理
                    break;
                case MsgName.Room.MSG_START_BATTLE :
                    //todo 开战消息处理
                    break;
                case MsgName.Sync.MSG_FIRE :
                    // todo 开火消息处理
                    break;
                case MsgName.Sync.MSG_HIT :
                    // todo 击中协议处理
                    break;
                case MsgName.Sync.MSG_SYNC_TANK :
                    // todo tank移动消息处理
                    break;
            }
        } catch (InterruptedException e) {
            LogUntil.logger.error(e.toString());
        }
    }

    /**
     * 获取房间状态
     * @param roomId
     * @return
     */
    public synchronized RoomInfo  getRoomStatus(int roomId){
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.id = roomId;
        roomInfo.count = roomHashMap.get(roomId).status;
        roomInfo.count = roomHashMap.get(roomId).playerIds.size();
        return roomInfo;
    }

    /**
     * 掉线由机器人控制
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
        //todo
    }

}
