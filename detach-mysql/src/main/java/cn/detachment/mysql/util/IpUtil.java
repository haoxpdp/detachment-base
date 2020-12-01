package cn.detachment.mysql.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * IpUtil
 *
 * @author haoxp
 * @date 19/10/20 0:51
 */
public class IpUtil {

    private static volatile InetAddress LOCAL_ADDRESS = null;

    private static final Log logger = LogFactory.getLog(IpUtil.class);


    private static InetAddress getValidAddressByInetAddress() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (!localAddress.isLoopbackAddress() && !localAddress.isLinkLocalAddress()
                    && localAddress.isSiteLocalAddress()) {
                return localAddress;
            }
        } catch (Throwable e) {
            logger.error("Failed to retrieving ip address, " + e.getMessage(), e);
        }
        return localAddress;
    }


    private static InetAddress getFirstValidAddress() throws UnknownHostException {
        InetAddress localAddress = getValidAddressByInetAddress();
        if (localAddress == null) {
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                if (interfaces != null) {
                    while (interfaces.hasMoreElements()) {
                        try {
                            NetworkInterface network = interfaces.nextElement();
                            Enumeration<InetAddress> addresses = network.getInetAddresses();
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (!address.isLoopbackAddress()
                                            && !address.isLinkLocalAddress()
                                            && address.isSiteLocalAddress()) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    logger.error(
                                            "Failed to retrieving ip address, " + e.getMessage(), e);
                                }
                            }
                        } catch (Throwable e) {
                            logger.error("Failed to retrieving ip address, " + e.getMessage(), e);
                        }
                    }
                }
            } catch (Throwable e) {
                logger.error("Failed to retrieving ip address, " + e.getMessage(), e);
            }
        }

        if (localAddress == null) {
            logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
            localAddress = InetAddress.getByName("127.0.0.1");
        }
        return localAddress;
    }

    /**
     * get address
     */
    private static InetAddress getAddress() throws UnknownHostException {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        InetAddress localAddress = getFirstValidAddress();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    /**
     * get ip
     */
    public static String getIp() {
        InetAddress address = null;
        try {
            address = getAddress();
        } catch (UnknownHostException e) {
            logger.error("获取ip失败！");
        }
        if (address == null) {
            return null;
        }
        return address.getHostAddress();
    }

    /**
     * get ip:port
     *
     * @param port port
     */
    public static String getIpPort(int port) {
        String ip = getIp();
        if (ip == null) {
            return null;
        }
        return ip.concat(":").concat(String.valueOf(port));
    }

}
