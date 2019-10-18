package com.wu.server.netty;

import com.sun.istack.internal.NotNull;
import com.wu.server.bean.User;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.service.RoomService;
import com.wu.server.status.DataManage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public interface NetManage {

    //通过玩家找玩家发送消息
    public static void send(@NotNull  String id , @NotNull MsgBase msgBase){
        send(DataManage.INSTANCE.onLineUser.get(id) , msgBase);
    }

    public static void send(@NotNull User user,@NotNull MsgBase msgBase){
        //user通道空
        if (user.getChannel() == null) return;
        Channel channel = user.getChannel();
        ByteBuf byteBuf = MsgBase.Encode(channel.alloc().ioBuffer(), msgBase);
        channel.writeAndFlush(byteBuf);
    }

}
