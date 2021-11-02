package io.tml.iov.common.util;

public class Jtt809Util {
    
    private Jtt809Util() {
    }
    
    /**
     * 加密
     * 
     * @param m1
     * @param ia1
     * @param ic1
     * @param key
     * @param data
     * @return
     */
    public static byte[] encrypt(int m1, int ia1, int ic1, int key, byte[] data) {
        if (data == null) {
            return new byte[0];
        }
        byte[] array = data;// 使用原对象，返回原对象
        int idx = 0;
        if (key == 0) {
            key = 1;
        }
        int mkey = m1;
        if (0 == mkey) {
            mkey = 1;
        }
        while (idx < array.length) {
            key = ia1 * (key % mkey) + ic1;
            array[idx] ^= ((key >> 20) & 0xFF);
            idx++;
        }
        return array;
    }
}
