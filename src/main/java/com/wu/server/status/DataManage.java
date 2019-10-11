package com.wu.server.status;

import com.wu.server.bean.User;
import com.wu.server.room.work.RoomWorker;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 采取恶汉设计模式管理在线玩家
 */
public class DataManage
{   //在线用户key:id,value:user
    public ConcurrentHashMap<String, User> onLineUser;
    //连接记入 key:channel,value:id
    public ConcurrentHashMap<ChannelHandlerContext, String> connection;
    //房间和其注册的线程key:roomId,value:RoomWorker
    public ConcurrentHashMap<Integer, RoomWorker> findRoomWorker;
    public DataManage() {
        onLineUser = new ConcurrentHashMap<>();
        connection = new ConcurrentHashMap<>();
        findRoomWorker = new ConcurrentHashMap<>();
    }
    public static final DataManage INSTANCE = new DataManage();

}
