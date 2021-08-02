package io.tml.iov.common.protocol.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/23 10:57
 * @Version 1.0
 * @Describe:
 */
public class LoginResponseEncoder implements Encoder {
    private static Logger log = LoggerFactory.getLogger(LoginResponseEncoder.class);
    @Override
    public void encode(ChannelHandlerContext ctx, JT809BasePacket packet, ByteBuf out) {
        log.info("登录响应开始编码！");
        JT809LoginResponsePacket responsePacket = (JT809LoginResponsePacket) packet;

    }

}
