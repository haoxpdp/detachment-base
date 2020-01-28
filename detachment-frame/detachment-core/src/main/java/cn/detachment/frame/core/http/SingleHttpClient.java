package cn.detachment.frame.core.http;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * 单例httpClient
 *
 * @author haoxp
 * @version v1.0
 * @date 20/1/27 12:01
 */
public class SingleHttpClient {

    private CloseableHttpClient httpclient;

    private RequestConfig requestConfig;

    private static SingleHttpClient singleton;

    /**
     * 获取实例，并初始化
     */
    public static SingleHttpClient getInstance() {

        if (null == singleton) {
            synchronized (SingleHttpClient.class) {
                if (null == singleton) {
                    singleton = new SingleHttpClient();
                    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
                    cm.setMaxTotal(1000);
                    singleton.httpclient = HttpClients.custom().setConnectionManager(cm)
                            .disableAutomaticRetries().build();
                    singleton.requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(10000).setSocketTimeout(10000)
                            .setConnectTimeout(10000).build();
                    return singleton;
                } else {
                    return singleton;
                }
            }
        } else {
            return singleton;
        }
    }

}
