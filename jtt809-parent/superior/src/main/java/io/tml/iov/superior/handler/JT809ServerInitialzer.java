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
        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(Integer.MAX_VALUE, false, delimiter));
        ch.pipeline().addLast(new JT809IdleStateHandler());
        ch.pipeline().addLast(new JT809AdapterHandler());
        ch.pipeline().addLast(new JT809DecoderAdapter());
        ch.pipeline().addLast(new JT809HeartbeatHandler());
        ch.pipeline().addLast(new JT809LoginHandler());
        ch.pipeline().addLast(new JT809Packet0x1202Handler());
        ch.pipeline().addLast(new JT809EncodeAdapter());
    }
}
