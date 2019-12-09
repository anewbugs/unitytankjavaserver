package com.wu.server;

import com.wu.server.Until.LogUntil;
import com.wu.server.navmesh.load.NavMeshLoad;
import com.wu.server.navmesh.mesh.NavMeshInfo;
import com.wu.server.netty.NetServer;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.status.DataManage;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class Main {

    //房间轮询时间
    static final int ADJUSTMENT_PERIOD = 10;
    static final String PROGRAM_END = "QUIT";
    static final long awaitTime = 5 * 1000;
    //初始工作线程数目
    static final int WORKER_THREAD_INTI_NUMBER = 10;
    public static void main(String[] args) {

        ExecutorService exec = Executors.newCachedThreadPool();
        //初始化
        RoomBoss.init(exec,ADJUSTMENT_PERIOD);
        //加载地图资源
        NavMeshInfo navMeshInfo = new NavMeshLoad().load("Mian.obj");
        DataManage.INSTANCE.addMap(navMeshInfo);
        //RoomBoss线程跑起来
        exec.execute(RoomBoss.getInstance());
        //通信线程跑起来
        exec.execute(new NetServer());
        //控制台结束程序
        boolean IsEnd = false;
        Scanner input = new Scanner(System.in);
        while(!IsEnd){
            IsEnd = input.nextLine().equals(PROGRAM_END);
        }

            exec.shutdownNow();
            LogUntil.logger.info("PROGRAM END !!!");

    }

}