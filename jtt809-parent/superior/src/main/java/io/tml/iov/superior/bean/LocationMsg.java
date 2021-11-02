package io.tml.iov.superior.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.tml.iov.common.packet.JT809Packet0x1202;
import io.tml.iov.superior.util.CoordinateTransformUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationMsg {

    private String addressDate;
    private String latitudeWGS84;
    private String latitudeWebMercator;
    private String longtitudeWGS84;
    private String longtitudeWebMercator;
    private int altitude;
    private int direction;
    private int mileage;
    private int speed;
    private String vehicleNo;

    public static LocationMsg convert(JT809Packet0x1202 pack) {
        LocationMsg msg = new LocationMsg();

        LocalDateTime time = LocalDateTime.of(pack.getDate(), pack.getTime());
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss");
        msg.setAddressDate(time.format(formatter));

        Double lon = pack.getLon() / 1000000.0d;
        Double lat = pack.getLat() / 1000000.0d;

        msg.setLatitudeWGS84(String.format("%.6f", lat));
        msg.setLongtitudeWGS84(String.format("%.6f", lon));

        BigDecimal[] bigDecimals = CoordinateTransformUtils.zuobiao2mokatuo(
                BigDecimal.valueOf(lon), BigDecimal.valueOf(lat));
        msg.setLongtitudeWebMercator(bigDecimals[0].toString());
        msg.setLatitudeWebMercator(bigDecimals[1].toString());

        msg.setAltitude(pack.getAltitude());
        msg.setDirection(pack.getDirection());
        msg.setMileage(pack.getVec3());
        msg.setSpeed(pack.getVec1());
        msg.setVehicleNo(pack.getVehicleNo());

        return msg;
    }

}
