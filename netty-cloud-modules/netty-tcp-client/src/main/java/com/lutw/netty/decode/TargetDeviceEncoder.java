package com.lutw.netty.decode;


import com.lutw.common.core.bean.TargetDeviceProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

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
 * 4.信息序列号 开机后发送的第一条gprs数据（gps数据包，心跳包，指令包）序列号为‘1’，
 * 之后每次发送数据序列号都自动加1。到达最大值65535后，重新从1开始计数。
 * 5.信息内容
 * </pre>
 */
public class TargetDeviceEncoder extends MessageToByteEncoder<TargetDeviceProtocol> {

  @Override
  protected void encode(ChannelHandlerContext channelHandlerContext,
                        TargetDeviceProtocol msg, ByteBuf byteBuf) throws Exception {
    // 写入消息SmartGps的具体内容
    byteBuf.writeShort(msg.getHeadData());// 信息头
    byteBuf.writeByte(msg.getProtocolData());// 协议号
    byteBuf.writeShort(msg.getPackageLength());// 包长度
    byteBuf.writeChar(msg.getSerialNumber());// 信息序列号
    if(msg.getContent()!=null) {
      byteBuf.writeBytes(msg.getContent());// 信息内容
    }
    byteBuf.writeByte(msg.getCheckNum());// 和校验
    byteBuf.writeByte(msg.getTailData());// 信息尾
  }
}
