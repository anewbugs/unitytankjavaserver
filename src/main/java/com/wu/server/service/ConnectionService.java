package com.wu.server.service;

import com.wu.server.bean.Player;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
//准备抛弃
public class ConnectionService {
    public static HashMap<ChannelHandlerContext, Player> clientState = new HashMap<>();
    public static Player GetPlayer(ChannelHandlerContext ctx) {
        if(clientState.containsKey(ctx)){
            return clientState.get(ctx);
        }
        return null;
    }

    /**
     * 添加玩家
     * @param
     * @param player
     */
    public static void  AddClientState(ChannelHandlerContext ctx,Player player){
        clientState.put(ctx,player);

    }

    /**
     * 删除玩家
     * @param ctx
     */
    public static void RemoveClientState(ChannelHandlerContext ctx){
        clientState.remove(ctx);
    }
}
