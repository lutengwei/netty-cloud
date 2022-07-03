package com.lutw.netty.tcp;


import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.cocurrent.FutureTaskScheduler;
import com.lutw.common.core.constants.ConstantValue;
import com.lutw.common.core.utils.StringUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author lutw
 * 该类继承了ChannelInboundHandlerAdapter
 * ChannelInboundHandlerAdapter用于处理入站I / O事件
 */
@Slf4j
@ChannelHandler.Sharable
@Component
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 通过socket接收终端设备数据
     *
     * @param ctx 保存Channel相关的所有上下文信息，同时关联一个ChannelHandler对象
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        TargetDeviceProtocol protocol = (TargetDeviceProtocol) msg;
        byte protocolData = protocol.getProtocolData();
        //异步处理转发的逻辑
        FutureTaskScheduler.add(() -> {
                System.out.println(protocol);
        });
    }


    /**
     * 关闭通道提示
     * @param ctx
     */
    private void  closeDevicePoint(ChannelHandlerContext ctx) {
        try {
            ctx.channel().close();
            // TODO 终端断开,终端下线
            String deviceId = TCChannelManage.removeChannel(ctx.channel(),TCChannelManage.formationChannelMap);
            if (StringUtils.isEmpty(deviceId)){
                InetSocketAddress inetSocketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
                String ipURL = inetSocketAddress.getAddress().getHostAddress();
                deviceId = TCChannelManage.removeChannel(ctx.channel(),TCChannelManage.deviceChannelMap);
                if (StringUtils.isEmpty(deviceId)){
                    return;
                }
                TCChannelManage.responseMessageToPage(deviceId+"终端断开");
                TCChannelManage.responseMessageToPage("F" + deviceId);
            }else {
                TCChannelManage.responseMessageToPage("编队服务器异常，断开连接");
            }
            removeDeviceIdAndIpUrl(deviceId);
            log.error("终端断开:" + deviceId + ctx);
        } catch (Exception e) {
            log.error("终端断开移除错误！",e.getMessage(),e);
        }
    }


    /**
     * 保存设备Ip
     * @param deviceId 设备id
     * @param videoIpUrl ip地址
     */
    public void addDeviceIdAndIpUrl(String deviceId,String videoIpUrl){
        try {
            redisTemplate.opsForHash().put(ConstantValue.DEVICE_IP_URL_LIST,deviceId, videoIpUrl);
        } catch (Exception e) {
            log.error("设备:"+deviceId+" IP"+videoIpUrl+"信息保存失败！",e.getMessage(),e);
        }
    }

    /**
     * 删除deviceIP
     * @param deviceId 设备id
     */
    private void removeDeviceIdAndIpUrl(String deviceId) {
        try {
            redisTemplate.opsForHash().delete(ConstantValue.DEVICE_IP_URL_LIST,deviceId);
        } catch (NumberFormatException e) {
            log.error("数据转换异常",e.getMessage(),e);
        } finally {
            //释放redis连接
            RedisConnectionUtils.unbindConnection(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        }
    }

}