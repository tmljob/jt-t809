package io.tml.iov.common.protocol.decoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809HeartbeatResponse;

/**
 * @Author: Xiuming Lee
 * @Date: 2019/9/23 14:53
 * @Version 1.0
 * @Describe: 心跳解码器器
 */
public class HeartbeatDecoder implements Decoder{
    private static Logger log = LoggerFactory.getLogger(HeartbeatDecoder.class);
    @Override
    public JT809BasePacket decoder(byte[] bytes) {
        log.info("心跳解码器！");
        return new JT809HeartbeatResponse();
    }
}
