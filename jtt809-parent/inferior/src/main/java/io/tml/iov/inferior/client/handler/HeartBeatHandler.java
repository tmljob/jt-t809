package io.tml.iov.inferior.client.handler;

import org.apache.commons.lang3.StringUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.tml.iov.common.packet.JT809Heartbeat;
import io.tml.iov.inferior.client.DataSender;
import lombok.extern.slf4j.Slf4j;

/**
 */
@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

            if (StringUtils.isBlank(DataSender.LONGINSTATUS)
                    || DataSender.LOGINING.equals(DataSender.LONGINSTATUS)) {
                DataSender.getInstance().login2Superior();
                log.info(
                        "try to login when the channel is idel ------ reday to login");
            }

            if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                log.info("the channle is idel, send heartbeat!");
                JT809Heartbeat heartBeat = new JT809Heartbeat();
                ctx.channel().writeAndFlush(heartBeat);
            }
        }

    }

}
