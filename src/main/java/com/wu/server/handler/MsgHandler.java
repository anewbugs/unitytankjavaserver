package com.wu.server.handler;

import com.wu.server.bean.*;
import com.wu.server.dao.*;
import com.wu.server.proto.*;
import com.wu.server.proto.base.MsgBase;
import com.wu.server.service.*;
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
       ConnectionService.clientState.put(ctx,player);
        msg.result = 0;
        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
    }

    /**
     * 注册协议处理
     * @param ctx
     * @param msgBase
     */
    public static void MsgRegister(ChannelHandlerContext ctx,MsgBase msgBase){
        MsgRegister msg = (MsgRegister)msgBase;
        //注册
        if(UserDao.Register(msg.id, msg.pw)){
            PlayerDataDao.CreatePlayer(msg.id);
            msg.result = 0;
        }
        else{
            msg.result = 1;
        }
        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
    }


/***************************************************房间管理*************************************************************/
/***********************************************************************************************************************/

    /**
     * 战绩获取
     * @param ctx
     * @param msgBase
     */
    public static void MsgGetAchieve(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgGetAchieve msg = (MsgGetAchieve)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;

        msg.win = player.getData().getWin();
        msg.lost = player.getData().getLost();

        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(),msg));
    }

    public static void MsgGetRoomList(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgGetRoomList msg = (MsgGetRoomList)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(), RoomService.ToMsg()));
        //player.Send(RoomManager.ToMsg());
    }
    /**
     *  创建房间
     * @param ctx
     * @param msgBase
     */
    public static void MsgCreateRoom(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgCreateRoom msg = (MsgCreateRoom)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        //已经在房间里
        if(player.getRoomId() >=0 ){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //创建
        Room room = RoomService.AddRoom();
        room.AddPlayer(player.getId());

        msg.result = 0;
        player.send(msg);
    }

    /**
     * 进入房间
     * @param ctx
     * @param msgBase
     */
    public static void MsgEnterRoom(ChannelHandlerContext ctx,  MsgBase msgBase){
        MsgEnterRoom msg = (MsgEnterRoom)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        //已经在房间里
        if(player.getRoomId() >=0 ){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //获取房间
        Room room = RoomService.GetRoom(msg.id);
        if(room == null){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //进入
        if(!room.AddPlayer(player.getId())){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //返回协议
        msg.result = 0;
        player.send(msg);
    }
    /**
     *  获取房间信息
     * @param ctx
     * @param msgBase
     */
    public static void MsgGetRoomInfo(ChannelHandlerContext ctx,   MsgBase msgBase){
        MsgGetRoomInfo msg = (MsgGetRoomInfo)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;

        Room room = RoomService.GetRoom(player.getRoomId());
        if(room == null){
            player.send(msg);
            return;
        }

        player.send(room.ToMsg());
    }

    /**
     *  离开房间
     * @param ctx
     * @param msgBase
     */
    public static void MsgLeaveRoom(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgLeaveRoom msg = (MsgLeaveRoom)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;

        Room room = RoomService.GetRoom(player.getRoomId());
        if(room == null){
            msg.result = 1;
            player.send(msg);
            return;
        }

        room.RemovePlayer(player.getId());
        //返回协议
        msg.result = 0;
        player.send(msg);
    }

    /**
     * 请求开始战斗
     * @param ctx
     * @param msgBase
     */
    public static void MsgStartBattle(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgStartBattle msg = (MsgStartBattle)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        //room
        Room room = RoomService.GetRoom(player.getRoomId());
        if(room == null){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //是否是房主
        if(!room.isOwner(player)){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //开战
        if(!room.StartBattle()){
            msg.result = 1;
            player.send(msg);
            return;
        }
        //成功
        msg.result = 0;
        player.send(msg);
    }
/***************************************************ping管理*************************************************************/
/***********************************************************************************************************************/
    /**
     * ping消息处理
     * @param ctx
     * @param msgBase
     */
    public static void MsgPing(ChannelHandlerContext ctx, MsgBase msgBase){
   // c.lastPingTime = NetManager.GetTimeStamp();
    MsgPong msgPong = new MsgPong();
    ctx.channel().writeAndFlush(MsgBase.Encode(ctx.alloc().ioBuffer(), msgPong));
}
/***************************************************战斗管理*************************************************************/
/***********************************************************************************************************************/

//同步位置协议
public static void MsgSyncTank(ChannelHandlerContext ctx, MsgBase msgBase){
    MsgSyncTank msg = (MsgSyncTank)msgBase;
    Player player =ConnectionService.GetPlayer(ctx);
    if(player == null) return;
    //room
    Room room = RoomService.GetRoom(player.getRoomId());
    if(room == null){
        return;
    }
    //status
    if(room.status != Status.FIGHT){
        return;
    }
//    //是否作弊
//    if(Math.Abs(player.x - msg.x) > 5 ||
//            Math.Abs(player.y - msg.y) > 5 ||
//            Math.Abs(player.z - msg.z) > 5){
//        System.out.println("疑似作弊 " + player.id);
//    }
    //更新信息
    player.setX(msg.x);
    player.setY(msg.y);
    player.setZ(msg.z);
    player.setEx(msg.ex);
    player.setEy(msg.ey);
    player.setEz(msg.ez);

//    player.x = msg.x;
//    player.y = msg.y;
//    player.z = msg.z;
//    player.ex = msg.ex;
//    player.ey = msg.ey;
//    player.ez = msg.ez;
    //广播
    msg.id = player.getId();
    room.Broadcast(msg);
}

    //开火协议
    public static void MsgFire(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgFire msg = (MsgFire)msgBase;
        Player player =ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        //room
        Room room = RoomService.GetRoom(player.getRoomId());
        if(room == null){
            return;
        }
        //status
        if(room.status != Status.FIGHT){
            return;
        }
        //广播
        msg.id = player.getId();
        room.Broadcast(msg);
    }

    //击中协议
    public static void MsgHit(ChannelHandlerContext ctx, MsgBase msgBase){
        MsgHit msg = (MsgHit)msgBase;
        Player player = ConnectionService.GetPlayer(ctx);
        if(player == null) return;
        //targetPlayer
        Player targetPlayer = PlayerService.GetPlayer(msg.targetId);
        if(targetPlayer == null){
            return;
        }
        //room
        Room room = RoomService.GetRoom(player.getRoomId());
        if(room == null){
            return;
        }
        //status
        if(room.status != Status.FIGHT){
            return;
        }
        //发送者校验
        if(player.getId() != msg.id){
            return;
        }
        //状态
        int damage = 35;
        targetPlayer.setHp(targetPlayer.getHp() - damage);
        //广播
        msg.id = player.getId();
        msg.hp = player.getHp();
        msg.damage = damage;
        room.Broadcast(msg);
    }
}
