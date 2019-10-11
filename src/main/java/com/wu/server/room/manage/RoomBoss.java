package com.wu.server.room.manage;

import com.wu.server.room.material.MsgLine;
import com.wu.server.room.work.RoomWorker;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class RoomBoss implements Runnable {
    //

    private ExecutorService exec;
    //消息队列
    private MsgLine msg = new MsgLine(50);
    //工作中线程
    private PriorityQueue<RoomWorker> workingRoomWorker = new PriorityQueue<>();
    //闲置线程
    private Queue<RoomWorker> idleRoomWorker = new LinkedList<>();
    //房间管理的睡眠度
    private int adjustmentPeriod ;

    /**
     * @param exec
     * @param msg
     * @param adjustmentPeriod
     */
    public RoomBoss(ExecutorService exec, MsgLine msg, int adjustmentPeriod) {
        this.exec = exec;
        this.msg = msg;
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
