package com.lutw.common.core.bean;



import com.lutw.common.core.constants.ConstantValue;
import com.lutw.common.core.utils.FormatTransfer;
import lombok.Data;

import java.io.Serializable;

/**
 * <pre>
 * 自己定义的协议
 *  数据包格式
 * +—————|———----——+——-----——+——----————+——----————+
 * |信息头   |  协议号 |  包长度      |   信息序列号      |   数据       |
 * +—————|———----——+——-----——+——----————+——----————+
 * 1.信息头：占用2字节，固定为 0x67 0x67
 * 2.协议号：占用1字节
 * 3.传输数据的长度，占用2字节 包长度计算范围为 “信息序列号” 到 “信息内容” (包括  “信息序列号” 和  “信息内容”)
 * 4.信息序列号 开机后发送的第一条GPRS数据（GPS数据包，心跳包，指令包）序列号为‘1’，
 * 之后每次发送数据序列号都自动加1。到达最大值65535后，重新从1开始计数。
 * 5.信息内容
 * </pre>
 */
@Data
public class TargetDeviceProtocol implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 消息的开头的信息标志
   */
  private short headData = ConstantValue.HEAD_DATA;

  /**
   * 协议号
   */
  private byte protocolData;

  /**
   * 信息序列号
   */
  private char serialNumber;

  /**
   * 包长度
   */
  private short packageLength;

  /**
   * 消息的内容
   */
  private byte[] content;

  /**
   * 和校验
   */
  private byte checkNum;

  /**
   * 消息的尾的信息标志
   */
  private byte tailData = ConstantValue.TAIL_DATA;

  public TargetDeviceProtocol() {

  }


  public Short getPackageLength() {
    return packageLength;
  }

  public void setPackageLength(Short packageLength) {
    this.packageLength = packageLength;
  }

  public TargetDeviceProtocol(byte protocolData, short packageLength, char serialNumber, byte[] content) {
    this.protocolData = protocolData;
    this.serialNumber = serialNumber;
    this.content = content;
    this.packageLength = packageLength;

  }
  @Override
  public String toString() {
    byte[] pData = new byte[1];
    pData[0] = protocolData;
    return "TargetDeviceProtocol{" +
            "headData=" + FormatTransfer.shortToHexString(headData) +
            ", protocolData=" + FormatTransfer.bytesToHexString(pData) +
            ", packageLength=" + FormatTransfer.shortToHexString(packageLength) +
            ", serialNumber=" + FormatTransfer.charToHexString(serialNumber) +
            ", content=" + FormatTransfer.bytesToHexString(content) +
            ", checkNum=" + FormatTransfer.intToHex(checkNum & 0xff) +
            ", tailData=" + FormatTransfer.intToHex(tailData & 0xff) +
            '}';
  }
}
