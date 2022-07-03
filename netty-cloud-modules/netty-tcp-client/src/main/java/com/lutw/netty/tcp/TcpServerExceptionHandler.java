package com.lutw.netty.tcp;

import com.lutw.netty.client.SocketClient;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@ChannelHandler.Sharable
@Service("ServerExceptionHandler")
public class TcpServerExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //捕捉异常信息
        cause.printStackTrace();
        log.error(cause.getMessage());
        ctx.close();
    }

    /**
     * 通道 Read 读取 Complete 完成
     * 做刷新操作 ctx.flush()
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception {
        log.error("没有获取到除心跳以外的数据，关闭连接!");
        log.info("重连");
        SocketClient socketClient = new SocketClient();
        socketClient.startConnect();
    }


}