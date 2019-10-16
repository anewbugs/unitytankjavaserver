package com.wu.server.status;

import com.wu.server.bean.User;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 采取恶汉设计模式管理在线玩家
 * *connection记录登入用户
 * *onLineUser记录登入后的玩家,在游戏中掉线的玩家
 * *当玩家下线以后,先删除连接玩家在根据玩家状态删除或“标记”
 */
public class DataManage
{   //在线用户key:id,value:user
    public ConcurrentHashMap<String, User> onLineUser;
    //连接记入 key:channel,value:id
    public ConcurrentHashMap<ChannelHandlerContext, String> connection;

    public DataManage() {
        onLineUser = new ConcurrentHashMap<>();
        connection = new ConcurrentHashMap<>();

    }
    public static final DataManage INSTANCE = new DataManage();

}
