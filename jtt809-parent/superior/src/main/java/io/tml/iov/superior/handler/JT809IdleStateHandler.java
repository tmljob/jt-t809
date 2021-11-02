package io.tml.iov.superior.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class JT809IdleStateHandler extends IdleStateHandler {
    private static Logger log = LoggerFactory
            .getLogger(JT809IdleStateHandler.class);

    private static final int READER_IDLE_TIME = 60;

    public JT809IdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        log.info("{}秒内未读到数据，关闭连接", READER_IDLE_TIME);
        ctx.channel().close();
    }
}
