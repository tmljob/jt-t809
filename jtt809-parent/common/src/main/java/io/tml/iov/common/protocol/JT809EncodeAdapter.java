package io.tml.iov.common.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.PacketDecoderUtils;

/**
 *  编码适配器
 */
public class JT809EncodeAdapter extends MessageToByteEncoder<JT809BasePacket> {
    private static Logger log = LoggerFactory.getLogger(JT809EncodeAdapter.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, JT809BasePacket packet, ByteBuf out) throws Exception {
//        EncoderFactory.getEncoder(packet.getClass().getSimpleName()).encode(ctx,packet,out);
        try {
            byte[] allBody = packet.getAllBody();
            // 转义
            byte[] dataBytes = CommonUtils.doEscape4Receive(allBody, 0, allBody.length);
                    //PacketEncoderUtils.encoderEscape(allBody);
            byte[] bytes1 = CommonUtils.append(new byte[]{JT809BasePacket.HEAD_FLAG}, dataBytes);
            byte[] bytes = CommonUtils.append(bytes1, new byte[]{JT809BasePacket.END_FLAG});
            String hexStr = PacketDecoderUtils.bytes2HexStr(bytes);
            log.info("send packet：{}",hexStr);
            out.writeBytes(bytes);
            
            out.retain();
            ctx.writeAndFlush(out);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
