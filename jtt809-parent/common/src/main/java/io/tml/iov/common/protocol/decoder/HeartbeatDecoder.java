package io.tml.iov.common.protocol.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809HeartbeatResponse;

/**
 * 心跳解码器器
 */
public class HeartbeatDecoder implements Decoder{
    private static Logger log = LoggerFactory.getLogger(HeartbeatDecoder.class);
    @Override
    public JT809BasePacket decoder(byte[] bytes) {
        log.info("heartbeat decode!");
        return new JT809HeartbeatResponse();
    }
}
