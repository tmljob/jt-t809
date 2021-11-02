package io.tml.iov.inferior.client.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.aopalliance.intercept.Interceptor;

import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class IpUtils implements Interceptor {
    
    private IpUtils() {
    }

    public static String getIpAddress() {
        try {
          Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
          InetAddress ip = null;
          while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface =  allNetInterfaces.nextElement();
            if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
               log.info("skip......");
            } else {
              Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
              while (addresses.hasMoreElements()) {
                ip = addresses.nextElement();
                if (ip instanceof Inet4Address) {
                  return ip.getHostAddress();
                }
              }
            }
          }
        } catch (Exception e) {
            log.error("getIpAddress error",e);
        }
        return "";
      }
 


}
