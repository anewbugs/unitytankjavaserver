package com.wu.server.netty.handler;

import com.wu.server.Until.LogUntil;
import com.wu.server.bean.PlayerData;
import com.wu.server.bean.User;
import com.wu.server.proto.system.MsgOffline;
import com.wu.server.room.manage.boss.RoomBoss;
import com.wu.server.status.DataManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 连接关闭处理
     * @param ctx
     * @throws Exception
     */
    public void close(ChannelHandlerContext ctx){

        /**
         * 职能：
         * 1.玩家不在房间中直接移除登入信息
         * 2.在房间但未开始游戏中伪装离开房间消息发给该工作线程，并移除玩家所有记录
         * 3.房间开始游戏，设置isuser为false，删除连接消息
         * 4.游戏结束让线程保存数据
         * */
        String userId = DataManage.INSTANCE.connection.get(ctx.channel());
        User user = DataManage.INSTANCE.onLineUser.get(userId);
        //Channel通道关闭，清除user对象的Channel

        if(user != null){
            if(user.roomId >= 0){
                    //离线消息
                    MsgOffline msgOffline = new MsgOffline();
                    msgOffline.id = user.getId();
                    msgOffline.roomId = user.roomId;
                    //伪装离线消息发给工作线程  TODO 由工作线程去标记离线玩家
                    user.setChannel(null);
                    DataManage.INSTANCE.findRoomWorker.get(user.roomId).workerRoomPendingMsg.add(msgOffline);

            }else{
                //移除登入消息
                DataManage.INSTANCE.onLineUser.remove(user.getId());
            }
        }
        DataManage.INSTANCE.connection.remove(ctx);
        ctx.close();

    }

    /**
     * 连接异常捕获处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //捕获超时异常
        LogUntil.logger.warn("ConnectionHandler 连接超时:"+cause);
        close(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //检测用户断线
        LogUntil.logger.warn("ConnectionHandler 客户端程序关闭:"+ctx);
        close(ctx);
    }
}
