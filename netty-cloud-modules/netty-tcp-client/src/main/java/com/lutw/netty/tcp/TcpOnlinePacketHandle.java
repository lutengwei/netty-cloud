package com.lutw.netty.tcp;

import com.lutw.common.core.bean.HexadecimalEnum;
import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.cocurrent.CallbackTask;
import com.lutw.common.core.cocurrent.CallbackTaskScheduler;
import com.lutw.common.core.utils.CheckSumCalculation;
import com.lutw.common.core.utils.FormatTransfer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description OnlinePacketHandle
 * @Author Lutw
 * @Date 2021/11/13 11:20
 * @Version 1.0
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class TcpOnlinePacketHandle extends ChannelInboundHandlerAdapter {

    /**
     * 设备首次连接
     * @author: ltw
     * @date: 2022/6/17 14:56
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        byte pr = 1;
        short le = 6;
        char sn = (char)0;
        short id = 1;
        byte ch = 1;
        byte[] deviceId = FormatTransfer.short2ByteNew(id);
        TargetDeviceProtocol protocol = new TargetDeviceProtocol(pr,le,sn,deviceId);
        protocol.setCheckNum(ch);
        //ctx.channel().attr(AttributeKey.newInstance());
        ctx.writeAndFlush(protocol);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null == msg) {
            super.channelRead(ctx, msg);
            return;
        }
        // 67 67 01 00 06 00 00 00 01 01 ee
        // 67 67 06 00 06 00 00 00 01 01 ee 67 67 04 00 05 00 1a 01 1b ee
        // 67 67 04 00 05 00 1a 01 1b ee
        TargetDeviceProtocol protocol = (TargetDeviceProtocol) msg;
        byte protocolData = protocol.getProtocolData();
        if (HexadecimalEnum.ONE.getCode() != protocolData) { // 判断是否是联机数据包（0x01）
            super.channelRead(ctx, msg);
            return;
        }
        CallbackTaskScheduler.add(new CallbackTask<Boolean>() {
            @Override
            public Boolean execute() throws Exception {
                try {
                    // 信息内容
                    ctx.writeAndFlush(CheckSumCalculation.getIsSuccess(protocol, true));
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
                    log.info("登录成功");
                    ctx.pipeline().addAfter("online", "heartBeat", new TcpHeartBeatPacketHandle());
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
