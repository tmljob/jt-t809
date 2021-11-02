package io.tml.iov.inferior.client;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.tml.iov.common.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DownLinkServer {

    public void starDownLinkServer() {
        int downlinkPort = PropertiesUtil.getInteger("downlink.port");
        new ServerBootstrap().group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline()
                                .addLast(new ChannelInboundHandlerAdapter() {

                                    @Override
                                    public void channelActive(
                                            ChannelHandlerContext ctx)
                                            throws Exception {
                                        log.info("downlink connection set up");
                                        ctx.fireChannelActive();
                                    }

                                    @Override
                                    public void channelInactive(
                                            ChannelHandlerContext ctx)
                                            throws Exception {
                                        log.info("downlink connection close");
                                        ctx.fireChannelInactive();
                                    }

                                    @Override
                                    public void channelRead(
                                            ChannelHandlerContext ctx,
                                            Object msg) {
                                        ByteBuf buffer = (ByteBuf) msg;
                                        log.info(buffer.toString(
                                                Charset.defaultCharset()));
                                    }
                                });
                    }
                }).bind(downlinkPort).addListener(future -> {
                    if (future.isSuccess()) {
                        log.info(
                                "downlink server start successfully at port {}!",
                                downlinkPort);
//                CommonUtils.delDataTimer(); // 定时任务，定时删除垃圾数据
                    } else {
                        log.error("downlink server fail to start  at port {}!",
                                downlinkPort);
                    }
                });
    }

}
