package com.wu.server.proto.base;

import com.alibaba.fastjson.JSONObject;
import com.wu.server.proto.MsgLogin;

import java.util.HashMap;

/**
 * 消息类基类
 * @author wu
 * @version 1.0
 */
public class MsgBase {
    public String protoName = null;

    /**编码
     * 将Object对象转化为JSON对象字节数组数组
     * @param msgBase
     * @return
     */
    public static byte[] Encode(MsgBase msgBase){
        //MsgBase对象转化为JSONObject对象
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(msgBase);
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
    public static  MsgBase Decode(String protoName, byte[] bytes){
        //字节数组转化为JSON对象
        try {
            Class<?> clazz = Class.forName("com.wu.server.proto."+protoName);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(bytes);
            System.out.println(jsonObject);
            //将JSON对象转化为MsgaBase对象

            MsgBase msgBase = (MsgLogin)JSONObject.toJavaObject(jsonObject, clazz);
            return msgBase;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}