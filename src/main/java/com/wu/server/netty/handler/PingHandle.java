package com.wu.server.netty.handler;

import com.wu.server.netty.handler.base.MsgHandler;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Field;


/**
 * @author wu
 * @version 2.0
 */
public class PingHandle extends ChannelInboundHandlerAdapter {
    /**
     * 只处理登入逻辑
     * 其他逻辑分发给线程处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //消息解码
        MsgBase msgBase = MsgBase.Decode(msg);
        //msg消息是MSG_PING 回复客户端
        if (msgBase.protoName.equals(MsgName.SysMsg.MSG_PING)) {
            MsgHandler.INSTANCE.MsgPing(ctx, msgBase);
        } else {
            //非MsgPing消息下发
            ctx.fireChannelRead(msgBase);
        }
    }


}
