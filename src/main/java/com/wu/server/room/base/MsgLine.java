package com.wu.server.room.base;

import com.wu.server.proto.base.MsgBase;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 房间消息队列
 */
public class MsgLine extends ArrayBlockingQueue<MsgBase> {
    /**
     * 队列初始化
     * @param capacity
     */
    public MsgLine(int capacity) {
        super(capacity);
    }



}
