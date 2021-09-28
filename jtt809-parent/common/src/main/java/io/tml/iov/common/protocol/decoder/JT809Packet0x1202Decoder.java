package io.tml.iov.common.protocol.decoder;

import static io.tml.iov.common.util.CommonUtils.PACKET_CACHE;

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
import io.tml.iov.common.util.constant.Const;


public class JT809Packet0x1202Decoder implements Decoder {
    private static Logger log = LoggerFactory
            .getLogger(JT809Packet0x1202Decoder.class);

    @Override
    public JT809BasePacket decoder(byte[] bytes) throws Exception {
        JT809Packet0x1202 jt809Packet0x1202 = new JT809Packet0x1202();
        ByteBuf byteBuf = PacketDecoderUtils.baseDecoder(bytes,
                jt809Packet0x1202);
        packetDecoder(byteBuf, jt809Packet0x1202);
        return jt809Packet0x1202;
    }

    private void packetDecoder(ByteBuf byteBuf, JT809Packet0x1202 packet)
            throws Exception {
        ByteBuf msgBodyBuf = null;
        if (packet.getEncryptFlag() == Const.EncryptFlag.NO) {
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
        // 车牌号
        byte[] vehicleNoBytes = new byte[21];
        msgBodyBuf.readBytes(vehicleNoBytes);
        packet.setVehicleNo(
                new String(vehicleNoBytes, Charset.forName("GBK")).trim());
        // 车辆颜色
        packet.setVehicleColor(msgBodyBuf.readByte());
        // 子业务类型标识
        packet.setDataType(msgBodyBuf.readShort());
        // 如果不是定位的数据，抛出空指针错误,解码适配器会对空指针错误做处理。
        if (packet
                .getDataType() != Const.SubBusinessDataType.UP_EXG_MSG_REAL_LOCATION) {
            throw new NullPointerException();
        }
        // 后续数据长度
        packet.setDataLength(msgBodyBuf.readInt());
        // 经纬度信息是否按国标进行加密
        packet.setExcrypt(msgBodyBuf.readByte());
        if (packet.getExcrypt() == Const.EncryptFlag.YES) {
            log.error("lon/lat info is encry, packet is {}",
                    PACKET_CACHE.get(Thread.currentThread().getName()));
        }
        // 跳过时间
//        msgBodyBuf.skipBytes(7);
        int day = Byte.toUnsignedInt(msgBodyBuf.readByte());
        int month = Byte.toUnsignedInt(msgBodyBuf.readByte());
        packet.setDate(LocalDate.of(msgBodyBuf.readShort(), month, day));
        packet.setTime(LocalTime.of(Byte.toUnsignedInt(msgBodyBuf.readByte()),
                Byte.toUnsignedInt(msgBodyBuf.readByte()),
                Byte.toUnsignedInt(msgBodyBuf.readByte())));
        // 经纬度
        packet.setLon(msgBodyBuf.readInt());
        packet.setLat(msgBodyBuf.readInt());
        // 速度
        packet.setVec1(msgBodyBuf.readShort());
        // 行驶记录速度
        packet.setVec2(msgBodyBuf.readShort());
        // 车辆当前总里程数
        packet.setVec3(msgBodyBuf.readInt());
        // 方向
        packet.setDirection(msgBodyBuf.readShort());
        // 海拔
        packet.setAltitude(msgBodyBuf.readShort());
        // 车辆状态
        packet.setState(msgBodyBuf.readInt());
        // 报警状态
        packet.setAlarm(msgBodyBuf.readInt());
    }
    
    private void decodePrt2019(JT809Packet0x1202 packet, ByteBuf msgBodyBuf) {
        // 车牌号
        byte[] vehicleNoBytes = new byte[21];
        msgBodyBuf.readBytes(vehicleNoBytes);
        packet.setVehicleNo(
                new String(vehicleNoBytes, Charset.forName("GBK")).trim());
        // 车辆颜色
        packet.setVehicleColor(msgBodyBuf.readByte());
        // 子业务类型标识
        packet.setDataType(msgBodyBuf.readShort());
        // 如果不是定位的数据，抛出空指针错误,解码适配器会对空指针错误做处理。
        if (packet
                .getDataType() != Const.SubBusinessDataType.UP_EXG_MSG_REAL_LOCATION) {
            throw new NullPointerException();
        }
        // 后续数据长度
        packet.setDataLength(msgBodyBuf.readInt());
        // 经纬度信息是否按国标进行加密
        packet.setExcrypt(msgBodyBuf.readByte());
        if (packet.getExcrypt() == Const.EncryptFlag.YES) {
            log.error("lon/lat info is encry, packet is {}",
                    PACKET_CACHE.get(Thread.currentThread().getName()));
        }
        
        //车辆定位信息数据长度
        int locLen = msgBodyBuf.readInt();
        //跳过报警标志&状态4+4
        msgBodyBuf.skipBytes(8);
        // 纬度、经度
        packet.setLat(msgBodyBuf.readInt());
        packet.setLon(msgBodyBuf.readInt());
        // 海拔
        packet.setAltitude(msgBodyBuf.readShort());
        // 速度
        packet.setVec1(msgBodyBuf.readShort());
        // 方向
        packet.setDirection(msgBodyBuf.readShort());
        //时间
        byte[] bcdTime = new byte[6];
        msgBodyBuf.readBytes(bcdTime);
        String bcdTimeStr = PacketDecoderUtils.bytes2HexStr(bcdTime);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyMMddHHmmss");
        LocalDateTime localDateTime = LocalDateTime.parse(bcdTimeStr, df);
        packet.setDate(localDateTime.toLocalDate());
        packet.setTime(localDateTime.toLocalTime());
        //(11+4)*5
        msgBodyBuf.skipBytes(45);
       //TODO: 位置附加信息解析
    }

}
