package cn.detachment.utils.http;

import org.apache.http.config.Registry;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @date 20/9/30
 */
public class HttpManager {

    private static volatile PoolingHttpClientConnectionManager cm;

    public static PoolingHttpClientConnectionManager getConnectionManager() {
        if (cm == null) {
            synchronized (HttpManager.class) {
                if (cm == null) {
                    cm = new PoolingHttpClientConnectionManager();
                }
            }
        }
        return cm;
    }

}
