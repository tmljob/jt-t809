package io.tml.iov.common.protocol.decoder;

import io.netty.buffer.ByteBuf;
import io.tml.iov.common.config.EncryptConfig;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809LoginResponsePacket;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.Jtt809Util;
import io.tml.iov.common.util.PacketDecoderUtils;
import io.tml.iov.common.util.constant.Const;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginResponseDecoder implements Decoder {

    @Override
    public JT809BasePacket decoder(byte[] bytes)  {
        JT809LoginResponsePacket loginResponsePacket = new JT809LoginResponsePacket();
        ByteBuf byteBuf = PacketDecoderUtils.baseDecoder(bytes, loginResponsePacket);
        loginResponsePacketDecoder(byteBuf, loginResponsePacket);
        return loginResponsePacket;
    }
    
    private void loginResponsePacketDecoder(ByteBuf byteBuf,JT809LoginResponsePacket loginResponsePacket) {
        ByteBuf msgBodyBuf = null;
        if (loginResponsePacket.getEncryptFlag() == Const.Encrypt.NO) {
            log.info("packet no encry, contine to process.");
            msgBodyBuf = PacketDecoderUtils.getMsgBodyBuf(byteBuf);
        } else {
            log.info("packet is encry, continue to process.");
            byte[] msgBodyArr =  Jtt809Util.encrypt(
                    EncryptConfig.getInstance().getM1(),
                    EncryptConfig.getInstance().getIa1(),
                    EncryptConfig.getInstance().getIc1(), loginResponsePacket.getEncryptKey(),
                    PacketDecoderUtils.getMsgBodyByteArr(byteBuf));
            msgBodyBuf = CommonUtils.getByteBuf(msgBodyArr);
        }

        loginResponsePacket.setResult(msgBodyBuf.readByte());

       
    }
}
