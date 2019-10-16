package com.wu.server.room.manage.work;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.Room;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.room.base.MsgLine;

import java.lang.reflect.Method;
import java.util.HashMap;

public class RoomWorker implements Runnable {
   //配置
   /**********************************************************************/
    //工作线程最大工作量
    public static final int THREAD_WORKING_ROOM_MAX = 2;
    //消息处理最大量
    private static final int THREAD_MSG_MAX = 10;
    /**********************************************************************/


    //房间待处理消息
    public MsgLine pendingMsg = new MsgLine(THREAD_MSG_MAX);
    //注册在该线程下的房间
    private HashMap<Integer, Room> room = new HashMap<>(THREAD_WORKING_ROOM_MAX);
    //管理房间数目
    private volatile int manageRoomNumber = 0;
    Method method;
    @Override
    public void run() {
        while (!Thread.interrupted()){
            try {
                MsgBase msg = pendingMsg.take();

            } catch (InterruptedException e) {
                LogUntil.logger.debug(e.toString());
            }
        }
    }

    public synchronized void addRoom (int roomId){
        room.put(roomId,new Room());
        this.manageRoomNumber ++;
    }
    public synchronized  void removeRoom(int roomId){
        room.remove(roomId);
        this.manageRoomNumber --;
    }
    public synchronized int getNumberOfRoom(){
        return manageRoomNumber;
    }
}
