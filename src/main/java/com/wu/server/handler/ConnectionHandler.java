package com.wu.server.handler;

import com.wu.server.handler.base.ConnectionChannelHandlerAdapter;
import com.wu.server.service.ConnectionService;
import com.wu.server.service.PlayerService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Date;

public class ConnectionHandler extends ConnectionChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+"ConnectionHandler-连接成功:"+ctx);
       ConnectionService.AddClientState(ctx,null);
        ctx.fireChannelActive();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println(new Date()+"ConnectionHandler-断开连接:"+ctx);
        //移除过期账户
        if(ConnectionService.GetPlayer(ctx) != null ){
            PlayerService.RemovePlayer(ConnectionService.GetPlayer(ctx).getId());
        }
        ConnectionService.RemoveClientState(ctx);
        ctx.close(promise);
    }
}
