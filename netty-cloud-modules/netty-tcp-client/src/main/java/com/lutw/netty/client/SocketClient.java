package com.lutw.netty.client;


import com.lutw.netty.decode.TargetDeviceDecoder;
import com.lutw.netty.decode.TargetDeviceEncoder;
import com.lutw.netty.session.ClientSession;
import com.lutw.netty.tcp.TcpOnlinePacketHandle;
import com.lutw.netty.tcp.TcpServerExceptionHandler;
import com.lutw.netty.tcp.TcpServerHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;


/**
 * websocket启动配置
 */
@Slf4j
@Component
@Data
public class SocketClient implements CommandLineRunner {

    private int port = 8080;

    private String ip = "0.0.0.0";

    private Channel channel;

    private Bootstrap b;

    private NioEventLoopGroup workGroup;

    private boolean connectFlag = false;

    //会话类
    private ClientSession session;

    @Override
    public void run(String... args) {
        CompletableFuture.runAsync(this::startConnect);
    }

    /**
     * 启动客户端连接
     */
    public void startConnect() {
        if (null == workGroup) {
            workGroup = new NioEventLoopGroup(1);
        }
        b = new Bootstrap();
        //设置reactor 线程  channel类型NIO
        b.group(workGroup).channel(NioSocketChannel.class);
        // 对象池，重用缓冲区
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 实现监控连接是否有效，连接处于空闲状态，超过2小时，发送数据包
        b.option(ChannelOption.SO_KEEPALIVE, true);
        // 设置读缓冲区为buf的大小
        b.option(ChannelOption.SO_SNDBUF, 1024);
        // 设置写缓冲区为buf的大小
        b.option(ChannelOption.SO_RCVBUF, 1024);
        // 快速复用端口
        b.option(ChannelOption.SO_REUSEADDR, true);
        // 连接超时
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        b.remoteAddress(ip, port);
        //装配流水线（流程）
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("decoder",new TargetDeviceDecoder());
                ch.pipeline().addLast("encoder",new TargetDeviceEncoder());
                ch.pipeline().addLast("online",new TcpOnlinePacketHandle());
                ch.pipeline().addLast("exception",new TcpServerExceptionHandler());
                ch.pipeline().addLast("tcpserver",new TcpServerHandler());
            }
        });
        try {
            ChannelFuture channelFuture = b.connect();
            channelFuture.addListener(connectedListener);
        } catch (Exception e) {
            log.error("连接服务" + ip + ":" + port + "失败," + e.getMessage(), e);
        }
    }

    GenericFutureListener<ChannelFuture> closeListener = (ChannelFuture f) ->{

        log.info(new Date() + ": 连接已经断开……");
        channel = f.channel();
        // 创建会话
        ClientSession session =
                channel.attr(ClientSession.SESSION_KEY).get();
        session.close();
        //唤醒用户线程
        notifyCommandThread();
    };

    GenericFutureListener<ChannelFuture> connectedListener = new GenericFutureListener<ChannelFuture>() {
        @Override
        public void operationComplete(ChannelFuture f) throws Exception {
            final EventLoop eventLoop
                    = f.channel().eventLoop();
            if (!f.isSuccess()) {
                log.info("连接失败!在10s之后准备尝试重连!");
                eventLoop.schedule(() -> startConnect(), 10,
                        TimeUnit.SECONDS);

                connectFlag = false;
            } else {
                connectFlag = true;
                log.info("TCP服务器 连接成功!");
                channel = f.channel();
                // 创建会话
                session = new ClientSession(channel);
                session.setConnected(true);
                channel.closeFuture().addListener(closeListener);

                //唤醒用户线程
                notifyCommandThread();
            }

        }
    };

    public void close() {
        workGroup.shutdownGracefully();
    }

    public synchronized void notifyCommandThread() {
        //唤醒，命令收集程
        this.notify();

    }

    public synchronized void waitCommandThread() {

        //休眠，命令收集线程
        try {
            this.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
