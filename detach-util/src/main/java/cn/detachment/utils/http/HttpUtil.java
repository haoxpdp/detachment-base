package cn.detachment.utils.http;

import cn.detachment.utils.exceptions.HttpRuntimeException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haoxp
 * @date 20/9/29
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static volatile CloseableHttpClient client;

    public static Map<String, HttpMethodApi> methodInstanceMap;

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

    public static <T> T get(String url, final Map<String, Object> params, Map<String, String> header, ResponseHandler<T> responseHandler, RequestConfig config) {
        try {
            if (!CollectionUtils.isEmpty(params)) {
                List<BasicNameValuePair> collect = params.entrySet().stream()
                        .filter(e -> Objects.nonNull(e.getValue()))
                        .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
                        .collect(Collectors.toList());
                String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(collect, StandardCharsets.UTF_8));
                url = url + "?" + paramStr;

            }
            RequestBuilder requestBuilder = methodInstanceMap.get(HttpGet.METHOD_NAME)
                    .instanceByHttpMethod(url);
            return request(requestBuilder, header, responseHandler, config);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }
    }

    public static String get(String url, final Map<String, Object> params) {
        try {
            return get(url, params, null, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8), null);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }
    }

    public static String get(String url, final Map<String, Objects> params, Map<String, String> header) {
        return get(url, params, header);
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> header, RequestConfig requestConfig) {
        return post(url, params, header, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8), requestConfig);
    }

    public static String post(String url, Map<String, Object> params, Map<String, String> header) {
        return post(url, params, header, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8), null);
    }

    public static String post(String url, Map<String, Object> params) {
        return post(url, params, null, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8), null);
    }

    public static String postJSON(String url, String jsonStr, Map<String, String> header, RequestConfig config) {
        try {
            RequestBuilder requestBuilder = methodInstanceMap.get(HttpPost.METHOD_NAME).instanceByHttpMethod(url);
            StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
            requestBuilder.setEntity(entity);
            return request(requestBuilder, header, DefaultStringHandler.getInstanceByCharset(StandardCharsets.UTF_8), config);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }
    }

    public static String postFile(String url, final Map<String, String> header, Map<String, File> files,
                                  Map<String, Object> param, RequestConfig requestConfig, Charset charset) {
        try {
            RequestBuilder requestBuilder = methodInstanceMap.get(HttpPost.METHOD_NAME).instanceByHttpMethod(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            if (!CollectionUtils.isEmpty(param)) {
                param.entrySet().stream()
                        .filter(e -> StringUtils.isEmpty(String.valueOf(e.getValue())))
                        .forEach(e -> builder.addPart(e.getKey(), new StringBody(String.valueOf(e.getValue()), ContentType.TEXT_PLAIN)));
            }

            if (!CollectionUtils.isEmpty(files)) {
                files.forEach((k, v) -> builder.addPart(k, new FileBody(v)));
            }
            requestBuilder.setEntity(builder.build());
            return request(requestBuilder, header, DefaultStringHandler.getInstanceByCharset(charset), requestConfig);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }
    }

    public static String postJSON(String url, String jsonStr) {
        return postJSON(url, jsonStr, null, null);
    }

    public static <T> T post(String url, Map<String, Object> params, Map<String, String> header, ResponseHandler<T> responseHandler, RequestConfig config) {
        try {
            RequestBuilder requestBuilder = methodInstanceMap.get(HttpPost.METHOD_NAME).instanceByHttpMethod(url);
            List<NameValuePair> pairs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(params)) {
                params.entrySet().stream()
                        .filter(e -> Objects.nonNull(e.getValue()))
                        .forEach(e -> pairs.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue()))));
            }
            requestBuilder.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));
            return request(requestBuilder, header, responseHandler, config);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T request(RequestBuilder requestBuilder, Map<String, String> header,
                                ResponseHandler<T> responseHandler, RequestConfig config) {
        inflateHeader(requestBuilder, header);
        inflateConfig(requestBuilder, config);

        try {
            return getClient().execute(requestBuilder.build(), responseHandler);
        } catch (Exception e) {
            throw new HttpRuntimeException(e.getMessage(), e);
        }

    }


    public static void inflateConfig(RequestBuilder requestBuilder, RequestConfig requestConfig) {
        if (requestConfig == null) {
            requestConfig = getDefaultRequestConfig();
        }
        requestBuilder.setConfig(requestConfig);
    }

    protected static void inflateHeader(RequestBuilder requestBuilder, Map<String, String> header) {
        if (CollectionUtils.isEmpty(header)) {
            return;
        }
        header.forEach(requestBuilder::addHeader);

    }

    public interface HttpMethodApi {

        /**
         * instanceByHttpMethod
         *
         * @param url url
         * @return request builder
         * @author haoxp
         * @date 20/10/2
         */
        RequestBuilder instanceByHttpMethod(String url);
    }
}
