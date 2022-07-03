package com.lutw.common.core.utils;

import com.lutw.common.core.bean.TargetDeviceProtocol;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @ClassName CheckSumCalculation
 * @Description
 * @Author ltw
 * @Date 2021-03-17 19:26
 * @Version V1.0
 */
@Slf4j
@Component
public class CheckSumCalculation {



    /**
     * 判断和校验是否正确
     * 和校验 = 信息序列号+ 信息内容
     * @param serialNumber 信息序列号
     * @param content 信息内容
     * @param check 和校验
     * @return
     */
    public static boolean getCheckNum(char serialNumber, byte[] content,byte check) {
        byte checkNumByte = 0x00;
        byte[] sn = FormatTransfer.charToByteNew(serialNumber);
        for (byte b : sn) {
            checkNumByte = (byte) (checkNumByte + (byte) (b & 0xff));
        }
        for (byte b : content) {
            checkNumByte = (byte) (checkNumByte + (byte) (b & 0xff));
        }
        if (check != checkNumByte){
            return false;
        }else {
            return true;
        }
    }

    /**
     * 消息响应数据包
     *
     * @param body   body
     * @param status 校验和是否正确
     * @return
     */
    public static TargetDeviceProtocol getIsSuccess(TargetDeviceProtocol body, boolean status) {
        short packageLength = 0x05;//包长度
        byte[] success = {0x01};//返回正确
        byte[] fail = {0x02};//返回失败
        body.setPackageLength(packageLength);
        if (status) {
            body.setContent(success);
        } else {
            body.setContent(fail);
        }
        byte[] charSerialNumber = FormatTransfer.charToByteNew(body.getSerialNumber());
        byte checkNum = CheckSumCalculation.getCheckNum(charSerialNumber, body.getContent());
        body.setCheckNum(checkNum);
        return body;
    }

    /**
     * 计算和校验
     *
     * @param sn      信息序列号
     * @param content 信息内容
     * @return 和校验
     */
    public static byte getCheckNum(byte[] sn, byte[] content) {
        byte checkNumByte = 0x00;
        for (byte b : sn) {
            checkNumByte = (byte) (checkNumByte + (byte) (b & 0xff));
        }
        for (byte b : content) {
            checkNumByte = (byte) (checkNumByte + (byte) (b & 0xff));
        }
        return checkNumByte;
    }

    /**
     * 判断编队服务器是否正常
     * @param body
     */
    /*public static void isFormationNormal(TargetDeviceProtocol body){
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
    }*/

    /**
     * 解析定位状态
     * @param positioningStatus
     * @return
     */
    public static char[] positioningStatus(String positioningStatus){
        int i = FormatTransfer.decodeHEX(positioningStatus);
        return String.valueOf(i).toCharArray();
    }

    /**
     * 设备id长度不够4位，前面补0
     * @param deviceId
     * @return
     */
    public static String deviceIdZeroPadding(Long deviceId){
        String s = String.valueOf(deviceId);
        if (s.length() == 1) {
            s = "000" + s;
        } else if (s.length() == 2) {
            s = "00" + s;
        } else if (s.length() == 3) {
            s = "0" + s;
        }
        return s;
    }

}
