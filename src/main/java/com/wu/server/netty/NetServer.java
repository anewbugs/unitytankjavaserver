package com.wu.server.netty;

import com.wu.server.handler.ConnectionHandler;
import com.wu.server.handler.base.ServerHandle;
import com.wu.server.room.manage.RoomBoss;
import com.wu.server.room.material.MsgLine;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NetServer implements Runnable {
    private RoomBoss roomBoss;

    public NetServer(RoomBoss roomBoss) {
        this.roomBoss = roomBoss;
    }

    @Override
    public void run() {
        int a = 10;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();


        serverBootstrap
                .group(boosGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {


                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        //连接处理Handler
                        nioSocketChannel.pipeline().addLast(new ConnectionHandler());
                        //消息长度处里Handler
                        //解决半包和粘包问题
                        nioSocketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,2,0,0));
                        //数据封包处理Handler
                        nioSocketChannel.pipeline().addLast(new LengthFieldPrepender(2));
                        //超时控制handler
                        nioSocketChannel.pipeline().addLast(new ReadTimeoutHandler(8));
                        //消息服务处理Handler
                        //处理和回应各消息
                        nioSocketChannel.pipeline().addLast(new ServerHandle());


                    }
                });
        bind(serverBootstrap,8000);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {

        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
            }
        });
    }
}
