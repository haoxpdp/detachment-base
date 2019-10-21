package cn.detachment.frame.core.util;

import com.alibaba.fastjson.JSONObject;
import com.haoxp.detach_base.common.exception.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/9/15 22:26
 */
public class HttpUtils {

    private static final Log logger = LogFactory.getLog(HttpUtils.class);

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static final String UTF8 = "UTF-8";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static PoolingHttpClientConnectionManager cm;
    private static CloseableHttpClient httpClient;

    static {
        try {
            cm = new PoolingHttpClientConnectionManager();
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(cm.getMaxTotal());
            cm.setValidateAfterInactivity(2000);
            HttpClientBuilder httpClientBuilder = HttpClients.custom();
            RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(130000).setConnectTimeout(100000).setConnectionRequestTimeout(120000).build();
            httpClientBuilder.setDefaultRequestConfig(defaultRequestConfig);
            httpClientBuilder.setRetryHandler(new StandardHttpRequestRetryHandler());
            httpClient = httpClientBuilder.setConnectionManager(cm).setConnectionManagerShared(true).build();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * post基础方法
     *
     * @param url
     * @param header
     * @param param
     * @param config
     * @return
     */
    public static String post(String url, Map<String, String> header, Map<String, String> param, RequestConfig config) {
        RequestBuilder requestBuilder = RequestBuilder.post(url);
        try {
            ArrayList<NameValuePair> list = new ArrayList();
            if (!CollectionUtils.isEmpty(param)) {
                param.forEach((key, value) -> {
                    list.add(new BasicNameValuePair(key, value));
                });
            }
            requestBuilder.setEntity(new UrlEncodedFormEntity(list, UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return httpRequest(requestBuilder, header, config, UTF8);
    }

    public static String post(String url) {
        return post(url, null, null, null);
    }

    public static String post(String url, Map<String, String> header, Map<String, String> param) {
        return post(url, header, param, null);
    }

    public static String post(String url, RequestConfig config) {
        return post(url, null, null, null);
    }

    /**
     * get基础方法
     *
     * @param url
     * @param header
     * @param param
     * @param config
     * @return
     */
    public static String get(String url, Map<String, String> header, Map<String, String> param, RequestConfig config, String charSet) {
        return get(url, header, param, config, charSet, null);
    }

    public static void getFile(String url, OperatorResponse operatorResponse) {
        get(url, null, null, null, "utf-8", operatorResponse);
    }

    public static void getFile(String url, Map<String, String> header, OperatorResponse operatorResponse) {
        get(url, header, null, null, "utf-8", operatorResponse);
    }

    public static String get(String url, Map<String, String> header, Map<String, String> param, RequestConfig config, String charSet, OperatorResponse operatorResponse) {
        // 如果有参数，生成并拼接在url后
        if (!CollectionUtils.isEmpty(param)) {
            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
            param.forEach((key, value) -> {
                list.add(new BasicNameValuePair(key, value));
            });

            try {
                String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(list, Charset.forName(charSet)));
                url = url + "?" + paramStr;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        RequestBuilder requestBuilder = RequestBuilder.get(url);
        return httpRequest(requestBuilder, header, config, charSet, operatorResponse);
    }

    public static String get(String url) {
        return get(url, null, null, null, UTF8);
    }

    public static String get(String url, String charSet) {
        return get(url, null, null, null, charSet);
    }

    public static String get(String url, Map<String, String> header, Map<String, String> param) {
        return get(url, header, param, null, UTF8);
    }

    public static String get(String url, Map<String, String> header, Map<String, String> param, String encode) {
        return get(url, header, param, null, encode);
    }

    public static String get(String url, RequestConfig config) {
        return get(url, null, null, config, UTF8);
    }

    /**
     * 基础post发送json数据<br/>
     * 若params是String类型，则直接发送。<br/>
     * 否则，调用fastJson的toJSONString()转化成json再发送。
     *
     * @param url
     * @param header
     * @param params
     * @param config
     * @return
     */
    public static String postJson(String url, Map<String, String> header, Object params, RequestConfig config) {
        RequestBuilder requestBuilder = RequestBuilder.post(url);
        if (params != null) {
            String jsonStr = null;
            if (params instanceof String) {
                jsonStr = (String) params;
            } else {
                jsonStr = JSONObject.toJSONString(params);
            }

            StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
            requestBuilder.setEntity(entity);
        }
        return httpRequest(requestBuilder, header, config, UTF8);
    }

    public static String postJson(String url, Object params, RequestConfig config) {
        return postJson(url, null, params, config);
    }


    public static String httpRequest(RequestBuilder requestBuilder, Map<String, String> header, RequestConfig config, String charSet) throws ServiceException {
        return httpRequest(requestBuilder, header, config, charSet, null);
    }

    public static String httpRequest(RequestBuilder requestBuilder, Map<String, String> header, RequestConfig config, String charSet, OperatorResponse operatorResponse) throws ServiceException {

        logger.info("uri:" + requestBuilder.getUri());
        logger.info("header:" + header);
        if (!CollectionUtils.isEmpty(header)) {
            header.forEach((key, value) -> {
                if (StringUtils.hasText(key)) {
                    requestBuilder.addHeader(key, value);
                }
            });
        }

        if (config != null) {
            requestBuilder.setConfig(config);
        }
        HttpUriRequest request = requestBuilder.build();
        logger.info("Executing request" + request.getRequestLine());
        Header[] allHeaders = request.getAllHeaders();
        for (Header h : allHeaders) {
            logger.info("exe   " + h.getName() + ":" + h.getValue());
        }

        ResponseHandler<String> responseHandler = response -> {

            if (operatorResponse != null) {
                return operatorResponse.operator(response);
            } else {
                HttpEntity entity = response.getEntity();
                String responseEntity = entity != null ? EntityUtils.toString(entity, Charset.forName(charSet)) : null;

                logger.info(String.format("response <<<%s>>>", responseEntity));

                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    return responseEntity;
                } else {
                    throw new HttpResponseException(status, "Unexpected response status: " + status);
                }
            }

        };

        try {
            String responseBody = httpClient.execute(request, responseHandler);
            return responseBody;
        } catch (HttpResponseException e) {
            throw new ServiceException(500, e.getMessage(), e);
        } catch (ConnectTimeoutException e) {
            throw new ServiceException(500, "请求超时", e);
        } catch (SocketTimeoutException e) {
            throw new ServiceException(500, "响应超时", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static CloseableHttpClient getClient() {
        return httpClient;
    }

    public interface OperatorResponse {
        String operator(HttpResponse response) throws IOException;
    }
}

