package cn.detachment.utils.http;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author haoxp
 * @date 20/9/29
 */
public class HttpUtil {

    private static volatile CloseableHttpClient client;

    public static CloseableHttpClient getClient() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = HttpClientBuilder
                            .create()
                            .build();
                }
            }
        }
        return client;
    }

    public String get(String url) {

        return "";
    }


}
