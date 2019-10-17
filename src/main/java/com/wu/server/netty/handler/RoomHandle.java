package com.wu.server.netty.handler;

import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.status.DataManage;
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
        if(msgbase.protoName.equals(MsgName.Room.MSG_CREATE_ROOM) ||
                msgbase.protoName.equals(MsgName.Room.MSG_GET_ROOM_LIST) ){
            //创建房间消息转发给RoomBoss线程取处理 todo Boss线程处理该消息
            RoomBoss.getInstance().msgPending.add(msgbase);
        }else{
            //TODO 房间其他消息转发给房间工作者线程自己
        }
    }
}
