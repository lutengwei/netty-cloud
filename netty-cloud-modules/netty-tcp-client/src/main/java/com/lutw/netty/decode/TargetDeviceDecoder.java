package com.lutw.netty.decode;

import com.lutw.common.core.bean.TargetDeviceProtocol;
import com.lutw.common.core.constants.ConstantValue;
import com.lutw.common.core.utils.CheckSumCalculation;
import com.lutw.common.core.utils.FormatTransfer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <pre>
 * 自己定义的协议
 *  数据包格式
 * +—————|———----——+——-----——+——----————+——----————+
 * |信息头   |  协议号 |  包长度      |   信息序列号      |   数据       |和校验/ 信息尾
 * +—————|———----——+——-----——+——----————+——----————+
 * 1.信息头：占用2字节，固定为 0x67 0x67
 * 2.协议号：占用1字节
 * 3.传输数据的长度，占用2字节 包长度计算范围为 “信息序列号” 到 “信息内容” (包括  “信息序列号” 和  “信息内容”)
 * 4.信息序列号 开机后发送的第一条GPRS数据（GPS数据包，心跳包，指令包）序列号为‘1’，
 * 之后每次发送数据序列号都自动加1。到达最大值65535后，重新从1开始计数。
 * 5.信息内容，长度不应该超过2048，防止socket流的攻击
 * </pre>
 * @author lutw
 */
@Slf4j
public class TargetDeviceDecoder extends ByteToMessageDecoder {
    // 数据包总长度合计 7+N+1+2 byte
    public final int BASE_LENGTH = 10;

    /**
     * @Description
     * @author lutw
     * @return void
     *    1 防套节流攻击，
    2 对16进制的数据帧简单校验
    3  使用接收固定长度，解决粘包问题
     */
    //错误包统计
    long errCount = 0;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf buffer, List<Object> list) throws Exception {
        byte[] errBytes;
        // 判断包头长度
        if (buffer.readableBytes() < 10) {// 不够包头
            return;
        }
        //读取协议号
        byte protocolData ;
        short headData;
        while (true) {
            // 标记包头开始的index
            buffer.markReaderIndex();
            // 读到了协议的开始标志，结束while循环
            headData = buffer.readShort();
            protocolData = buffer.readByte();
            if (headData == ConstantValue.HEAD_DATA && protocolData < 0x13) {
                break;
            }
            // 未读到包头，略过一个字节
            // 每次略过，一个字节，去读取，包头信息的开始标记
            buffer.resetReaderIndex();
            buffer.readByte();

            // 当略过，一个字节之后，
            // 数据包的长度，又变得不满足
            // 此时，应该结束。等待后面的数据到达
            if (buffer.readableBytes() < BASE_LENGTH) {
                return;
            }

        }
        // 读取消息包长度
        short length = buffer.readShort();
        //包长度大于1024，信息头寻找错误
        if (length > 1024){
            buffer.resetReaderIndex();
            buffer.readByte();
            //buffer.discardReadBytes();
        }
        // 长度如果小于0
        if (length < 0) {// 非法数据，关闭连接
            channelHandlerContext.close();
        }
        if (length > buffer.readableBytes()) {// 读到的消息体长度如果小于传送过来的消息长度
            // 重置读取位置
            buffer.resetReaderIndex();
            return;
        }
        char serialNumberData = buffer.readChar();

        byte[] content;
        if (buffer.hasArray()) {
            //堆缓冲
            ByteBuf slice = buffer.slice(buffer.readerIndex(),length);
            content = slice.array();
        } else {
            //直接缓冲
            content = new byte[length-4];
            buffer.readBytes(content, 0, length-4);
            //buffer.discardReadBytes();
        }
        //和校验
        byte checkData = buffer.readByte();
        //信息尾
        byte tailData = buffer.readByte();

        if (tailData == ConstantValue.TAIL_DATA){
            //和校验
            if (!CheckSumCalculation.getCheckNum(serialNumberData,content,checkData)){
                errCount++;
                log.error("错误包数:"+errCount+"---和校验不正确:"+channelHandlerContext.channel());
                buffer.resetReaderIndex();
                errBytes = new byte[length+5];
                buffer.readBytes(errBytes,0,length + 5);
                log.error("错误数据："+ FormatTransfer.bytes2hex02(errBytes));
                buffer.resetReaderIndex();
                buffer.readByte();
                //buffer.discardReadBytes();
                return;
            }
        }else {
            errCount++;
            log.error("错误包数:"+errCount+"---未找到信息尾:"+channelHandlerContext.channel());
            buffer.resetReaderIndex();
            errBytes = new byte[length+5];
            buffer.readBytes(errBytes,0,length + 5);
            log.error("错误数据："+FormatTransfer.bytes2hex02(errBytes));
            buffer.resetReaderIndex();
            buffer.readByte();
            //buffer.discardReadBytes();
            return;
        }
        //buffer.discardReadBytes();
        TargetDeviceProtocol protocol = new TargetDeviceProtocol();
        protocol.setProtocolData(protocolData);
        protocol.setPackageLength(length);
        protocol.setSerialNumber(serialNumberData);
        protocol.setContent(content);
        protocol.setCheckNum(checkData);
        if ((buffer.writerIndex() - buffer.readerIndex()) > 512){
            log.warn("90====="+buffer.readerIndex()+"========"+buffer.writerIndex()+"========");
        }
        list.add(protocol);
    }

}