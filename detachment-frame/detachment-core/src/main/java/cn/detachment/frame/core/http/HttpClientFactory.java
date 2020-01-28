package cn.detachment.frame.core.http;

import cn.detachment.frame.core.constant.SSLVersion;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * httpClientCreator
 * <p>
 * httpClient
 *
 * @author haoxp
 * @version v1.0
 * @date 20/1/27 0:32
 */
public class HttpClientFactory extends HttpClientBuilder {

    private static Logger logger = LoggerFactory.getLogger(HttpClientFactory.class);

    private HttpClientFactory() {
    }

    public static HttpClientFactory newInstance() {
        return new HttpClientFactory();
    }

    /**
     * 是否设置线程池
     */
    private boolean isPool = false;

    public HttpClientFactory retry(final int retryTimes) {
        return retry(retryTimes, true);
    }

    /**
     * retry
     *
     * @param retryTimes           尝试次数
     * @param retryWhenInterrupted interruptedException
     * @return cn.detachment.frame.core.http.HttpClientFactory
     * @author haoxp
     * @date 20/1/28 14:47
     */
    public HttpClientFactory retry(final int retryTimes, final boolean retryWhenInterrupted) {
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
                return retryWhenInterrupted;
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


    /**
     * pool
     *
     * @param maxTotal           最大链接数
     * @param defaultMaxPerRoute 每隔路由默认链接数
     * @return cn.detachment.frame.core.http.HttpPool
     * @author haoxp
     * @date 20/1/28 11:30
     */
    public HttpClientFactory pool(int maxTotal, int defaultMaxPerRoute) {
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLUtil().getSSLConnectionSocketFactory(SSLVersion.SSLv3))
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(defaultMaxPerRoute);
        isPool = true;
        return (HttpClientFactory) setConnectionManager(cm);
    }

    public HttpClientFactory proxy(String hostIp, int port) {
        HttpHost proxy = new HttpHost(hostIp, port, "http");
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        return (HttpClientFactory) this.setRoutePlanner(routePlanner);
    }

    public HttpClientFactory ssl() {
        return (HttpClientFactory) this.setSSLSocketFactory(new SSLUtil().getSSLConnectionSocketFactory(SSLVersion.SSLv3));
    }
}
