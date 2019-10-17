package com.wu.server.room.manage.boss;

import com.wu.server.room.base.MsgLine;
import com.wu.server.room.manage.work.RoomWorker;
import javafx.concurrent.Worker;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class RoomBoss implements Runnable {

   //配置
    /**********************************************************************/
    //消息处理最大量
    static final int THREAD_MSG_MAX = 10;
    static final int WORKER_NUMBER =10;
    /**********************************************************************/


    private ExecutorService exec;
    //消息队列
    public MsgLine msgPending = new MsgLine(THREAD_MSG_MAX);
    //工作中线程
    private PriorityQueue<RoomWorker> workingRoomWorker = new PriorityQueue<>();
    //闲置线程
    private Queue<RoomWorker> idleRoomWorker = new LinkedList<>();
    //房间管理的睡眠度
    private int adjustmentPeriod ;
    //采取懒汉模式构造roomBoss线程
    //房间和其注册的线程key:roomId,value:RoomWorker
    public ConcurrentHashMap<Integer, RoomWorker> findRoomWorker = new ConcurrentHashMap<>();
    private static RoomBoss roomBoss;
    public static void init(ExecutorService exec, int adjustmentPeriod) {
            roomBoss = new RoomBoss(exec,adjustmentPeriod);
    }
    public static RoomBoss getInstance(){
        return roomBoss;
    }

    /**
     * @param exec
     * @param adjustmentPeriod
     */
    private RoomBoss(ExecutorService exec, int adjustmentPeriod) {
        this.exec = exec;
        this.adjustmentPeriod = adjustmentPeriod;
    }

    /**
     * 房间管理线程管理
     */
    public void adjustWorkerNumber(){

    }

    @Override
    public void run() {
        init();
        while(!Thread.interrupted()){

        }
    }

    private void init() {

        for (int i = 0 ;i < WORKER_NUMBER ;i++){
            exec.execute(new RoomWorker());
        }
    }
}
