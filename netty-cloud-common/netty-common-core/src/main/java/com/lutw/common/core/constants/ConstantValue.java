package com.lutw.common.core.constants;

/**
 * 静态常量
 * @Author ltw
 * @Date 2020-09-07 09:17:00
 * @Version V1.0
 */
public class ConstantValue {
    public static final String NETTY_SERVER_IP = "0.0.0.0";
    public static final int NETTY_SERVER_PORT = 8080;
    //信息头
    public static final short HEAD_DATA = 0x6767;
    //信息尾
    public static final byte TAIL_DATA = (byte) (0xee & 0xff);//238, 16进制转换ee
    //redis(信息序列号)集合
    public static final String SERIALNUMBER_LIST = "SERIALNUMBER_LIST";
    //redis（指令日志）集合
    public static final String COMMAND_LIST = "COMMAND_LIST";
    //获取每一个设备连接ip地址
    public static final String DEVICE_IP_URL_LIST = "DEVICE_IP_URL_LIST";
    //指挥中心
    public static final String COMMAND_CENTER_LIST = "COMMAND_CENTER_LIST";




}
