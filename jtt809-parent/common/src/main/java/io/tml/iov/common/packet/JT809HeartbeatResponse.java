package io.tml.iov.common.packet;

/**
 * 心跳应答包 数据体为空
 */

public class JT809HeartbeatResponse extends JT809BasePacket {

    @Override
    public byte[] getMsgBodyByteArr() {
        return new byte[0];
    }
}
