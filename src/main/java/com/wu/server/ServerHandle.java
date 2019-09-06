package com.wu.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.AbstractByteBufAllocator;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.json.JsonObjectDecoder;

public class ServerHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        System.out.println((String)msg);
        ByteBuf requestByteBuf = (ByteBuf) msg;

        byte[] a =new byte[1] ;
        byte[] b = new byte[1];

        requestByteBuf.readBytes(a);
        System.out.println(requestByteBuf.toString());
        requestByteBuf.readBytes(b);
        System.out.println(requestByteBuf.toString());
       int l = (int)a[0];
        int l2 = (int)b[0];
        System.out.println(l+"  "+l2);
        byte[] msgbytes= new byte[l2];
        System.out.println(requestByteBuf.toString());
        byte[] jsonbytes = new byte[l - l2 - 1];
        requestByteBuf.readBytes(msgbytes);
        requestByteBuf.readBytes(jsonbytes);
        System.out.println(new String(jsonbytes));
        JSONObject js = (JSONObject) JSONObject.parse(jsonbytes);
        System.out.println(js.get("id"));
        js.put("result",0);


        ByteBuf byteBuf = ctx.alloc().ioBuffer();
        byteBuf.writeBytes(b);
        byteBuf.writeBytes(msgbytes);
        byteBuf.writeBytes(js.toJSONString().getBytes());
        ctx.channel().writeAndFlush(byteBuf);

    }
}
