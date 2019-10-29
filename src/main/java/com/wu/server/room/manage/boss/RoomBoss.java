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
import com.wu.server.status.DataManage;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

public class RoomBoss implements Runnable {

   //配置
    /**********************************************************************/
    //消息处理最大量
    private static final int WORKER_NUMBER =5;
    private static final int LEAST_WORKER_NUMBER = 2;
    /**********************************************************************/
    static int roomId = 0;

    private ExecutorService exec;
    //消息队列
    public MsgLine roomBossMsgPending = new MsgLine();

    //负载线程
    private static LinkedList<RoomWorker> workingRoomWorker = new LinkedList<>();
    //闲置线程
    public static ConcurrentLinkedQueue<RoomWorker> idleRoomWorker = new ConcurrentLinkedQueue<>();
    //采取懒汉模式构造roomBoss线程

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
        //房间管理的睡眠度
        init();
    }


    @Override
    public void run() {
        while(!Thread.interrupted()){

            //消息处理
            messageProcessing();
            //人力资源
            humanResource();
            //管理负载线程
            adjustWorkingRoomWorker();
            //管理空闲线程
            adjustIdleRoomWorker();

        }
    }


    /**
     * 初始化工作线程
     */
    private void init() {
        for (int i = 0 ;i < WORKER_NUMBER ;i++){
            RoomWorker roomWorker = new RoomWorker();
            idleRoomWorker.add(roomWorker);
            exec.execute(roomWorker);
        }
    }

    /**
     * 消息处理
     */

    // todo 消息重复处理，设一个值，达到规定值丢弃（返回失败）
    private void messageProcessing(){
        try {
            if (this.roomBossMsgPending.isEmpty() ) return;
            MsgBase msgBase = roomBossMsgPending.poll();
            LogUntil.logger.debug(this +" Receive " + msgBase);//todo test  RoomBoss receive msg
            if (msgBase.protoName.equals(MsgName.Room.MSG_CREATE_ROOM)){
                MsgCreateRoom msgCreateRoom = (MsgCreateRoom) msgBase;
                // 创建房间逻辑 todo test
                RoomWorker worker  = idleRoomWorker.peek();
                LogUntil.logger.debug(roomId+"    "+worker);
                //无空闲房间，重新加载
                if (worker == null){
                    roomBossMsgPending.add(msgBase);
                    return;
                }
                DataManage.INSTANCE.findRoomWorker.put(roomId,worker);

                worker.addRoom(roomId,msgCreateRoom.id);
                msgCreateRoom.result = 0;
                roomId++;
                NetManage.send(msgCreateRoom.id , msgCreateRoom);
            }
            else if(msgBase.protoName.equals(MsgName.Room.MSG_GET_ROOM_LIST)){
                // 获取房间消息处理
                MsgGetRoomList msgGetRoomList = (MsgGetRoomList) msgBase;
                //遍历房间消息发送出去
                msgGetRoomList.rooms = new RoomInfo[DataManage.INSTANCE.findRoomWorker.size()];
                int i = 0 ;
                for (Map.Entry<Integer, RoomWorker> entry : DataManage.INSTANCE.findRoomWorker.entrySet()) {
                    msgGetRoomList.rooms[i] = entry.getValue().getRoomStatus(entry.getKey());
                    i++;
                }
                NetManage.send(msgGetRoomList.id,msgGetRoomList);
            }


        } catch (Exception e) {
            LogUntil.logger.error(e.toString());
        }
    }


    /**
     *根据具体情况进行雇员和裁员
     */
    public void humanResource(){
        // todo need test
        if(idleRoomWorker.size() < LEAST_WORKER_NUMBER){
            RoomWorker roomWorker = new RoomWorker();
            exec.execute(roomWorker);
            idleRoomWorker.add(roomWorker);
        }
        //TODO
//        if (idleRoomWorker.size()>WORKER_NUMBER){
//            RoomWorker roomWorker =idleRoomWorker.peek();
//            if (roomWorker.manageRoomNumber == 0){
//                idleRoomWorker.poll()                                                               ;
//
//            }
//
//        }

    }

    /**
     * 管理idleRoomWorker
     * 将负载的线程移入workingRoomWorker
     * 不再添加房间管理
     */

    private void adjustIdleRoomWorker(){
        RoomWorker roomWorker = idleRoomWorker.peek();
        if (roomWorker.manageRoomNumber == roomWorker.THREAD_WORKING_ROOM_MAX){
            workingRoomWorker.add(idleRoomWorker.poll());
        }
    }

    /**
     * WorkingRoomWorker管理
     * 管理负载线程将有空闲的线程加入到闲置线程
     */
    private void adjustWorkingRoomWorker() {

        for (int i = 0; i < workingRoomWorker.size(); i++) {
           RoomWorker roomWorker = workingRoomWorker.get(i);
           //遍历负载线程有空闲的的线程重新加入闲置线程之上
           if(roomWorker.manageRoomNumber < RoomWorker.THREAD_WORKING_ROOM_MAX){
               idleRoomWorker.add(roomWorker);
               workingRoomWorker.remove(i);
           }
        }
    }
}
