package io.tml.iov.common.util;

import java.io.ByteArrayOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;


public class CommonUtils {
    /** 当前线程报文的缓存，以备出错时，打印或记录，方便后期定位 */
    public static Map<String, String> PACKET_CACHE = new HashMap<>();

    public static ByteBuf getByteBuf(byte[] bytes) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        return byteBuf;
    }

    public static byte[] getByteArray(ByteBuf byteBuf) {
        int num = byteBuf.readableBytes();
        byte[] originalPacket = new byte[num];
        byteBuf.readBytes(originalPacket);

        return originalPacket;
    }

    /**
     * byte数组拼接
     * 
     * @param first
     * @param back
     * @return
     */
    public static byte[] append(byte[] first, byte[] back) {
        if (null == first || null == back) {
            return null;
        }
        int length = first.length + back.length;
        byte[] res = new byte[length];
        System.arraycopy(first, 0, res, 0, first.length);
        System.arraycopy(back, 0, res, first.length, back.length);
        return res;

    }

    /**
     * short转换为byte[]
     * 
     * @param number
     * @return byte[]
     */
    public static byte[] short2Bytes(short number) {
        byte[] b = new byte[2];
        b[0] = (byte) (number >> 8);
        b[1] = (byte) (number & 0xff);
        return b;
    }

    /**
     * 大转小时，会截取低位，舍弃高位。 int 32位 byte 8位 当int转为1个byte时，会截取int最低的8位 int to bytes
     * 
     * @param n
     * @return
     */
    public static byte[] int2bytes(int n) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (n >> (24 - i * 8));
        }
        return b;
    }

    /**
     * byte数组转时间字符串 格式 yyMMddHHmmss
     * 
     * @return
     */
    public static String bytes2timeStr(byte[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            int timeUnit = byte2UnsignedInt(array[i]);
            if (timeUnit < 10) {
                stringBuilder.append(0);
            }
            stringBuilder.append(timeUnit);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()).substring(0, 2)
                + stringBuilder.toString();
    }

    /**
     * byte转无符号整数
     * 
     * @param value
     * @return
     */
    public static int byte2UnsignedInt(byte value) {
        return Byte.toUnsignedInt(value);
    }

    /**
     *
     *
     * @描述 将一个long转换成8位的byte[]
     * @param num long值
     * @return 长度是8的byte[]
     * @throws Exception
     */
    public static byte[] longToBytes(long num) {
        byte[] b = new byte[8];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (num >>> (56 - i * 8));
        }
        return b;
    }

    /**
     *
     *
     * @描述 将一个数组转换成一个long值
     * @param b 长度是8的byte[]
     * @return long值
     * @throws Exception
     */
    public static long bytesToLong(byte[] b) {
        int mask = 0xff;
        long temp = 0;
        long res = 0;
        for (int i = 0; i < 8; i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }

    /**
     * 将一个byte数组转换成二进制字符串
     * 
     * @param bytes
     * @return 二进制字符串
     */
    public static String bytes2bitStr(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(byte2bitStr(b));
        }
        return stringBuilder.toString();
    }

    /**
     * 将一个byte转换成二进制字符串
     * 
     * @param b
     * @return 二进制字符串
     */
    public static String byte2bitStr(byte b) {
        return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
                + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
                + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
                + (byte) ((b >> 1) & 0x1) + (byte) ((b >> 0) & 0x1);
    }

    /**
     * byte[]转换为short
     * 
     * @param bytes
     * @return short
     */
    public static short bytes2Short(byte[] bytes) {
        short z = (short) ((bytes[0] << 8) | (bytes[1] & 0xFF));
        return z;
    }

    /**
     * byte to int
     *
     * @param data
     * @return
     */
    public static int bytes2int(byte[] data) {
        int mask = 0xff;
        int temp = 0;
        int n = 0;
        for (int i = 0; i < data.length; i++) {
            n <<= 8;
            temp = data[i] & mask;
            n |= temp;
        }
        return n;
    }
    
    public static byte[] getBytesWithLengthAfter(int length, byte[] pwdByte) {
        byte[] lengthByte = new byte[length];
        for (int i = 0; i < pwdByte.length; i++) {
            lengthByte[i] = pwdByte[i];
        }
        for (int i = 0; i < (length - pwdByte.length); i++) {
            lengthByte[pwdByte.length + i] = 0x00;
        }
        return lengthByte;
    }
    
    /**
     * 16进制字符串转换成byte数组
     * byte[]
     *
     * @param hex
     * @return 2016年10月12日 by fox_mt
     */
    public static byte[] hexStringToByte(String hex) {
        hex = hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    
    
    public static byte[] doEscape4Receive(byte[] bs, int start, int end) throws Exception {
        if (start < 0 || end > bs.length){
            throw new ArrayIndexOutOfBoundsException("doEscape4Receive error : index out of bounds(start=" + start
                    + ",end=" + end + ",bytes length=" + bs.length + ")");
        }
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            for (int i = start; i < end ; i++) {
                if (bs[i] == 0x5B) {
                    baos.write(0x5A);
                    baos.write(0x01);
                } else if (bs[i] == 0x5A) {
                    baos.write(0x5A);
                    baos.write(0x02);
                } else if(bs[i] == 0x5D){
                    baos.write(0x5E);
                    baos.write(0x01);
                }else if(bs[i] == 0x5E){
                    baos.write(0x5E);
                    baos.write(0x02);
                }else{
                    baos.write(bs[i]);
                }
            }
            return baos.toByteArray();
        } catch (Exception e) {
            throw e;
        } finally {
            if (baos != null) {
                baos.close();
                baos = null;
            }
        }
    }
    
    public static int formatLonLat(Double needFormat) {
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(6);
        numFormat.setGroupingUsed(false);
        String fristFromat = numFormat.format(needFormat);
        Double formatedDouble = Double.parseDouble(fristFromat);
        numFormat.setMaximumFractionDigits(0);
        String formatedValue = numFormat.format(formatedDouble * 1000000);
        return Integer.parseInt(formatedValue);
    }

}
