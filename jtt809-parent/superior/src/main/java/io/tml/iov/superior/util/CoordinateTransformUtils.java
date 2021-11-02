package io.tml.iov.superior.util;

import java.math.BigDecimal;

public class CoordinateTransformUtils {
    
    private CoordinateTransformUtils() {
    }


    private static final double PI_S = 3.1415926535897932384626;

    private static final double A = 6378245.0;

    private static final double EE = 0.00669342162296594323;

    // 经度转为墨卡托坐标系经度
    public static BigDecimal[] zuobiao2mokatuo(BigDecimal lng, BigDecimal lat) {

        double dlat = transformlat(lng.doubleValue() - 105.0,
                lat.doubleValue() - 35.0);
        double dlng = transformlng(lng.doubleValue() - 105.0,
                lat.doubleValue() - 35.0);
        double radlat = lat.doubleValue() / 180.0 * PI_S;
        double magic = Math.sin(radlat);
        magic = 1 - EE * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((A * (1 - EE)) / (magic * sqrtmagic) * PI_S);
        dlng = (dlng * 180.0) / (A / sqrtmagic * Math.cos(radlat) * PI_S);
        double mglat = lat.doubleValue() + dlat;
        double mglng = lng.doubleValue() + dlng;
        double earthRad = 6378137.0;
        double x = mglng * Math.PI / 180 * earthRad;
        double a = mglat * Math.PI / 180;
        double y = earthRad / 2
                * Math.log((1.0 + Math.sin(a)) / (1.0 - Math.sin(a)));

        return new BigDecimal[] { BigDecimal.valueOf(x),
                BigDecimal.valueOf(y) };

    }

    public static double transformlat(double lng, double lat) {
        double ret = -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat
                + 0.1 * lng * lat + 0.2 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI_S)
                + 20.0 * Math.sin(2.0 * lng * PI_S)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PI_S) + 40.0 * Math.sin(lat / 3.0 * PI_S))
                * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PI_S)
                + 320 * Math.sin(lat * PI_S / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat
                + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PI_S)
                + 20.0 * Math.sin(2.0 * lng * PI_S)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PI_S) + 40.0 * Math.sin(lng / 3.0 * PI_S))
                * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PI_S)
                + 300.0 * Math.sin(lng / 30.0 * PI_S)) * 2.0 / 3.0;
        return ret;
    }

}
