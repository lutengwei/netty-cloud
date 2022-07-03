package com.lutw.netty.websocket.handler;

import com.lutw.common.core.cocurrent.CallbackTask;
import com.lutw.common.core.cocurrent.CallbackTaskScheduler;
import com.lutw.netty.tcp.handler.TCChannelManage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * webSocket接收数据处理通道
 * @author lutw
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketOnlineHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 接收 webSocket 信息
     * @param ctx
     * @param webSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {

        if (null == webSocketFrame) {
            super.channelRead(ctx, webSocketFrame);
            return;
        }

        if (webSocketFrame instanceof TextWebSocketFrame) {
            String requestMsg = ((TextWebSocketFrame) webSocketFrame).text();
            //判断requestMsg是否是联机请求

            CallbackTaskScheduler.add(new CallbackTask<Boolean>() {
                @Override
                public Boolean execute() throws Exception {
                    try {

                        InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                        String ipURL = inetSocketAddress.getAddress().getHostAddress();
                        int port = inetSocketAddress.getPort();
                        TCChannelManage.webSocketChannelMap.put(String.valueOf(port),ctx.channel());
                        log.info("收到WebSocket请求,IP地址: " + ipURL);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }

                //异步任务返回
                @Override
                public void onBack(Boolean r) {
                    if (r) {
                        log.info("联机成功");
                        // 添加心跳机制等
                        //ctx.pipeline().addAfter("online", "heartBeat", new HeartBeatPacketHandle());
                        ctx.pipeline().remove("online");
                    } else {
                        log.info("登录失败:");
                    }

                }
                //异步任务异常
                @Override
                public void onException(Throwable t) {
                    t.printStackTrace();
                    log.info("登录失败:");
                }
            });
        }
    }

}
