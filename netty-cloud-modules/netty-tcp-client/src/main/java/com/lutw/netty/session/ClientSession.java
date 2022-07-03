package com.lutw.netty.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.Data;

import java.util.UUID;

/**
 * @Description:
 * @Author: ltw
 * @Date: 2022/6/17 16:03
 */
@Data
public class ClientSession {

    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("SESSION_KEY");

    /**
     * 用户实现客户端会话管理的核心
     */
    private Channel channel;
    private String user;
    /**
     * 保存登录后的服务端sessionid
     */
    private String sessionId;

    private boolean isConnected = false;
    private boolean isLogin = false;

    //绑定通道
    //连接成功之后
    public ClientSession(Channel channel) {
        //正向的绑定
        this.channel = channel;
        this.sessionId = UUID.randomUUID().toString();
        //反向的绑定
        channel.attr(ClientSession.SESSION_KEY).set(this);
    }
    //登录成功之后,设置sessionId
    public static void loginSuccess(ChannelHandlerContext ctx, String sessionId) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        session.setSessionId(sessionId);
        session.setLogin(true);
    }

    //获取channel
    public static ClientSession getSession(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        ClientSession session = channel.attr(ClientSession.SESSION_KEY).get();
        return session;
    }

    public String getRemoteAddress() {
        return channel.remoteAddress().toString();
    }

    //写protobuf 数据帧
    public ChannelFuture writeAndFlush(Object pkg) {
        ChannelFuture f = channel.writeAndFlush(pkg);
        return f;
    }

    public void writeAndClose(Object pkg) {
        ChannelFuture future = channel.writeAndFlush(pkg);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    //关闭通道
    public void close() {
        isConnected = false;

        ChannelFuture future = channel.close();
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("连接顺利断开");
                }
            }
        });
    }

}
