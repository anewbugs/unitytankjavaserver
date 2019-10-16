package com.wu.server.handler;

import com.wu.server.handler.base.MsgHandler;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author wu
 * 处理登入逻辑函数
 */
public class LoginHandle extends ChannelInboundHandlerAdapter {
    //处理登入逻辑
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        MsgBase msgBase = (MsgBase) msg;
        //如果是登入逻辑在这里处理
        if(msgBase.protoName.equals(MsgName.Login.MSG_LOGIN)){
            MsgHandler.INSTANCE.MsgLogin(ctx,msgBase);
        }
        //其他消息分发给下一个逻辑处理
        else{
            ctx.fireChannelRead(msgBase);
        }
    }
}
