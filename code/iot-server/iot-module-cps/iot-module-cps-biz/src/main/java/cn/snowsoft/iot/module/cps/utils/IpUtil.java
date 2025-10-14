package cn.snowsoft.iot.module.cps.utils;

import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 获取本机IP 地址
 */
@Component
public class IpUtil {
     /**
      * 获取本地IP地址
      * @return 返回地址
      * @throws Exception 抛出异常
      */
     public  static String getLocalIP() throws Exception {
          if (isWindowsOS()) {
               return InetAddress.getLocalHost().getHostAddress();
          } else {
               return getLinuxLocalIp();
          }
     }

     /**
      * 获取Linux下的IP地址
      *
      * @return IP地址
      * @throws Exception 抛出异常
      */
     private static String getLinuxLocalIp() throws Exception {
          String ip = "";
          try {
               for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                    NetworkInterface intf = en.nextElement();
                    String name = intf.getName();
                    if (!name.contains("docker") && !name.contains("lo")) {
                         for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                              InetAddress inetAddress = enumIpAddr.nextElement();
                              if (!inetAddress.isLoopbackAddress()) {
                                   String ipaddress = inetAddress.getHostAddress().toString();
                                   if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                        ip = ipaddress;
                                   }
                              }
                         }
                    }
               }
          } catch (Exception ex) {
               ex.printStackTrace();
          }
          return ip;
     }

     /**
      * 判断操作系统是否是Windows
      *
      * @return 是windows还是其他
      */
     public static boolean isWindowsOS() {
          boolean isWindowsos = false;
          String osName = System.getProperty("os.name");
          if (osName.toLowerCase().indexOf("windows") > -1) {
               isWindowsos = true;
          }
          return isWindowsos;
     }



}
