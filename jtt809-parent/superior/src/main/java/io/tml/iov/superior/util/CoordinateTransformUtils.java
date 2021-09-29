package io.tml.iov.superior.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class CoordinateTransformUtils {

    public static double x_PI = 3.14159265358979324 * 3000.0 / 180.0;

    public static double PIs = 3.1415926535897932384626;

    public static double a = 6378245.0;

    public static double ee = 0.00669342162296594323;

    // 经度转为墨卡托坐标系经度
    public static BigDecimal[] zuobiao2mokatuo(BigDecimal lng, BigDecimal lat) {

        double dlat = transformlat(lng.doubleValue() - 105.0,
                lat.doubleValue() - 35.0);
        double dlng = transformlng(lng.doubleValue() - 105.0,
                lat.doubleValue() - 35.0);
        double radlat = lat.doubleValue() / 180.0 * PIs;
        double magic = Math.sin(radlat);
        magic = 1 - ee * magic * magic;
        double sqrtmagic = Math.sqrt(magic);
        dlat = (dlat * 180.0) / ((a * (1 - ee)) / (magic * sqrtmagic) * PIs);
        dlng = (dlng * 180.0) / (a / sqrtmagic * Math.cos(radlat) * PIs);
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
        ret += (20.0 * Math.sin(6.0 * lng * PIs)
                + 20.0 * Math.sin(2.0 * lng * PIs)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lat * PIs) + 40.0 * Math.sin(lat / 3.0 * PIs))
                * 2.0 / 3.0;
        ret += (160.0 * Math.sin(lat / 12.0 * PIs)
                + 320 * Math.sin(lat * PIs / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformlng(double lng, double lat) {
        double ret = 300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat
                + 0.1 * Math.sqrt(Math.abs(lng));
        ret += (20.0 * Math.sin(6.0 * lng * PIs)
                + 20.0 * Math.sin(2.0 * lng * PIs)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(lng * PIs) + 40.0 * Math.sin(lng / 3.0 * PIs))
                * 2.0 / 3.0;
        ret += (150.0 * Math.sin(lng / 12.0 * PIs)
                + 300.0 * Math.sin(lng / 30.0 * PIs)) * 2.0 / 3.0;
        return ret;
    }

    /*
     * public String[]latLng2WebMercator (double lng, double lat) { double
     * earthRad = 6378137.0; double x = lng * Math.PI / 180 * earthRad; double a
     * = lat * Math.PI / 180; double y = earthRad / 2 * Math.log((1.0 +
     * Math.sin(a)) / (1.0 - Math.sin(a))); return new
     * String[]{big(x)+"",big(y)+""}; }
     */

    private static String big(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(9);
        // 结果未做任何处理
        return nf.format(d);
    }

    public static void main(String[] args) {
        CoordinateTransformUtils coordinateTransformUtils = new CoordinateTransformUtils();
        String lng = "113.07537";
        String lat = "27.796398";
        BigDecimal[] bigDecimals = coordinateTransformUtils.zuobiao2mokatuo(
                BigDecimal.valueOf(Double.parseDouble(lng)),
                BigDecimal.valueOf(Double.parseDouble(lat)));
        for (BigDecimal b : bigDecimals) {
            System.out.println("b = " + b);
        }
        
    }

}
