package com.wu.server.room.work;

import com.wu.server.proto.base.MsgBase;
import com.wu.server.room.material.MsgLine;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class RoomWorker implements Runnable {
    //房间待处理消息
    private MsgLine pendingMsg;
    //注册在该线程下的房间

    @Override
    public void run() {
        while (!Thread.interrupted()){}
    }
}
