package com.wu.server.proto.base;

import com.alibaba.fastjson.JSONObject;
import com.wu.server.proto.MsgLogin;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.Date;
import java.util.HashMap;

/**
 * 消息类基类
 * @author wu
 * @version 1.0
 */
public class MsgBase {
    public String protoName = null;



    public static ByteBuf Encode(ByteBuf byteBuf,MsgBase msgBase){
        byte[] protoNameLengthBytes = new byte[1];
        protoNameLengthBytes[0] = (byte)msgBase.protoName.length();
        byteBuf.writeBytes(protoNameLengthBytes);
        byteBuf.writeBytes(msgBase.protoName.getBytes());
        byteBuf.writeBytes(MsgBase.EncodeMsg(msgBase));
        return byteBuf;
    }

    /**
     * 解析ByteBuf
     * @param msg
     * @return MsgBase对象
     */
    public static MsgBase Decode(Object msg){
        ByteBuf requestByteBuf = (ByteBuf)msg;
        byte[] msgLengthBytes =new byte[1] ;
        requestByteBuf.readBytes(msgLengthBytes);
        int msgLength = (int)msgLengthBytes[0];
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
        MsgBase msgBase = MsgBase.DecodeMsg(protoName,jsonBytes);
        return msgBase;
    }

    /**编码
     * 将Object对象转化为JSON对象字节数组数组
     * @param msgBase
     * @return
     */
    public static byte[] EncodeMsg(MsgBase msgBase){
        //MsgBase对象转化为JSONObject对象
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msgBase);
        System.out.println(new Date() +" MsgBase Response: "+ jsonObject);
        //将JSONObject对象转化为字符串对象
        String jsonStr = jsonObject.toJSONString();
        //将JSON字符串转化为字节数组
        byte[] jsonByte = jsonStr.getBytes();
        return jsonByte;
    }

    /**解码
     * 将JSON字节数组转换成对应的javaBean对象
     * @param protoName
     * @param bytes
     * @return msgBase
     */
    public static  MsgBase DecodeMsg(String protoName, byte[] bytes){
        //字节数组转化为JSON对象
        try {
            Class<?> clazz = Class.forName("com.wu.server.proto."+protoName);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(bytes);
            System.out.println(new Date() +" MsgBase Receive: "+ jsonObject);
            //将JSON对象转化为MsgaBase对象

            MsgBase msgBase = (MsgBase) JSONObject.toJavaObject(jsonObject, clazz);
            return msgBase;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
