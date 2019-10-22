package com.wu.server.netty;

import com.wu.server.Until.LogUntil;
import com.wu.server.netty.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.util.concurrent.TimeUnit;

public class NetServer implements Runnable {

    //配置
    /**************************************************/
    //线程检查关闭时间
    static int NETTY_SHUTDOWN_CHECKED_PERIOD = 100;
    //服务器端口号
    static final int NETTY_SERVER_PORT = 8000;
    //解码配置
    static final int MAX_FRAME_LENGTH = 1024;
    static final int LENGTH_FIELD_OFFSET = 0;
    static final int LENGTH_FIELD_LENGTH = 2;
    static final int LENGTH_ADJUSTMENT = 0;
    static final int INITIAL_BYTES_TO_STRIP = 0;
    //超时配置
    static final int CONNECTION_TIMEOUT_SECOND = 10;
    /**************************************************/

    public NetServer() {
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
                        nioSocketChannel.pipeline().addLast("ConnectionHandler",new ConnectionHandler());
                        //消息长度处里Handler 解决半包和粘包问题
                        nioSocketChannel.pipeline().addLast("LengthFieldBasedFrameDecoder", new LengthFieldBasedFrameDecoder(MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH,LENGTH_ADJUSTMENT,INITIAL_BYTES_TO_STRIP));
                        //数据封包处理Handler
                        nioSocketChannel.pipeline().addLast("LengthFieldPrepender",new LengthFieldPrepender(LENGTH_FIELD_LENGTH));
                        //超时控制handler
            //todo            nioSocketChannel.pipeline().addLast("ReadTimeoutHandler" , new ReadTimeoutHandler(CONNECTION_TIMEOUT_SECOND));
                        //服务器断连
                        nioSocketChannel.pipeline().addLast("ServerHandler" ,new ServerHandler());
                        //Ping消息回应
                        nioSocketChannel.pipeline().addLast("PingHandle", new PingHandle());
                        //登入消息处理
                        nioSocketChannel.pipeline().addLast("LoginHandle" , new LoginHandle());
                        //玩家数据获取
                        nioSocketChannel.pipeline().addLast("UserHandle",new UserHandle());
                       //房间消息管理
                        nioSocketChannel.pipeline().addLast("RoomHandle" , new RoomHandle());


                    }
                });
        bind(serverBootstrap,NETTY_SERVER_PORT);


        while(!Thread.interrupted()){
            try {
                TimeUnit.MILLISECONDS.sleep(NETTY_SHUTDOWN_CHECKED_PERIOD);
            } catch (InterruptedException e) {
               LogUntil.logger.error(e.toString());
               Thread.currentThread().interrupt();
            }
        }
        LogUntil.logger.info("netty关闭");
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
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
