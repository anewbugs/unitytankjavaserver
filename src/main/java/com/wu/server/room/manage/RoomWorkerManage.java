package com.wu.server.room.manage;

import com.wu.server.room.material.MsgLine;
import com.wu.server.room.work.RoomWorker;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class RoomWorkerManage implements Runnable {
    //
    private ExecutorService exec;
    //消息队列
    private MsgLine msgs;
    //工作中线程
    private PriorityQueue<RoomWorker> workingRoomWorker = new PriorityQueue<>();
    //闲置线程
    private Queue<RoomWorker> idleRoomWorker = new LinkedList<>();
    //房间管理的睡眠度
    private int adjustmentPeriod ;

    /**
     * @param exec
     * @param msgs
     * @param adjustmentPeriod
     */
    public RoomWorkerManage(ExecutorService exec, MsgLine msgs, int adjustmentPeriod) {
        this.exec = exec;
        this.msgs = msgs;
        this.adjustmentPeriod = adjustmentPeriod;
    }

    /**
     * 房间管理线程管理
     */
    public void adjustWorkerNumber(){

    }

    @Override
    public void run() {
        while(!Thread.interrupted()){

        }
    }
}
