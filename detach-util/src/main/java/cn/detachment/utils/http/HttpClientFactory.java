package cn.detachment.utils.http;

import cn.detachment.utils.ssl.SslUtil;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author haoxp
 * @date 20/9/29
 */
public class HttpClientFactory extends HttpClientBuilder {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

    private HttpClientFactory() {

    }

    public HttpClientFactory newInstance() {
        return new HttpClientFactory();
    }

    private volatile boolean isPool = false;

    public HttpClientFactory retry(final int retryTimes, final boolean retryWithInterrupted) {
        HttpRequestRetryHandler requestRetryHandler = (e, exceptionCnt, context) -> {
            if (exceptionCnt > retryTimes) {
                return false;
            }
            // 服务器丢掉链接
            if (e instanceof NoHttpResponseException) {
                return true;
            }
            // ssl握手不重试
            if (e instanceof SSLHandshakeException) {
                return false;
            }
            if (e instanceof UnknownHostException) {
                return true;
            }
            if (e instanceof InterruptedIOException) {
                return retryWithInterrupted;
            }
            // ssl 异常不重试
            if (e instanceof SSLException) {
                return false;
            }

            HttpClientContext clientContext = HttpClientContext.adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 请求是幂等
            return !(request instanceof HttpEntityEnclosingRequest);
        };
        this.setRetryHandler(requestRetryHandler);
        return this;
    }

    public HttpClientFactory retry(int times) {
        return retry(times, false);
    }

    /**
     * pool
     *
     * @param maxTotal           maxTotal 最大链接数
     * @param defaultMaxPerRoute defaultMaxPerRoute 每条路由最大连接数
     * @author haoxp
     * @date 20/10/2
     */
    public HttpClientFactory pool(int maxTotal, int defaultMaxPerRoute) {
        PoolingHttpClientConnectionManager cm = HttpPoolManager.getDefaultSimpleConnectionPool();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return (HttpClientFactory) setConnectionManager(cm);

    }

    public HttpClientFactory ignoreSSLPool(int maxTotal, int defaultMaxPerRoute) throws KeyManagementException, NoSuchAlgorithmException {
        PoolingHttpClientConnectionManager cm = HttpPoolManager.getDefaultSkipSslConnectionPool();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        return (HttpClientFactory) setConnectionManager(cm);
    }

    public HttpClientFactory ignoreSSL() throws KeyManagementException, NoSuchAlgorithmException {
        return (HttpClientFactory) this.setSSLSocketFactory(new SSLConnectionSocketFactory(SslUtil.ignoreSSLVerify()));
    }

}
