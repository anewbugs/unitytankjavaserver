package com.wu.server.handler;

import com.wu.server.proto.MsgLogin;
import com.wu.server.proto.base.MsgBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf requestByteBuf = (ByteBuf) msg;
        //消息体长度
        byte[] msgLengthbytes =new byte[1] ;
        requestByteBuf.readBytes(msgLengthbytes);
        int msgLength = (int)msgLengthbytes[0];
        //解析协议名
        byte[] protoNameLengthBytes = new byte[1];
        requestByteBuf.readBytes(protoNameLengthBytes);
        int protoNameLength = (int)protoNameLengthBytes[0];
        byte[] protoNameBytes= new byte[protoNameLength];
        requestByteBuf.readBytes(protoNameBytes);
        String protoName = new String(protoNameBytes);
        //解析协议体
        byte[] jsonBytes = new byte[msgLength - protoNameLength - 1];
        requestByteBuf.readBytes(jsonBytes);
        MsgBase msgBase = MsgBase.Decode(protoName,jsonBytes);
        //分发消息
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.wu.server.handler.MsgHandler");
            Method method =clazz.getMethod("MyMethod",ChannelHandlerContext.class,MsgBase.class);
            method.invoke(null, ctx,msgBase);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


//
//        byte[] jsonbytes = new byte[l - l2 - 1];
//
//        requestByteBuf.readBytes(jsonbytes);
//        System.out.println(new String(jsonbytes));
//        JSONObject js = (JSONObject) JSONObject.parse(jsonbytes);
//
//        //MsgBase jb = (MsgBaseLogin)JSONObject.toJavaObject(js,MsgBaseLogin.class );
//        MsgBase jb  = MsgBase.Decode(new String(msgName),jsonbytes);
//        //System.out.println("id="+jb.id+ ",mname= "+jb.protoName+",paseword =" +jb.pw+",result="+ jb.result);
//        JSONObject json = (JSONObject) JSONObject.toJSON(jb);
//        System.out.println(json.toJSONString());
//        System.out.println("-----------------------------------------------");
//        //System.out.println(js.get("id"));
//        js.put("result",0);
//
//
//        ByteBuf byteBuf = ctx.alloc().ioBuffer();
//        byteBuf.writeBytes(b);
//        byteBuf.writeBytes(msgName);
//        byteBuf.writeBytes(js.toJSONString().getBytes());
//        ctx.channel().writeAndFlush(byteBuf);


    }
}
