package com.wu.server.room.work;

public class RoomWorker implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()){}
    }
}
