package io.tml.iov.common.protocol.decoder;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809LoginPacket;
import io.tml.iov.common.util.PacketDecoderUtils;
import io.tml.iov.common.util.constant.Const;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/21 21:02
 * @Version 1.0
 * @Describe: 登录解码器
 */
public class LoginDecoder implements Decoder {
    private static Logger log = LoggerFactory.getLogger(LoginDecoder.class);

    @Override
    public JT809BasePacket decoder(byte[] bytes) throws Exception {
        JT809LoginPacket loginPacket = new JT809LoginPacket();
        ByteBuf byteBuf = PacketDecoderUtils.baseDecoder(bytes, loginPacket);
        loginPacketDecoder(byteBuf,loginPacket);
        return loginPacket;
    }

    /**
     *
     * @param byteBuf
     * @param loginPacket
     */
    private void loginPacketDecoder(ByteBuf byteBuf,JT809LoginPacket loginPacket) throws Exception{
        ByteBuf msgBodyBuf = null;
        if (loginPacket.getEncryptFlag() == Const.EncryptFlag.NO) {
            log.info("报文未加密！继续处理。");
            msgBodyBuf = PacketDecoderUtils.getMsgBodyBuf(byteBuf);
        } else {
            // TODO: 后续处理
            log.info("报文已加密！未处理。");
            msgBodyBuf = null;
            return;
        }

        loginPacket.setUserId(msgBodyBuf.readInt());

        byte[] passwordBytes = new byte[8];
        msgBodyBuf.readBytes(passwordBytes);
        loginPacket.setPassword(new String(passwordBytes, Charset.forName("GBK")));

        // TODO ip和端口号的解析待确定
        byte[] downLinkIpBytes = new byte[32];
        msgBodyBuf.readBytes(downLinkIpBytes);
        loginPacket.setDownLinkIp(new String(downLinkIpBytes));

        loginPacket.setDownLinkPort(msgBodyBuf.readShort());

    }
}
