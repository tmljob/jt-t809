package io.tml.iov.superior.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809HeartbeatResponse;


public class JT809HeartbeatHandler extends SimpleChannelInboundHandler<JT809HeartbeatResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JT809HeartbeatResponse msg) throws Exception {
        ctx.channel().writeAndFlush(msg);
    }
}
