package cn.detachment.example.core.http;

import cn.detachment.frame.core.http.HttpClientFactory;
import cn.detachment.frame.core.http.HttpUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/1/28 18:38
 */
public class HttpExample {

    private static Logger logger = LoggerFactory.getLogger(HttpExample.class);

    public static void main(String[] args) {
        Map<Integer, String> map = new HashMap<>();
        CloseableHttpClient client = HttpClientFactory.newInstance().pool(10, 10).build();
        CloseableHttpClient c = HttpClientFactory.newInstance().ssl().build();

        String url = "https://blog.csdn.net/anningzhu/article/details/77484212";

        for (int j = 0; j <= 10; j++) {
            int testTimes = 100;
            long s = System.currentTimeMillis();

            for (int i = 0; i < testTimes; i++) {
                HttpUtil.get(client, url);
            }
            long d = System.currentTimeMillis();


            for (int i = 0; i < testTimes; i++) {
                HttpUtil.get(c, url);
            }
            long e = System.currentTimeMillis();
            map.put(j, String.format("%d %d", d - s, e - d));
        }
        logger.info("{}", map);
    }

}
