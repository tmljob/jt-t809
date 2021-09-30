package io.tml.iov.common.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CrcUtil {

    public static boolean checkCRC(byte[] bytes) {
        ByteBuf byteBuf = CommonUtils.getByteBuf(bytes);
//        short businessType = CommonUtils.bytes2Short(new byte[] { bytes[9], bytes[10]});
        // 获取数据长度和crc标示
        byteBuf.skipBytes(1);
        int msgLength = CommonUtils.bytes2int(
                new byte[] { bytes[1], bytes[2], bytes[3], bytes[4] });
//        if(Const.BusinessDataType.UP_CONNECT_RSP == businessType) {
//            msgLength = 31;
//        }
        int crcLength = msgLength - 4;
       
        log.info("bytebuf is {}",ByteBufUtil.hexDump(byteBuf));
        byte[] crcBody = new byte[crcLength];
        byteBuf.readBytes(crcBody);
        log.info("bytebuf execude crcbody is {}",ByteBufUtil.hexDump(byteBuf));

        short oldCRCcode = byteBuf.readShort();
        short currentCRCcode = (short) getCRC16(crcBody);
        if (oldCRCcode == currentCRCcode) {
            log.info("crc code is {} ", oldCRCcode);
            return true;
        }
        log.error("crc check error, packet is {}",
                CommonUtils.PACKET_CACHE.get(Thread.currentThread().getName()));
        return false;
    }

    public static int getCRC16(byte[] bytes) {
        int crc = (short) 0xffff;
        int polynomial = 0x1021;
        for (int index = 0; index < bytes.length; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }
        crc &= 0xffff;
        return crc;
    }

}
