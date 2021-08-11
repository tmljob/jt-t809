package io.tml.iov.superior.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 适配处理器
 */
public class JT809AdapterHandler extends ChannelInboundHandlerAdapter {
    private static Logger log = LoggerFactory.getLogger(JT809AdapterHandler.class);


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("{}客户端已连接",ctx.name());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("{}客户端关闭",ctx.name());
        super.channelInactive(ctx);
    }


}
