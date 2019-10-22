package com.wu.server.room.manage.boss;

import com.wu.server.Until.LogUntil;
import com.wu.server.netty.NetManage;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.base.RoomInfo;
import com.wu.server.proto.net.MsgCreateRoom;
import com.wu.server.proto.net.MsgGetRoomList;
import com.wu.server.room.base.MsgLine;
import com.wu.server.room.manage.work.RoomWorker;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

public class RoomBoss implements Runnable {

   //配置
    /**********************************************************************/
    //消息处理最大量
    static final int THREAD_MSG_MAX = 10;
    static final int WORKER_NUMBER =1;
    /**********************************************************************/
    static int roomId = 0;

    private ExecutorService exec;
    //消息队列
    public MsgLine msgPending = new MsgLine(THREAD_MSG_MAX);
    //工作中线程
    private LinkedList<RoomWorker> workingRoomWorker = new LinkedList<>();
    //闲置线程
    private Queue<RoomWorker> idleRoomWorker = new LinkedList<>();
    //房间管理的睡眠度
    private int adjustmentPeriod ;
    //采取懒汉模式构造roomBoss线程
    //房间和其注册的线程key:roomId,value:RoomWorker
    public static ConcurrentHashMap<Integer, RoomWorker> findRoomWorker = new ConcurrentHashMap<>();
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
        init();
    }


    @Override
    public void run() {
        while(!Thread.interrupted()){
            //消息处理
            messageProcessing();
            //工作线程调度
            adjustWorkerNumber();
            //管理findRoomWorker
            adjustFindRoomWorker();
        }
    }


    /**
     * 初始化
     */
    private void init() {
        for (int i = 0 ;i < WORKER_NUMBER ;i++){
            RoomWorker roomWorker = new RoomWorker();
            idleRoomWorker.add(roomWorker);
            exec.execute(new RoomWorker());
        }
    }

    /**
     * 消息处理
     */

    // todo 消息重复处理，设一个值，达到规定值丢弃（返回失败）
    private void messageProcessing(){
        try {
            MsgBase msgBase = msgPending.take();
            if (msgBase.protoName.equals(MsgName.Room.MSG_CREATE_ROOM)){
                MsgCreateRoom msgCreateRoom = (MsgCreateRoom) msgBase;
                // 创建房间逻辑 todo test
                RoomWorker worker  = idleRoomWorker.peek();
                LogUntil.logger.debug(roomId+"    "+worker);
                //无空闲房间，重新加载
                if (worker == null){
                    msgPending.add(msgBase);
                    return;
                }
                findRoomWorker.put(roomId,worker);
                worker.addRoom(roomId,msgCreateRoom.id);
                msgCreateRoom.result = 0;
                roomId++;
                NetManage.send(msgCreateRoom.id , msgCreateRoom);
            }
            else if(msgBase.protoName.equals(MsgName.Room.MSG_GET_ROOM_LIST)){
                // 获取房间消息处理  todo test
                MsgGetRoomList msgGetRoomList = (MsgGetRoomList) msgBase;
                //遍历房间消息发送出去
                msgGetRoomList.rooms = new RoomInfo[findRoomWorker.size()];
                int i = 0 ;
                for (Map.Entry<Integer, RoomWorker> entry : findRoomWorker.entrySet()) {
                    msgGetRoomList.rooms[i] = entry.getValue().getRoomStatus(entry.getKey());
                    i++;
                }
                NetManage.send(msgGetRoomList.id,msgGetRoomList);
            }


        } catch (InterruptedException e) {
            LogUntil.logger.error(e.toString());
        }
    }


    /**
     * 房间管理线程管理
     */
    public void adjustWorkerNumber(){
        // todo test
        RoomWorker roomWorker = idleRoomWorker.peek();
        //闲置工作线程负载满额调入工作线程中
        if (roomWorker.manageRoomNumber == roomWorker.THREAD_WORKING_ROOM_MAX){
            workingRoomWorker.add(idleRoomWorker.poll());
        }

    }

    /**
     * FindRoomWorker管理
     */
    private void adjustFindRoomWorker() {
        //todo test
        for (int i = 0; i < workingRoomWorker.size(); i++) {
           RoomWorker roomWorker = workingRoomWorker.get(i);
           //遍历工作线程有闲置的重新加入闲置闲置线程之上
           if(roomWorker.manageRoomNumber < RoomWorker.THREAD_WORKING_ROOM_MAX){
               idleRoomWorker.add(roomWorker);
               workingRoomWorker.remove(i);
           }
        }
    }
}
