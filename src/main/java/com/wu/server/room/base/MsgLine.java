package com.wu.server.room.base;

import com.wu.server.proto.base.MsgBase;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 房间消息队列
 */
public  class MsgLine extends ConcurrentLinkedQueue<MsgBase> {
    /**
     * 队列初始化
     *
     */
    public MsgLine() {
        super();
    }


}
