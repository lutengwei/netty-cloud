package com.lutw.netty.server;

import com.lutw.netty.tcp.decode.TargetDeviceDecoder;
import com.lutw.netty.tcp.decode.TargetDeviceEncoder;
import com.lutw.netty.tcp.handler.TcpOnlinePacketHandle;
import com.lutw.netty.tcp.handler.TcpServerExceptionHandler;
import com.lutw.netty.tcp.handler.TcpServerHandler;
import com.lutw.netty.websocket.handler.WebSocketFrameHandler;
import com.lutw.netty.websocket.handler.WebSocketOnlineHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 解析请求类型 webSocket、socket
 * @ClassName SelectDecode
 * @Description
 * @Author ltw
 * @Date 2020-08-17 19:26
 * @Version V1.0
 */

@Slf4j
@Component
public class SelectDecode extends ByteToMessageDecoder {
    private static final String WEBSOCKET_PATH = "/websocket";
    /** 默认暗号长度为23 */
    private static final int MAX_LENGTH = 23;
    /** WebSocket握手的协议前缀 */
    private static final String WEBSOCKET_PREFIX = "GET /";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        try {
            String protocol = getBufStart(in);
            ChannelPipeline pipeline = ctx.channel().pipeline();
            if (protocol.startsWith(WEBSOCKET_PREFIX)) {
                // Netty内置HTTP请求的解码器和编码器,报文级别
                pipeline.addLast(new HttpServerCodec());
                // ChunkedWriteHandler 是用于大数据的分区传输
                // 主要用于处理大数据流，比如一个1G大小的文件如果你直接传输肯定会撑暴jvm内存的;
                pipeline.addLast(new ChunkedWriteHandler());
                // 解析Http消息体请求用的
                // 把多个消息转换为一个单一的完全FullHttpRequest或是FullHttpResponse，
                // 原因是HTTP解码器会在每个HTTP消息中生成多个消息对象HttpRequest/HttpResponse,HttpContent,LastHttpContent
                pipeline.addLast(new HttpObjectAggregator(65536));
                // WebSocket数据压缩
                pipeline.addLast(new WebSocketServerCompressionHandler());
                // WebSocketServerProtocolHandler是配置websocket的监听地址/协议包长度限制
                pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true,10*1024));
                // 此事件被 HeartbeatHandler 的 userEventTriggered 方法处理到
                //pipeline.addLast(new IdleStateHandler(15,0,0, TimeUnit.SECONDS));
                pipeline.addLast(new WebSocketOnlineHandler());
                pipeline.addLast(new WebSocketFrameHandler());
            }else{
                //pipeline.addLast(new IdleStateHandler(15,0,0,TimeUnit.SECONDS));
                //pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                pipeline.addLast("decoder",new TargetDeviceDecoder());
                pipeline.addLast("encoder",new TargetDeviceEncoder());
                pipeline.addLast("online",new TcpOnlinePacketHandle());
                pipeline.addLast("exception",new TcpServerExceptionHandler());
                pipeline.addLast("tcpserver",new TcpServerHandler());
                log.info("收到Tcp请求");
            }
            in.resetReaderIndex();
            pipeline.remove(this);
        } catch (BeansException e) {
            log.error(e.getMessage(),e);
        }
    }

    /**
     * 解析请求数据判断是否是WebSocket请求
     * @param in 缓存数据
     * @return
     */
    private String getBufStart(ByteBuf in){
        int length = in.readableBytes();
        if (length > MAX_LENGTH) {
            length = MAX_LENGTH;
        }

        // 标记读位置
        in.markReaderIndex();
        byte[] content = new byte[length];
        in.readBytes(content);
        return new String(content);
    }


}
