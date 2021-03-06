package io.tml.iov.common.protocol.decoder;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.tml.iov.common.config.EncryptConfig;
import io.tml.iov.common.config.ProtocalVersionConfig;
import io.tml.iov.common.packet.JT809BasePacket;
import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.Jtt809Util;
import io.tml.iov.common.util.PacketDecoderUtils;
import io.tml.iov.common.util.ThreadPacketCache;
import io.tml.iov.common.util.constant.Const;

public class JT809Packet0x1202Decoder implements Decoder {
    private static Logger log = LoggerFactory
            .getLogger(JT809Packet0x1202Decoder.class);

    @Override
    public JT809BasePacket decoder(byte[] bytes) {
        JT809Packet0x1202 jt809Packet0x1202 = new JT809Packet0x1202();
        ByteBuf byteBuf = PacketDecoderUtils.baseDecoder(bytes,
                jt809Packet0x1202);
        packetDecoder(byteBuf, jt809Packet0x1202);
        return jt809Packet0x1202;
    }

    private void packetDecoder(ByteBuf byteBuf, JT809Packet0x1202 packet) {
        ByteBuf msgBodyBuf = null;
        if (packet.getEncryptFlag() == Const.Encrypt.NO) {
            msgBodyBuf = PacketDecoderUtils.getMsgBodyBuf(byteBuf);
        } else {
            log.info("packet is encry, continue to process.");
            byte[] msgBodyArr = Jtt809Util.encrypt(
                    EncryptConfig.getInstance().getM1(),
                    EncryptConfig.getInstance().getIa1(),
                    EncryptConfig.getInstance().getIc1(),
                    packet.getEncryptKey(),
                    PacketDecoderUtils.getMsgBodyByteArr(byteBuf));
            msgBodyBuf = CommonUtils.getByteBuf(msgBodyArr);
        }

        if (ProtocalVersionConfig.getInstance().getVersion()
                .equalsIgnoreCase(Const.ProtocalVersion.VERSION_2019)) {
            decodePrt2019(packet, msgBodyBuf);
        } else {
            decodePrt2011(packet, msgBodyBuf);
        }

    }

    private void decodePrt2011(JT809Packet0x1202 packet, ByteBuf msgBodyBuf) {
        parserDataHead(packet, msgBodyBuf);
        // ????????????
        int day = Byte.toUnsignedInt(msgBodyBuf.readByte());
        int month = Byte.toUnsignedInt(msgBodyBuf.readByte());
        packet.setDate(LocalDate.of(msgBodyBuf.readShort(), month, day));
        packet.setTime(LocalTime.of(Byte.toUnsignedInt(msgBodyBuf.readByte()),
                Byte.toUnsignedInt(msgBodyBuf.readByte()),
                Byte.toUnsignedInt(msgBodyBuf.readByte())));
        // ?????????
        packet.setLon(msgBodyBuf.readInt());
        packet.setLat(msgBodyBuf.readInt());
        // ??????
        packet.setVec1(msgBodyBuf.readShort());
        // ??????????????????
        packet.setVec2(msgBodyBuf.readShort());
        // ????????????????????????
        packet.setVec3(msgBodyBuf.readInt());
        // ??????
        packet.setDirection(msgBodyBuf.readShort());
        // ??????
        packet.setAltitude(msgBodyBuf.readShort());
        // ????????????
        packet.setState(msgBodyBuf.readInt());
        // ????????????
        packet.setAlarm(msgBodyBuf.readInt());
    }

    private void parserDataHead(JT809Packet0x1202 packet, ByteBuf msgBodyBuf) {
        // ?????????
        byte[] vehicleNoBytes = new byte[21];
        msgBodyBuf.readBytes(vehicleNoBytes);
        packet.setVehicleNo(
                new String(vehicleNoBytes, Charset.forName("GBK")).trim());
        // ????????????
        packet.setVehicleColor(msgBodyBuf.readByte());
        // ?????????????????????
        packet.setDataType(msgBodyBuf.readShort());
        // ???????????????????????????????????????????????????,????????????????????????????????????????????????
        if (packet
                .getDataType() != Const.SubBusinessDataType.UP_EXG_MSG_REAL_LOCATION) {
            throw new NullPointerException();
        }
        // ??????????????????
        packet.setDataLength(msgBodyBuf.readInt());
        // ??????????????????????????????????????????
        packet.setExcrypt(msgBodyBuf.readByte());
        if (packet.getExcrypt() == Const.Encrypt.YES) {
            log.error("lon/lat info is encry, packet is {}",
                    ThreadPacketCache.get(Thread.currentThread().getName()));
        }
    }

    private void decodePrt2019(JT809Packet0x1202 packet, ByteBuf msgBodyBuf) {
        parserDataHead(packet, msgBodyBuf);

        // ??????????????????????????????
        int locLen = msgBodyBuf.readInt();
        // ??????????????????&??????4+4
        msgBodyBuf.skipBytes(8);
        // ???????????????
        packet.setLat(msgBodyBuf.readInt());
        packet.setLon(msgBodyBuf.readInt());
        // ??????
        packet.setAltitude(msgBodyBuf.readShort());
        // ??????
        packet.setVec1(msgBodyBuf.readShort());
        // ??????
        packet.setDirection(msgBodyBuf.readShort());
        // ??????
        byte[] bcdTime = new byte[6];
        msgBodyBuf.readBytes(bcdTime);
        String bcdTimeStr = PacketDecoderUtils.bytes2HexStr(bcdTime);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(bcdTimeStr, df);
        packet.setDate(localDateTime.toLocalDate());
        packet.setTime(localDateTime.toLocalTime());

        // ????????????????????????
        int locAttachLen = locLen - 28;
        while (locAttachLen > 0) {
            byte attchId = msgBodyBuf.readByte();
            byte attchLen = msgBodyBuf.readByte();
            ByteBuf attchInfo = msgBodyBuf.readBytes(attchLen);

            if (Const.LocAttachInfo.MILE == attchId) {
                packet.setVec3(attchInfo.readInt());
            }

            locAttachLen = locAttachLen - 1 - 1 - attchLen;
        }

        // (11+4)*5
        msgBodyBuf.skipBytes(45);
    }

}
