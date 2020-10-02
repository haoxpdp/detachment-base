package cn.detachment.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoxp
 * @date 20/9/29
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static volatile CloseableHttpClient client;

    public static Map<String, HttpMethodAPI> methodInstanceMap;

    static {
        methodInstanceMap = new HashMap<>();
        methodInstanceMap.put(HttpGet.METHOD_NAME, RequestBuilder::get);
        methodInstanceMap.put(HttpPost.METHOD_NAME, RequestBuilder::post);
        methodInstanceMap.put(HttpHead.METHOD_NAME, RequestBuilder::head);
        methodInstanceMap.put(HttpPut.METHOD_NAME, RequestBuilder::put);
        methodInstanceMap.put(HttpDelete.METHOD_NAME, RequestBuilder::delete);
        methodInstanceMap.put(HttpTrace.METHOD_NAME, RequestBuilder::trace);
        methodInstanceMap.put(HttpOptions.METHOD_NAME, RequestBuilder::options);
    }

    public static void setUtilHttpClient(CloseableHttpClient c) {
        client = c;

    }

    public static CloseableHttpClient getClient() throws NoSuchAlgorithmException, KeyManagementException {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null) {
                    client = HttpClientFactory.newInstance()
                            .retry(3)
                            .ignoreSSLPool(10, 8)
                            .build();
                }
            }
        }
        return client;
    }

    private static volatile RequestConfig defaultReqConfig;


    /**
     * 默认设置
     * 连接超时 3s
     * 建立连接超时 3s
     * 从池中取连接10s
     */
    public static RequestConfig getDefaultRequestConfig() {
        if (defaultReqConfig == null) {
            synchronized (HttpUtil.class) {
                if (defaultReqConfig == null) {
                    defaultReqConfig = RequestConfig.custom()
                            .setSocketTimeout(3000)
                            .setConnectionRequestTimeout(10000)
                            .setConnectTimeout(3000)
                            .build();
                }
            }
        }
        return defaultReqConfig;
    }

    public static <T> T get(String url, final Map<String, Object> params, ResponseHandler<T> responseHandler) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        RequestBuilder requestBuilder = methodInstanceMap.get(HttpGet.METHOD_NAME)
                .instanceByHttpMethod(url);
        if (!CollectionUtils.isEmpty(params)) {
            List<BasicNameValuePair> collect = params.entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
                    .collect(Collectors.toList());
            requestBuilder.setEntity(new UrlEncodedFormEntity(collect, StandardCharsets.UTF_8));
        }
        return request(requestBuilder, null, responseHandler, null);
    }

    public static String get(String url, final Map<String, Object> params) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        return get(url, params, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8));
    }

    public static <T> T request(RequestBuilder requestBuilder, Map<String, Object> header,
                                ResponseHandler<T> responseHandler, RequestConfig config) throws KeyManagementException, NoSuchAlgorithmException, IOException {
        inflateHeader(requestBuilder, header);
        inflateConfig(requestBuilder, config);

        return getClient().execute(requestBuilder.build(), responseHandler);

    }


    public static void inflateConfig(RequestBuilder requestBuilder, RequestConfig requestConfig) {
        if (requestConfig == null) {
            requestConfig = getDefaultRequestConfig();
        }
        requestBuilder.setConfig(requestConfig);
    }

    protected static void inflateHeader(RequestBuilder requestBuilder, Map<String, Object> header) {
        if (CollectionUtils.isEmpty(header)) {
            return;
        }
        header.forEach((k, v) -> requestBuilder.addHeader(k, String.valueOf(v)));

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, IOException {
        System.out.println(HttpUtil.get("https://www.baidu.com", null));;
    }


    interface HttpMethodAPI {
        RequestBuilder instanceByHttpMethod(String url);
    }
}
