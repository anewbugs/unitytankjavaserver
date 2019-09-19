package com.wu.server.handler;

import com.wu.server.bean.Player;
import com.wu.server.bean.Room;
import com.wu.server.dao.PlayerDataDao;
import com.wu.server.handler.base.ConnectionChannelHandlerAdapter;
import com.wu.server.service.ConnectionService;
import com.wu.server.service.PlayerService;
import com.wu.server.service.RoomService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.util.Date;

/**
 * 连接拦截器
 * @Authot wu
 * @version 1.0
 */
public class ConnectionHandler extends ConnectionChannelHandlerAdapter {
    /**
     * 新建连接处理
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date()+" ConnectionHandler 连接成功:"+ctx);
       ConnectionService.AddClientState(ctx,null);
        ctx.fireChannelActive();
    }

}
