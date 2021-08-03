package io.tml.iov.common.packet;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.tml.iov.common.config.EncryptConfig;
import io.tml.iov.common.util.CommonUtils;
import io.tml.iov.common.util.Jtt809Util;
import io.tml.iov.common.util.constant.Const;

/**
 * 实时上传车辆定位信息包
 */
public class JT809Packet0x1202 extends JT809BasePacket {

    private static final int FIXED_LENGTH = 64;

    public JT809Packet0x1202() {
        setMsgLength(getFixedByteLength() + FIXED_LENGTH);
        setMsgSn(Const.getMsgSN());
        setMsgId(Const.BusinessDataType.UP_EXG_MSG);
        setMsgGNSSCenterId(Const.UserInfo.MSG_GNSSCENTERID);
        setVersionFlag(new byte[] { 1, 0, 0 });
        // 加密配置
        setEncryptFlag((byte) EncryptConfig.getInstance().getEncryptFlag());
        setEncryptKey(0);
    }

    /** 车牌号 21字节 */
    private String vehicleNo;
    /** 车辆颜色 1字节 */
    private byte vehicleColor;
    /** 子业务类型标识 2字节 */
    private short dataType;
    /** 后续数据长度 4字节 后续长度为36，不足补0 */
    private int dataLength;
    /** 该字段标识传输的定位信息是否使用国家测绘局批准的地图保密插件进行加密。加密标识：1-已加密，0-未加密。1字节 */
    private byte excrypt;
    /** 日月年（dmyy），年的表示是先将年转换成2为十六进制数，如2009标识为0x070xD9. 4字节 */
    private LocalDate date;
    /** 时分秒（hms） 3字节 */
    private LocalTime time;
    /** 经度，单位为1*10^-6度。 4字节 */
    private int lon;
    /** 纬度，单位为1*10^-6度。 4字节 */
    private int lat;
    /** 2字节； 速度，指卫星定位车载终端设备上传的行车速度信息，为必填项。单位为千米每小时（km/h）。 */
    private short vec1;
    /** 2字节；行驶记录速度，指车辆行驶记录设备上传的行车速度信息，为必填项。单位为千米每小时（km/h）。 */
    private short vec2;
    /** 4字节；车辆当前总里程数，值车辆上传的行车里程数。单位单位为千米（km）。 */
    private int vec3;
    /** 2字节；方向，0-359，单位为度（。），正北为0，顺时针 */
    private short direction;
    /** 2字节；海拔高度，单位为米（m）。 */
    private short altitude;
    /** 4字节；车辆状态，二进制表示 */
    private int state;
    /** 4字节；报警状态，二进制表示 */
    private int alarm;

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public byte getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(byte vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public short getDataType() {
        return dataType;
    }

    public void setDataType(short dataType) {
        this.dataType = dataType;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public byte getExcrypt() {
        return excrypt;
    }

    public void setExcrypt(byte excrypt) {
        this.excrypt = excrypt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public short getVec1() {
        return vec1;
    }

    public void setVec1(short vec1) {
        this.vec1 = vec1;
    }

    public short getVec2() {
        return vec2;
    }

    public void setVec2(short vec2) {
        this.vec2 = vec2;
    }

    public int getVec3() {
        return vec3;
    }

    public void setVec3(int vec3) {
        this.vec3 = vec3;
    }

    public short getDirection() {
        return direction;
    }

    public void setDirection(short direction) {
        this.direction = direction;
    }

    public short getAltitude() {
        return altitude;
    }

    public void setAltitude(short altitude) {
        this.altitude = altitude;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    @Override
    public byte[] getMsgBodyByteArr() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(64);
        try {
            buffer.writeBytes(CommonUtils.getBytesWithLengthAfter(21,
                    vehicleNo.getBytes(Charset.forName("GBK"))));// 21
            buffer.writeByte(1);// 1
            buffer.writeShort(
                    Const.SubBusinessDataType.UP_EXG_MSG_REAL_LOCATION);// 2
            buffer.writeInt(36);// 4
            // 是否加密
            buffer.writeByte((byte) 0);// 0未加密 // 1
            // 日月年dmyy
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            buffer.writeByte((byte) cal.get(Calendar.DATE));
            buffer.writeByte((byte) (cal.get(Calendar.MONTH) + 1));
            String hexYear = "0" + Integer.toHexString(cal.get(Calendar.YEAR));
            buffer.writeBytes(CommonUtils.hexStringToByte(hexYear));// 4
            // 时分秒
            buffer.writeByte((byte) cal.get(Calendar.HOUR_OF_DAY));
            buffer.writeByte((byte) cal.get(Calendar.MINUTE));
            buffer.writeByte((byte) cal.get(Calendar.SECOND));// 3
            // 经度，纬度
            buffer.writeInt(getLon());// 4
//            buffer.writeInt(39563620);// 4
            buffer.writeBytes(CommonUtils.int2bytes(39563620));
            // 速度
            buffer.writeShort(getVec1());// 2
            // 行驶记录速度
            buffer.writeShort(getVec2());// 2
            // 车辆当前总里程数
            buffer.writeInt((int) getVec3());// 4
            // 方向
            buffer.writeShort(getDirection());// 2
            // 海拔
            buffer.writeShort((short) 0);// 2
            // 车辆状态
            int accStatus = 0;
            int gpsStatus = 0;
            if (accStatus == 0 && gpsStatus == 0) {
                buffer.writeInt(0);// 4
            } else if (accStatus == 1 && gpsStatus == 0) {
                buffer.writeInt(1);// 4
            } else if (accStatus == 0 && gpsStatus == 1) {
                buffer.writeInt(2);// 4
            } else {
                buffer.writeInt(3);// 4
            }
            // 报警状态
            buffer.writeInt(1);// 0表示正常；1表示报警//4

            byte[] msgBody = new byte[buffer.readableBytes()];
            buffer.readBytes(msgBody);

            if (EncryptConfig.getInstance()
                    .getEncryptFlag() == Const.SWITCH_ON) {
                msgBody = Jtt809Util.encrypt(
                        EncryptConfig.getInstance().getM1(),
                        EncryptConfig.getInstance().getIa1(),
                        EncryptConfig.getInstance().getIc1(), getEncryptKey(),
                        msgBody);
            }

            return msgBody;
        } finally {
            buffer.release();
        }
    }

    @Override
    public String toString() {
        return "JT809Packet0x1202{" + "vehicleNo='" + vehicleNo + '\''
                + ", vehicleColor='" + vehicleColor + '\'' + ", dataType="
                + dataType + ", dataLength=" + dataLength + ", excrypt="
                + excrypt + ", date=" + date + ", time=" + time + ", lon='"
                + lon + '\'' + ", lat='" + lat + '\'' + ", vec1='" + vec1 + '\''
                + ", vec2='" + vec2 + '\'' + ", vec3='" + vec3 + '\''
                + ", direction='" + direction + '\'' + ", altitude='" + altitude
                + '\'' + ", state='" + state + '\'' + ", alarm='" + alarm + '\''
                + super.toString() + '}';
    }
}
