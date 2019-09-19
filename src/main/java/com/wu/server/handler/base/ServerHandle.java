package com.wu.server.handler.base;

import com.wu.server.bean.Player;
import com.wu.server.bean.Room;
import com.wu.server.dao.PlayerDataDao;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.service.ConnectionService;
import com.wu.server.service.PlayerService;
import com.wu.server.service.RoomService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

public class ServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println(ctx);
//
//        ByteBuf requestByteBuf = (ByteBuf) msg;
//        //消息体长度
//        byte[] msgLengthBytes =new byte[1] ;
//        requestByteBuf.readBytes(msgLengthBytes);
//        int msgLength = (int)msgLengthBytes[0];
//        //解析协议名
//        byte[] protoNameLengthBytes = new byte[1];
//        requestByteBuf.readBytes(protoNameLengthBytes);
//        int protoNameLength = (int)protoNameLengthBytes[0];
//        byte[] protoNameBytes= new byte[protoNameLength];
//        requestByteBuf.readBytes(protoNameBytes);
//        String protoName = new String(protoNameBytes);
//        //解析协议体
//        byte[] jsonBytes = new byte[msgLength - protoNameLength - 1];
//        requestByteBuf.readBytes(jsonBytes);
        MsgBase msgBase = MsgBase.Decode(msg);
        //分发消息
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.wu.server.handler.MsgHandler");
            Method method =clazz.getMethod(msgBase.protoName,ChannelHandlerContext.class,MsgBase.class);
            method.invoke(null, ctx,msgBase);

        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接关闭处理
     * @param ctx
     * @throws Exception
     */
    public void close(ChannelHandlerContext ctx) throws Exception{

        Player player = ConnectionService.GetPlayer(ctx);
        //移除过期账户
        //下线
        if(player != null ){
            if(player.getRoomId() >= 0){
                Room room = RoomService.GetRoom(player.getRoomId());
                room.RemovePlayer(player.getId());
            }
            //保存数据
            PlayerDataDao.UpdatePlayerData(player);
            //移除登录信息
            PlayerService.RemovePlayer(ConnectionService.GetPlayer(ctx).getId());
        }
        //移除连接信息
        ConnectionService.RemoveClientState(ctx);
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
        System.err.println(new Date()+" ConnectionHandler exception:"+cause.toString());
        System.err.println(new Date()+" ConnectionHandler 连接超时:"+ctx);
        close(ctx);
    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println(new Date()+" ConnectionHandler 客户端程序关闭:"+ctx);
        close(ctx);
    }
}
