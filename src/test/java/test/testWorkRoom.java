package test;
;
import com.wu.server.proto.net.MsgFire;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.room.manage.work.RoomWorker;
import com.wu.server.status.DataManage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testWorkRoom {
    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        RoomBoss.init(exec,10);
        exec.execute(RoomBoss.getInstance());
        exec.execute(new Runnable() {
            @Override
            public void run() {
                DataManage.INSTANCE.findRoomWorker.put(0,RoomBoss.idleRoomWorker.peek());
            }
        });
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);

                        DataManage.INSTANCE.findRoomWorker.get(0).workerRoomPendingMsg.add(new MsgFire());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


}
