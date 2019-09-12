package com.wu.server.handler;

import com.wu.server.proto.MsgLogin;
import com.wu.server.proto.base.MsgBase;
import io.netty.channel.ChannelHandlerContext;

public class MsgHandler {


/***************************************************登入注册*************************************************************/
/***********************************************************************************************************************/
    /**
     * 登入协议处理
     * @param ctx
     * @param msgBase
     */
    public static void MsgLogin(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgLogin msg = (MsgLogin) msgBase;

    }

}
