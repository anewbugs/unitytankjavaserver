package com.wu.server.handler;

import com.wu.server.handler.base.ConnectionChannelHandlerAdapter;
import com.wu.server.service.ConnectionService;
import com.wu.server.service.PlayerService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Date;

/**
 * 连接拦截器
 * @Authot wu
 * @version 1.0
 */
public class ConnectionHandler extends ConnectionChannelHandlerAdapter {
    /**
     * 新建连接处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+" ConnectionHandler 连接成功:"+ctx);
       ConnectionService.AddClientState(ctx,null);
        ctx.fireChannelActive();
    }

    /**
     * 连接关闭处理
     * @param ctx
     * @throws Exception
     */
    public void close(ChannelHandlerContext ctx) throws Exception{
            System.out.println(new Date()+" ConnectionHandler 连接超时:"+ctx);
            //移除过期账户
            if(ConnectionService.GetPlayer(ctx) != null ){
                PlayerService.RemovePlayer(ConnectionService.GetPlayer(ctx).getId());
            }
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
        System.out.println(new Date()+" ConnectionHandler exception:");
        cause.printStackTrace();
        close(ctx);
    }
}
