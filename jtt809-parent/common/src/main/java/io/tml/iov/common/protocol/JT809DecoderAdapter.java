package io.tml.iov.common.protocol;

import static io.tml.iov.common.util.CommonUtils.PACKET_CACHE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.protocol.decoder.DecoderFactory;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.CrcUtil;
import io.tml.iov.common.util.PacketDecoderUtils;

/**
 *  解码器
 */
public class JT809DecoderAdapter extends ByteToMessageDecoder {
    private static Logger log = LoggerFactory
            .getLogger(JT809DecoderAdapter.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in,
            List<Object> out) throws Exception {
        // 判断是否有可读的字节
        if (in.readableBytes() <= 0) {
            return;
        }
        // 1、进行转义
        byte[] bytes = PacketDecoderUtils.decoderEscape(in);
        // 2、校验crc
        if (!CrcUtil.checkCRC(bytes)) {
            return;
        }
        // 3、判断是那种类型的数据，交给具体的解码器类完成。
        ByteBuf byteBuf = CommonUtils.getByteBuf(bytes);
        byteBuf.skipBytes(9);
        // 获取业务标志
        short msgId = byteBuf.readShort();

        // 交给具体的解码器
        JT809BasePacket packet = null;
        try {
            packet = DecoderFactory.getDecoder(msgId).decoder(bytes);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                // log.info("没有可用的解析器，忽略这条信息！此信息不在业务范围内。");
                // 没有可用的解析器，忽略这条信息！此信息不在业务范围内。
            } else {
                log.error("packet paser error！ error info:{};packet info:{}",
                        e.getMessage(),
                        PACKET_CACHE.get(Thread.currentThread().getName()));
            }
            return;
        }
        out.add(packet);
    }
}
