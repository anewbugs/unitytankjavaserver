package com.wu.server.proto;

import com.alibaba.fastjson.JSONObject;
import  com.wu.server.proto.login.MsgBaseLogin;
import java.util.HashMap;

/**
 * 消息类基类
 * @author wu
 * @version 1.0
 */
public class MsgBase {
    public static HashMap<String,Class> h = new HashMap<>( );

    static {
        h.put("MsgLogin",MsgBaseLogin.class);
    }
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

    /**
     * 解码
     * @param protoName
     * @param bytes
     * @return
     */
//    public static MsgBase Decode(String protoName, byte[] bytes){
//        HashMap<String,Class> msgBaseMatch = new HashMap<String, Class>();
//        static{
//
//
//        }
//
//        return null;
//    }
}
