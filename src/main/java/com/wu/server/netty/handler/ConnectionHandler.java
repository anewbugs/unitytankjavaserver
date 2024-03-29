package com.wu.server.netty.handler;

import com.wu.server.Until.LogUntil;
import com.wu.server.status.DataManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 连接拦截器
 * @Authot wu
 * @version 1.0
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter {
    public Node node;
    /**
     * 新建连接处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LogUntil.logger.info(" ConnectionHandler 连接成功:"+ctx);
        DataManage.INSTANCE.connection.put(ctx.channel(),"");
        ctx.fireChannelActive();
        node = new Node();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println(node.str);
        ctx.fireChannelRead(msg);
    }
    static class Node{
       public String str;
       static int a = 0;
       public Node(){
           str = "node" + a++;
       }
    }
}
