package io.tml.iov.superior.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ExceptionCaughtHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        super.exceptionCaught(ctx, cause);
        
        Channel channel = ctx.channel();
        if(channel.isActive()) {
            ctx.close();
        }
    }
}
