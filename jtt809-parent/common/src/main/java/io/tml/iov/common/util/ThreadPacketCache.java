package io.tml.iov.common.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadPacketCache {
    
    private ThreadPacketCache() {
    }

    /** 当前线程报文的缓存，以备出错时，打印或记录，方便后期定位 */
    private static Map<String, String> cache = new HashMap<>();

    public static String get(String threadName) {
        return cache.get(threadName);
    }

    public static void put(String threadName, String packet) {
        cache.put(threadName, packet);
    }
}
