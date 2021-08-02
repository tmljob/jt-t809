package io.tml.iov.common.util;

/**
 * 编码公共方法
 */
public class PacketEncoderUtils {

    /** 编码转义*/
    public static byte[] encoderEscape(byte[] dataBytesWithoutHeadAndEnd){
        String dataStr = PacketDecoderUtils.bytes2FullHexStr(dataBytesWithoutHeadAndEnd);
        dataStr = dataStr.replaceAll("0x5b", "0x5a0x01");
        dataStr = dataStr.replaceAll("0x5a", "0x5a0x02");
        dataStr = dataStr.replaceAll("0x5d", "0x5e0x01");
        dataStr = dataStr.replaceAll("0x5e", "0x5e0x02");
        byte[] bytes = PacketDecoderUtils.fullHexStr2Bytes(dataStr);
        return bytes;
    }
}
