package com.wu.server.handler;

import com.wu.server.bean.Player;
import com.wu.server.bean.PlayerData;
import com.wu.server.dao.PlayerDataDao;
import com.wu.server.dao.UserDao;
import com.wu.server.proto.MsgGetAchieve;
import com.wu.server.proto.MsgKick;
import com.wu.server.proto.MsgLogin;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.service.ConnectionService;
import com.wu.server.service.PlayerService;
import io.netty.channel.ChannelHandlerContext;

/**
 * 消息处理器
 * @author wu
 * @version 1.0
 */
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
        //密码校验
        if (!PlayerService.CheckedPlayer(msg.id,msg.pw)){
            msg.result = 1;
            //发送登入失败协议
            ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
            return;
        }
        //不允许再次登入
        if(PlayerService.IsOnline(msg.id)){
            msg.result=1;
            //发送登入失败协议
            ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
            return ;
        }
        //如果已经登陆，踢下线
        if(PlayerService.IsOnline(msg.id)){
            //发送踢下线协议
            Player other = PlayerService.GetPlayer(msg.id);
            MsgKick msgKick = new MsgKick();
            msgKick.reason = 0;
            other.getChannel().writeAndFlush(MsgBase.Encode(other.getChannel().alloc().ioBuffer(),msgKick));
            //断开连接
            other.getChannel().close();
        }
        //获取玩家数据
        PlayerData playerData = PlayerDataDao.GetPlayerData(msg.id);
        if(playerData == null){
            msg.result = 1;
            ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
            return;
        }
        //构建Player
        Player player = new Player(ctx.channel());
        player.setId(msg.id);
        player.setData(playerData);
        PlayerService.AddPlayer(msg.id,player);
        //c.player = player;
        msg.result = 0;
        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
    }



/***************************************************房间管理*************************************************************/
/***********************************************************************************************************************/
    public static void MsgGetAchieve(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgGetAchieve msg = (MsgGetAchieve)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;

        msg.win = player.getData().getWin();
        msg.lost = player.getData().getLost();

        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
    }


}
