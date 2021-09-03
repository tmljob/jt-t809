package io.tml.iov.superior.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.tml.iov.common.protocol.JT809DecoderAdapter;
import io.tml.iov.common.protocol.JT809EncodeAdapter;


public class JT809ServerInitialzer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ByteBuf delimiter = Unpooled.copiedBuffer(new byte[]{0x5d});
        //in&out bound
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        //in-bound
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, false, delimiter));
        //in&out bound
        ch.pipeline().addLast(new JT809IdleStateHandler());
        //in-bound
        ch.pipeline().addLast(new JT809AdapterHandler());
        //in-bound
        ch.pipeline().addLast(new JT809DecoderAdapter());
        //in-bound
        ch.pipeline().addLast(new JT809HeartbeatHandler());
        //in-bound
        ch.pipeline().addLast(new JT809LoginHandler());
       //in-bound
        ch.pipeline().addLast(new JT809Packet0x1202Handler());
        //out-bound
        ch.pipeline().addLast(new JT809EncodeAdapter());
        
        //暂时没有作用
        ch.pipeline().addLast(new ExceptionCaughtHandler());
    }
}
