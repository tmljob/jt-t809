package io.tml.iov.common.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.PacketDecoderUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 编码适配器
 */
@Slf4j
public class JT809EncodeAdapter extends MessageToByteEncoder<JT809BasePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, JT809BasePacket packet,
            ByteBuf out) throws Exception {
        try {
            byte[] allBody = packet.getAllBody();
            // 转义
            byte[] dataBytes = CommonUtils.doEscape4Receive(allBody, 0,
                    allBody.length);
            byte[] bytes1 = CommonUtils.append(
                    new byte[] { JT809BasePacket.HEAD_FLAG }, dataBytes);
            byte[] bytes = CommonUtils.append(bytes1,
                    new byte[] { JT809BasePacket.END_FLAG });
            String hexStr = PacketDecoderUtils.bytes2HexStr(bytes);
            log.info("send packet:{}", hexStr);
            out.writeBytes(bytes);
            
//            out.retain();
//            ctx.writeAndFlush(out);
            ctx.flush();
        } catch (Exception e) {
            log.error("JT809EncodeAdapter encode error!", e);
        }
    }
}
