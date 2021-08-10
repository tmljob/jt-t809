package io.tml.iov.common.protocol.encoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;


public class LoginResponseEncoder implements Encoder {
    private static Logger log = LoggerFactory
            .getLogger(LoginResponseEncoder.class);

    @Override
    public void encode(ChannelHandlerContext ctx, JT809BasePacket packet,
            ByteBuf out) {
        log.info("start to encode login respond");
        JT809LoginResponsePacket responsePacket = (JT809LoginResponsePacket) packet;

    }

}
