package com.lutw.netty.tcp.handler;


import com.lutw.common.core.bean.HexadecimalEnum;
import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.cocurrent.FutureTaskScheduler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Description OnlinePacketHandle
 * @Author Lutw
 * @Date 2021/11/13 11:20
 * @Version 1.0
 */
@Slf4j
@Component
public class TcpHeartBeatPacketHandle extends IdleStateHandler {
    private static final int READ_IDLE_GAP = 15;

    public TcpHeartBeatPacketHandle() {
        super(READ_IDLE_GAP, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg) {
            super.channelRead(ctx, msg);
            return;
        }
        TargetDeviceProtocol protocol = (TargetDeviceProtocol) msg;
        byte protocolData = protocol.getProtocolData();
        if (HexadecimalEnum.FOUR.getCode() != protocolData) { // 判断是否是心跳数据包（0x01）
            super.channelRead(ctx, msg);
            return;
        }
        //异步处理转发的逻辑
        FutureTaskScheduler.add(() -> ctx.writeAndFlush(msg));
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info(READ_IDLE_GAP + "秒内未读到数据，关闭连接", ctx.channel().close());
    }
}
