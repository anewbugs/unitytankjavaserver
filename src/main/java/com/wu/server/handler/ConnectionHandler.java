package com.wu.server.handler;

import com.wu.server.handler.base.ConnectionChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Date;

public class ConnectionHandler extends ConnectionChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+"ConnectionHandler-连接成功:"+ctx);
        ctx.fireChannelActive();
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println(new Date()+"ConnectionHandler-断开连接:"+ctx);
        ctx.close(promise);
    }
}
