package com.lutw.netty.tcp.handler;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

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

    @Autowired
    private RedisTemplate redisTemplate;

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
                    String contents = FormatTransfer.bytes2hex02(protocol.getContent());
                    //截取出来 设备id
                    String deviceId = contents.substring(0, 4);
                    long deviceIdNum = FormatTransfer.decodeHEX(deviceId);
                    String s = CheckSumCalculation.deviceIdZeroPadding(deviceIdNum);
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                    String ipURL = inetSocketAddress.getAddress().getHostAddress();
                    log.info("IP地址：" + ipURL);
                    //接收数据后，回应信息
                    ctx.writeAndFlush(CheckSumCalculation.getIsSuccess(protocol, true));
                    System.out.println("设备ID:" + deviceIdNum + "上线");
                } catch (Exception e) {
                    log.error("联机失败",e.getMessage());
                    return false;
                }
                return true;
            }

            //异步任务返回
            @Override
            public void onBack(Boolean r) {
                if (r) {
                    log.info("联机成功");
                    System.out.println("联机成功");
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
