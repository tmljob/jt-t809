package io.tml.iov.inferior.client.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.aopalliance.intercept.Interceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class IpUtils implements Interceptor {

    public static String getLinuxLocalIp() {
        String ip = "";
        try {

            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    // 不含有docker和lo
                    for (Enumeration<InetAddress> enumIpAddr = intf
                            .getInetAddresses(); enumIpAddr
                                    .hasMoreElements();) {
                        InetAddress inAddress = enumIpAddr.nextElement();
                        if (!inAddress.isLoopbackAddress()) {
                            String ipaddress = inAddress.getHostAddress()
                                    .toString();
                            if (!ipaddress.contains("::")
                                    && !ipaddress.contains("0:0:")
                                    && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            log.error("IpUtils getLinuxLocalIp error.", e);
            ip = "127.0.0.1";
        }
        return ip;
    }

}