package io.tml.iov.common.util;

public class Jtt809Util {
    
    /**
     * 加密
     * 
     * @param M1
     * @param IA1
     * @param IC1
     * @param key
     * @param data
     * @return
     */
    public static byte[] encrypt(int M1, int IA1, int IC1, int key, byte[] data) {
        if (data == null) {
            return null;
        }
        byte[] array = data;// 使用原对象，返回原对象
        int idx = 0;
        if (key == 0) {
            key = 1;
        }
        int mkey = M1;
        if (0 == mkey) {
            mkey = 1;
        }
        while (idx < array.length) {
            key = IA1 * (key % mkey) + IC1;
            array[idx] ^= ((key >> 20) & 0xFF);
            idx++;
        }
        return array;
    }
}
