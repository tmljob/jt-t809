package io.tml.iov.common.protocol.decoder;

import java.util.HashMap;
import java.util.Map;

import io.tml.iov.common.util.constant.Const;

/**
 * 解码工厂类
 */
public class DecoderFactory {
    
    private DecoderFactory() {
    }

    private static Map<Short, Decoder> factMap = new HashMap<>();
    static {
        factMap.put(Const.BusinessDataType.UP_CONNECT_REQ,
                new LoginDecoder());
        factMap.put(Const.BusinessDataType.UP_LINKTEST_REQ,
                new HeartbeatDecoder());
        factMap.put(Const.BusinessDataType.UP_EXG_MSG,
                new JT809Packet0x1202Decoder());
        factMap.put(Const.BusinessDataType.UP_CONNECT_RSP,
                new LoginResponseDecoder());
    }

    /**
     *
     * @param businessDataType 业务数据类型标志
     * @return 具体的解码器
     */
    public static Decoder getDecoder(short businessDataType) {
        return factMap.get(businessDataType);
    }
}
