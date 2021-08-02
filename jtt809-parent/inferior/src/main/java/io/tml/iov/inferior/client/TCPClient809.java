package io.tml.iov.inferior.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.tml.iov.common.protocol.JT809DecoderAdapter;
import io.tml.iov.common.protocol.JT809EncodeAdapter;
import io.tml.iov.inferior.client.handler.CliBusiHandler;
import io.tml.iov.inferior.client.handler.HeartBeatHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TCPClient809 {

    private int connectTimeoutMillis = 3000;

    private boolean tcpNoDelay = false;

    private boolean reuseAddress = true;

    private boolean keepAlive = true;

    private Bootstrap bootstrap = null;

    private static TCPClient809 instance = new TCPClient809();

    private TCPClient809() {
        init();
    }

    public static TCPClient809 getInstance() {
        return instance;
    }

    private void init() {
        bootstrap = new Bootstrap();
        bootstrap.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                connectTimeoutMillis);
        bootstrap.option(ChannelOption.SO_REUSEADDR, reuseAddress);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
    }

    public Channel getChannel(String ip, int port)  {
        int a = 0x5d;
        final ByteBuf delimiter = Unpooled.buffer(1);
        delimiter.writeByte(a);
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                                .addLast(new LoggingHandler(LogLevel.DEBUG));
                        // InBound
                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(
                                Integer.MAX_VALUE, false, delimiter));
                        ch.pipeline().addLast(new IdleStateHandler(0,20,0, TimeUnit.SECONDS));//设置空闲心跳机制
                        ch.pipeline().addLast(new HeartBeatHandler());
                        ch.pipeline().addLast(new JT809DecoderAdapter());
                        // OutBound
                        ch.pipeline().addLast(new JT809EncodeAdapter());
                        //InBound
                        ch.pipeline().addLast(new CliBusiHandler());// 反馈数据处理
                    }
                });
        try {
            return bootstrap.connect(ip, port).sync().channel();
        } catch (InterruptedException e) {
            log.error("TCPClient809 getChannel error!");
            return null;
        }
    }

}
