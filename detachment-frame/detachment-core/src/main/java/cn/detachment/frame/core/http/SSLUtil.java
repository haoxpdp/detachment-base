package cn.detachment.frame.core.http;


import cn.detachment.frame.core.constant.SSLVersion;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

/**
 * ssl util
 *
 * @author haoxp
 * @version v1.0
 * @date 20/1/28 16:59
 * @see <a href='https://blog.csdn.net/anningzhu/article/details/77484212'>ssl server/client</a>
 */
public class SSLUtil {

    private static Logger logger = LoggerFactory.getLogger(SSLUtil.class);


    private static final SSLHandler sslHandler = new SSLHandler();

    public synchronized SSLSocketFactory getSSLFactory(SSLVersion sslVersion) {
        try {
            SSLContext sc = getSSLContext(sslVersion);
            sc.init(null, new TrustManager[]{sslHandler}, new SecureRandom());
            return sc.getSocketFactory();
        } catch (KeyManagementException e) {
            throw new SSLFCreateException(e);
        }
    }

    public SSLConnectionSocketFactory getSSLConnectionSocketFactory(SSLVersion sslVersion) {
        try {
            SSLContext sc = getSSLContext(sslVersion);
            sc.init(null, new TrustManager[]{sslHandler}, new SecureRandom());
            return new SSLConnectionSocketFactory(sc, sslHandler);
        } catch (KeyManagementException e) {
            throw new SSLFCreateException(e);
        }
    }

    private static SSLContext getSSLContext(SSLVersion sslVersion) throws SSLFCreateException {
        try {
            return SSLContext.getInstance(sslVersion.getName());
        } catch (NoSuchAlgorithmException e) {
            throw new SSLFCreateException(e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        X509TrustManager x509m = new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        };
        // 获取一个SSLContext实例
        SSLContext s = SSLContext.getInstance(SSLVersion.SSLv3.getName());
        // 初始化SSLContext实例
        s.init(null, new TrustManager[]{x509m},
                new java.security.SecureRandom());
        // 打印这个SSLContext实例使用的协议
        System.out.println("缺省安全套接字使用的协议: " + s.getProtocol());
        // 获取SSLContext实例相关的SSLEngine
        SSLEngine e = s.createSSLEngine();
        System.out
                .println("支持的协议: " + Arrays.asList(e.getSupportedProtocols()));
        System.out.println("启用的协议: " + Arrays.asList(e.getEnabledProtocols()));
        System.out.println("支持的加密套件: "
                + Arrays.asList(e.getSupportedCipherSuites()));
        System.out.println("启用的加密套件: "
                + Arrays.asList(e.getEnabledCipherSuites()));
    }

}
