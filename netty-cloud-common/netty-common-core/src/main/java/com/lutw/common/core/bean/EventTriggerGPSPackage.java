package com.lutw.common.core.bean;

import lombok.Data;

/**
 * 事件触发包
 */
@Data
public class EventTriggerGPSPackage {
    //协议号
    private String protocolData;
    //设备id
    private long deviceId;
    //星期数
    private int week;
    //周内秒
    private long second;
    //纳秒
    private Long nanosecond;
    //时间
    private String dateTime;
    //经度
    private double lot;
    //纬度
    private double lat;
    //高度
    private double height;
    //航向
    private double course;
    //俯仰角
    private double pitchAngle;
    //速度
    private double speed;
    //主天线卫星书
    private int mainAntennaSatellite;
    //副天线卫星数
    private int viceAntennaSatellite;
    //系统状态
    private String systemState;
    //卫星状态
    private String satelliteState;
}
