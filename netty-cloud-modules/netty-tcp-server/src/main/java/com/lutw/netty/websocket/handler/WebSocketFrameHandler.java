package com.lutw.netty.websocket.handler;

import com.lutw.netty.tcp.handler.TCChannelManage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * webSocket接收数据处理通道
 * @author lutw
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 接收 webSocket 信息
     * @param ctx
     * @param webSocketFrame
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) throws Exception {

        try {
            if (webSocketFrame instanceof TextWebSocketFrame) {
                String requestMsg = ((TextWebSocketFrame) webSocketFrame).text();
                // 返回应答消息
                System.out.println(requestMsg);
            }
        } catch (Exception e) {
            log.error("下发指令解析错误",e.getMessage(),e);
        }
    }

    /**
     * 每当从服务端收到客户端断开时，客户端的 Channel 移除 ChannelMap 列表
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        try {
            ctx.close();
            // TODO 终端断开,终端下线
            String deviceId = TCChannelManage.removeChannel(ctx.channel(),TCChannelManage.webSocketChannelMap);
            log.error("webSocket断开:" + deviceId + ctx );
        } catch (Exception e) {
            log.error("webSocket断开",e.getMessage(),e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }







}
