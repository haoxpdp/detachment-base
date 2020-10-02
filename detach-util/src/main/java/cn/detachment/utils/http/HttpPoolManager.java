package cn.detachment.utils.http;

import cn.detachment.utils.ssl.SslUtil;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author haoxp
 * @date 20/9/30
 */
public class HttpPoolManager {



    private static volatile PoolingHttpClientConnectionManager DEFAULT_SIMPLE_CONNECTION_POOL;

    public static PoolingHttpClientConnectionManager getDefaultSimpleConnectionPool() {
        if (DEFAULT_SIMPLE_CONNECTION_POOL == null) {
            synchronized (HttpPoolManager.class) {
                if (DEFAULT_SIMPLE_CONNECTION_POOL == null) {
                    DEFAULT_SIMPLE_CONNECTION_POOL = new PoolingHttpClientConnectionManager();
                }
            }
        }
        return DEFAULT_SIMPLE_CONNECTION_POOL;
    }


    /**
     * 绕过ssl验证的http
     */
    private static volatile PoolingHttpClientConnectionManager DEFAULT_SKIP_SSL_CONNECTION_POOL;

    public static PoolingHttpClientConnectionManager getDefaultSkipSslConnectionPool() throws NoSuchAlgorithmException, KeyManagementException {
        if (DEFAULT_SKIP_SSL_CONNECTION_POOL == null) {
            synchronized (HttpPoolManager.class) {
                if (DEFAULT_SKIP_SSL_CONNECTION_POOL == null) {
                    DEFAULT_SKIP_SSL_CONNECTION_POOL = new PoolingHttpClientConnectionManager(ignoreSSLRegistry());

                }
            }
        }
        return DEFAULT_SKIP_SSL_CONNECTION_POOL;
    }

    private static Registry<ConnectionSocketFactory> ignoreSSLRegistry() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SslUtil.ignoreSSLVerify();

        return RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();
    }



}
