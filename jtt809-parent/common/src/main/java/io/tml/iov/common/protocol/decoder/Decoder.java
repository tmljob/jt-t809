package io.tml.iov.common.protocol.decoder;

import io.tml.iov.common.packet.JT809BasePacket;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/21 21:12
 * @Version 1.0
 * @Describe:
 */
public interface Decoder {
    /**
     *
     * @param bytes
     * @return
     */
    JT809BasePacket decoder(byte[] bytes) throws Exception;
}
