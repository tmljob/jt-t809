package io.tml.iov.common.protocol.decoder;

import io.netty.buffer.ByteBuf;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;
import io.tml.iov.common.util.PacketDecoderUtils;
import io.tml.iov.common.util.constant.Const;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginResponseDecoder implements Decoder {

    @Override
    public JT809BasePacket decoder(byte[] bytes) throws Exception {
        JT809LoginResponsePacket loginResponsePacket = new JT809LoginResponsePacket();
        ByteBuf byteBuf = PacketDecoderUtils.baseDecoder(bytes, loginResponsePacket);
        loginResponsePacketDecoder(byteBuf, loginResponsePacket);
        return loginResponsePacket;
    }
    
    private void loginResponsePacketDecoder(ByteBuf byteBuf,JT809LoginResponsePacket loginResponsePacket) throws Exception{
        ByteBuf msgBodyBuf = null;
        if (loginResponsePacket.getEncryptFlag() == Const.EncryptFlag.NO) {
            log.info("packet no encry, contine to process.");
            msgBodyBuf = PacketDecoderUtils.getMsgBodyBuf(byteBuf);
        } else {
            // TODO: 后续处理
            log.info("packet is encry, not to process.");
            msgBodyBuf = null;
            return;
        }

        loginResponsePacket.setResul(msgBodyBuf.readByte());;

       
    }
}
