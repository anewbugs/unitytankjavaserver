package test;

import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.net.MsgFire;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.room.manage.work.RoomWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class testWorkRoom {
    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        RoomBoss.init(exec,10);

        RoomWorker roomWorker = new RoomWorker();
        exec.execute(roomWorker);
        exec.execute(new Runnable() {
            @Override
            public void run() {
                RoomBoss.findRoomWorker.put(0,roomWorker);
            }
        });
        exec.execute(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                        RoomBoss.findRoomWorker.get(0).putMsg(new MsgFire());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }


}
