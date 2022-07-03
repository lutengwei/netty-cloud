package com.lutw.netty.server;

import com.lutw.common.core.constants.ConstantValue;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


/**
 * websocket启动配置
 */
@Slf4j
@Component
public class Server implements CommandLineRunner {

    private int port = ConstantValue.NETTY_SERVER_PORT;

    //服务端启动引导类
    private ServerBootstrap b = new ServerBootstrap();

    //Reacter反应器是多线程的反应模式
    private EventLoopGroup bossGroup;  //监听线程，可以是多个，负责接收请求
    private EventLoopGroup workerGroup; //IO传输线程，负责处理I/O请求

    @Override
    public void run(String... args) {
        CompletableFuture.runAsync(this::init);
    }

    public void init() {
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        //设置reactor 线程  channel类型NIO
        b.group(bossGroup, workerGroup);
        // 设置nio类型的channel
        b.channel(NioServerSocketChannel.class);
        // TODO -------------监听线程设置--------------
        // 对象池，重用缓冲区
        b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 快速复用端口
        b.option(ChannelOption.SO_REUSEADDR, true);
        // 连接超时
        b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
        // TODO -------------工作线程设置--------------
        // 对象池，重用缓冲区
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        // 实现监控连接是否有效，连接处于空闲状态，超过2小时，发送数据包
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 设置读缓冲区为buf的大小
        b.childOption(ChannelOption.SO_SNDBUF, 1024);
        // 设置写缓冲区为buf的大小
        b.childOption(ChannelOption.SO_RCVBUF, 1024);

        //装配流水线（流程）
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast("selectDecode", new SelectDecode());
            }
        });

        // 绑定端口，通过调用sync同步方法阻塞直到绑定成功
        ChannelFuture socketFuture = null;
        try {
            socketFuture = b.bind(port).sync();
            if (socketFuture.isSuccess()) {
                System.out.println("Netty 服务已启动");
            }
        } catch (Exception e) {
            log.error("启动异常", e);
        }
        //JVM关闭时的钩子函数
        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    // 优雅关闭EventLoopGroup,释放掉所有资源包括创建的线程
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                })
        );
        try {
            // 监听通道关闭事件 应用程序会一直等待，直到channel关闭
            ChannelFuture closeFuture =
                    socketFuture.channel().closeFuture();
            closeFuture.sync();
        } catch (Exception e) {
            log.error("其他异常", e);
        } finally {
            // 优雅关闭EventLoopGroup,释放掉所有资源包括创建的线程
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
