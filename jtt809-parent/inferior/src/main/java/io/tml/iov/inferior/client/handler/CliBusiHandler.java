package io.tml.iov.inferior.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.tml.iov.common.packet.JT809LoginResponsePacket;
import io.tml.iov.common.util.constant.Const;
import io.tml.iov.inferior.client.DataSender;
import io.tml.iov.inferior.client.constant.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CliBusiHandler extends SimpleChannelInboundHandler<JT809LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JT809LoginResponsePacket msg)
            throws Exception {
        log.info("应答----------------" + "0x"
                + Integer.toHexString(msg.getMsgId()));
        if (msg.getMsgId() == Const.BusinessDataType.UP_CONNECT_RSP) {
            byte[] msgBody = msg.getMsgBodyByteArr();
            int result = msgBody[0];
            if (result == Const.LoginResponseCode.SUCCESS) {
                DataSender.LONGINSTATUS = Constants.LOGIN_SUCCESS;
                
                synchronized (DataSender.class) {
                    DataSender.class.notifyAll();
                }
                
                log.info("------------------登录成功");
            } else {
                log.info("------------------登录异常，请检查" + "0x0"
                        + Integer.toHexString(result));
            }
        }

    }

}
