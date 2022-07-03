package com.lutw.netty.tcp.handler;

import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 存储整个工程通道的全局配置
 * Timely communication
 *
 */
@Slf4j
public class TCChannelManage {

    //储每一个socket客户端接入进来时的channel对象
    public static ConcurrentMap<String, Channel> deviceChannelMap = new ConcurrentHashMap<>();
    //存储每一个webSocket客户端接入进来时的channel对象
    public static ConcurrentMap<String, Channel> webSocketChannelMap = new ConcurrentHashMap<>();
    //存储编队服务器进来时的channel对象
    public static ConcurrentMap<String, Channel> formationChannelMap = new ConcurrentHashMap<>();


    /**
     * 判断通道是否存在
     * @param id
     * @return
     */
    public static boolean hasChannel(String id,ConcurrentMap<String,Channel> channelMap) {
        Channel channel = channelMap.get(id);
        if (channel == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除map集合中的channel通道
     * @param value  id
     * @return  id
     */
    public static String removeChannel(Object value,ConcurrentMap<String, Channel> channelMap){
        String deviceId = "";
        for(Object key: channelMap.keySet()){
            if (StringUtils.isEmpty((String) key)){
                return deviceId;
            }
            if(channelMap.get(key).equals(value)){
                deviceId = (String) key;
                channelMap.remove(deviceId);
            }
        }
        return deviceId;
    }


    /**
     * 向页面中返回提示数据
     * @param message 提示信息
     */
    public static void responseMessageToPage(String message){

        Collection<Channel> values = webSocketChannelMap.values();
        Iterator<Channel> iterator = values.iterator();
        while (iterator.hasNext()){
            Channel channel = iterator.next();
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    /**
     * 判断服务器是否在线并将数据发送
     * @param body 发送的数据
     */
    public static void responseMessageToFormation(TargetDeviceProtocol body){
        if (TCChannelManage.formationChannelMap.size() == 1) {
            Collection<Channel> channels = TCChannelManage.formationChannelMap.values();
            Iterator<Channel> iterator = channels.iterator();
            Channel channel = iterator.next();
            channel.writeAndFlush(body);
        } else {
            Collection<Channel> values = TCChannelManage.webSocketChannelMap.values();
            Iterator<Channel> iterator = values.iterator();
            while (iterator.hasNext()){
                Channel channel = iterator.next();
                channel.writeAndFlush(new TextWebSocketFrame("编队服务器断开"));
            }
        }
    }

    /**
     * 发送数据到终端设备
     * @param body 发送数据
     */
    public static void responseMessageToDevice(TargetDeviceProtocol body){
        Collection<Channel> values = TCChannelManage.deviceChannelMap.values();
        Iterator<Channel> iterator = values.iterator();
        while (iterator.hasNext()){
            Channel channel = iterator.next();
            channel.writeAndFlush(body);
        }
    }

    /**
     * 发送数据到终端设备
     */
    public static void responseMessageToDevice(String deviceId, TargetDeviceProtocol body){

        if (hasChannel(deviceId,deviceChannelMap)){
            Channel channel = deviceChannelMap.get(deviceId);
            channel.writeAndFlush(body);
        }else {
            responseMessageToPage("终端不在线");
        }
    }

}
