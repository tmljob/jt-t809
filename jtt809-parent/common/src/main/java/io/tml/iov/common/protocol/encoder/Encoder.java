package io.tml.iov.common.protocol.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.tml.iov.common.packet.JT809BasePacket;


public interface Encoder {
    /** 编码*/
    void encode(ChannelHandlerContext ctx, JT809BasePacket packet, ByteBuf out);
}
