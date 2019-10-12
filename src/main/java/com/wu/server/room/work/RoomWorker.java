package com.wu.server.room.work;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.Room;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.room.material.MsgLine;
import com.wu.server.status.Configure;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class RoomWorker implements Runnable {
    //房间待处理消息

    private MsgLine pendingMsg = new MsgLine(10);
    //注册在该线程下的房间
    private HashMap<Integer, Room> room = new HashMap<>(Configure.THREAD_WORKING_ROOM);
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
    }
    public synchronized  void removeRoom(int roomId){
        room.remove(roomId);
    }
    public synchronized int getNumberOfRoom(){
        return room.size();
    }
}
