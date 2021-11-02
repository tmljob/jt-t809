package io.tml.iov.common.protocol.decoder;

import io.tml.iov.common.packet.JT809BasePacket;


public interface Decoder {
    /**
     *
     * @param bytes
     * @return
     */
    JT809BasePacket decoder(byte[] bytes);
}
