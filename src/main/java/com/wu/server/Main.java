package com.wu.server;

import com.wu.server.Until.LogUntil;
import com.wu.server.netty.NetServer;
import com.wu.server.room.manage.boss.RoomBoss;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    //房间轮询时间
    static final int ADJUSTMENT_PERIOD = 10;
    static final String PROGRAM_END = "QUIT";
    static final long awaitTime = 5 * 1000;
    //初始工作线程数目
    static final int WORKER_THREAD_INTI_NUMBER = 10;
    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        //RoomBoss线程跑起来
        exec.execute(RoomBoss.getInstance(exec,ADJUSTMENT_PERIOD));
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
