package io.tml.iov.common.packet;

import io.tml.iov.common.util.PropertiesUtil;
import io.tml.iov.common.util.constant.Const;

/**
 * 心跳应答包 数据体为空
 */

public class JT809HeartbeatResponse extends JT809BasePacket {
    
    public JT809HeartbeatResponse() {
        setMsgLength(JT809BasePacket.getFixedByteLength());
        setMsgSn(Const.getMsgSN());
        setMsgId(Const.BusinessDataType.UP_LINKTEST_RSP);
        setMsgGNSSCenterId(PropertiesUtil.getInteger("netty.server.centerId"));
        setVersionFlag(new byte[]{1,0,0});
        setEncryptFlag(Const.Encrypt.NO);
        setEncryptKey(0);
    }

    @Override
    public byte[] getMsgBodyByteArr() {
        return new byte[0];
    }
}
