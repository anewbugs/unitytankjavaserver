package com.wu.server;

import com.wu.server.handler.ConnectionHandler;
import com.wu.server.handler.base.ServerHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * @author wu
 * @see ServerMain
 * nettyServer
 */
public class ServerMain {
    //端口

    private static final int PORT = 8000;

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();


        serverBootstrap
                .group(boosGroup,workerGroup)
                .channel(NioServerSocketChannel.class)

                .childHandler(new ChannelInitializer<NioSocketChannel>() {

                    protected void initChannel(NioSocketChannel nioSocketChannel) {
                        //超时控制handler
                        nioSocketChannel.pipeline().addLast(new ReadTimeoutHandler(20));
                        //连接处理Handler
                        nioSocketChannel.pipeline().addLast(new ConnectionHandler());
                        //消息长度处里Handler
                        //解决半包和粘包问题
                        nioSocketChannel.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024,0,1,0,0));
                        //数据封包处理Handler
                        nioSocketChannel.pipeline().addLast(new LengthFieldPrepender(1));
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
