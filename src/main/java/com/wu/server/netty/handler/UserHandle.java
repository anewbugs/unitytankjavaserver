package com.wu.server.netty.handler;

import com.wu.server.bean.PlayerData;
import com.wu.server.bean.User;
import com.wu.server.netty.NetManage;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.proto.base.MsgName;
import com.wu.server.proto.net.MsgGetAchieve;
import com.wu.server.status.DataManage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 玩家数据获取处理类
 */
public class UserHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MsgBase msgBase = (MsgBase) msg;
        if(msgBase.protoName.equals(MsgName.User.MSG_GET_ACHIEVE)){
            MsgGetAchieve msgGetAchieve = (MsgGetAchieve) msgBase;
            User user = DataManage.INSTANCE.onLineUser.get(msgGetAchieve.id);
            if (user == null) return;
            PlayerData userData = user.playerData;
            msgGetAchieve.win = userData.getWin();
            msgGetAchieve.lost = userData.getLost();
            NetManage.send(ctx,msgGetAchieve);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
}
