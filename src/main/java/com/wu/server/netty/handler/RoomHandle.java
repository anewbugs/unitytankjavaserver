package com.wu.server.netty.handler;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.User;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.net.MsgFire;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.status.DataManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.scene.layout.BorderStrokeStyle;

import java.lang.reflect.Field;

/**
 * @author wu
 * 房间有关消息处理
 */
public class RoomHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        MsgBase msgbase = (MsgBase) msg;
        if(msgbase.protoName.equals(MsgName.Room.MSG_CREATE_ROOM) ||
                msgbase.protoName.equals(MsgName.Room.MSG_GET_ROOM_LIST) ){
            //创建房间消息转发给RoomBoss线程取处理 todo Boss线程处理该消息
            try {
                RoomBoss.getInstance().roomBossMsgPending.add(msgbase);
            } catch (Exception e) {
                LogUntil.logger.error(e.toString());
            }
        }else{
            // 房间其他消息转发给房间工作者线程自己

            try {
                //反射获取id
                Field field = msgbase.getClass().getField("id");
                String userId = field.get(msgbase).toString();

                User user = DataManage.INSTANCE.onLineUser.get(userId);
                if (user == null) return;
                //通过玩家id获取user中房间id
                int roomId =user.roomId;
                //将消息分发工作线程 todo 工作线程处理这些消息
                if(roomId > -1) {
                    DataManage.INSTANCE.findRoomWorker.get(roomId).workerRoomPendingMsg.add(msgbase);

                }
                else{
                    field = msgbase.getClass().getDeclaredField("roomId");
                    roomId = field.getInt(msgbase);
                    DataManage.INSTANCE.findRoomWorker.get(roomId).workerRoomPendingMsg.add(msgbase);
                }
            } catch (Exception e) {
                LogUntil.logger.warn("Discard the message :" + e.toString());
            }

        }
    }
}
