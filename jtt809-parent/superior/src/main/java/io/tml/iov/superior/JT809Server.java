package io.tml.iov.superior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.superior.handler.JT809ServerInitialzer;

/**
 * 启动类
 */
public class JT809Server {
    private static Logger log = LoggerFactory.getLogger(JT809Server.class);

    private static int PORT;

    public static void main(String[] args) {
        PORT = Integer.parseInt(PropertiesUtil.getProperty("port", "9090"));
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new JT809ServerInitialzer());
        bind(serverBootstrap, PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                log.info("JT809 Server start successfully at port {}!", port);
            } else {
                log.error("JT809 Server fail to start at port {}!", port);
            }
        });
    }
}
