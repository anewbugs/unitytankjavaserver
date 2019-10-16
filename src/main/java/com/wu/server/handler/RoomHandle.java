package com.wu.server.handler;

import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author wu
 * 房间有关消息处理
 */
public class RoomHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MsgBase msgbase = (MsgBase) msg;
        if(msgbase.protoName.equals(MsgName.Room.MSG_CREATE_ROOM)){
            //创建房间消息转发给RoomBoss线程取处理

        }else{
            //房间其他消息转发给房间工作者线程自己处理

        }
    }
}
