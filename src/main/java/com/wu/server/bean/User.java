package com.wu.server.bean;

import com.sun.istack.internal.NotNull;
import io.netty.channel.Channel;

public class User {
    private String id ;
    private Channel channel;
    public volatile int status =Status.PREPARE;
    public volatile boolean isUser = true;
    public volatile int roomId = -1;//-1表示房间号不存在
    public PlayerData playerData;

    public User(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel( Channel channel) {
        this.channel = channel;
    }

}
