package com.wu.server.handler;

import com.wu.server.handler.base.ConnectionChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ConnectionHandler extends ConnectionChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("连接成功");
        System.out.println(ctx);
        ctx.fireChannelActive();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("断开连接");
        ctx.close(promise);
    }
}
