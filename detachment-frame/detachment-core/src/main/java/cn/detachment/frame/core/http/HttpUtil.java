package cn.detachment.frame.core.http;

import cn.detachment.frame.core.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
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
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.detachment.frame.core.constant.CharSetEnum.UTF8;
import static cn.detachment.frame.core.constant.HttpCode.*;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/1/27 0:32
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    public static RequestConfig defaultCfg;

    private static final String REDIRECT_HEADER = "location";

    static {
        defaultCfg = RequestConfig.custom()
                .setSocketTimeout(130000)
                .setConnectTimeout(100000)
                .setConnectionRequestTimeout(120000)
                .build();

    }

    //get方法

    public static String get(CloseableHttpClient client, String url, OperatorResponse operatorResponse) {
        return get(client, url, null, operatorResponse);
    }

    public static String get(CloseableHttpClient client, String url,
                             RequestConfig requestConfig, OperatorResponse operatorResponse) {
        return get(client, url, null, requestConfig, operatorResponse);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> params,
                             RequestConfig requestConfig, OperatorResponse operatorResponse) {
        return get(client, url, null, params, requestConfig, UTF8, operatorResponse);
    }

    public static String get(CloseableHttpClient client, String url) {
        return get(client, url, null, null);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> params) {
        return get(client, url, null, params, null, UTF8);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> header, final Map<String, String> params,
                             RequestConfig requestConfig, final String character) {
        return get(client, url, header, params, requestConfig, character, null);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> header, final Map<String, String> params,
                             RequestConfig requestConfig, final String character, OperatorResponse operatorResponse) {
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> list = params.entrySet().stream()
                    .map(e -> new BasicNameValuePair(e.getKey(), e.getValue())).collect(Collectors.toList());
            try {
                String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(list, Charset.forName(character)));
                url = url + "?" + paramStr;
            } catch (IOException e) {
                throw new ServiceException(e.getMessage(), e);
            }
        }
        RequestBuilder requestBuilder = RequestBuilder.get(url);
        return httpRequest(client, requestBuilder, header, requestConfig, character, operatorResponse);
    }

    //post方法

    // postJSON方法

    public static String postJSON(CloseableHttpClient client, String uri,
                                  Object json) {
        return postJSON(client, uri, json, null);
    }

    public static String postJSON(CloseableHttpClient client, String uri,
                                  Object json, RequestConfig requestConfig) {
        return postJSON(client, uri, null, json, requestConfig, UTF8);
    }

    public static String postJSON(CloseableHttpClient client, String uri,
                                  final Map<String, String> header, Object json,
                                  RequestConfig requestConfig, String charSet) {
        Assert.notNull(json, "json must not null!");
        RequestBuilder requestBuilder = RequestBuilder.post(uri);
        String jsonStr = transferJSONToString(json);
        StringEntity entity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);
        requestBuilder.setEntity(entity);
        return httpRequest(client, requestBuilder, header, requestConfig, charSet, null);
    }

    private static String transferJSONToString(Object json) {
        if (json instanceof String) {
            return (String) json;
        }
        return JSONObject.toJSONString(json);
    }

    public static String httpRequest(CloseableHttpClient client,
                                     final RequestBuilder requestBuilder, final Map<String, String> header) {
        return httpRequest(client, requestBuilder, header, null, UTF8, null);
    }

    public static String httpRequest(CloseableHttpClient client,
                                     final RequestBuilder requestBuilder, final Map<String, String> header,
                                     final String charSet) {
        return httpRequest(client, requestBuilder, header, null, charSet, null);
    }


    public static String httpRequest(CloseableHttpClient client,
                                     final RequestBuilder requestBuilder, final Map<String, String> header,
                                     RequestConfig requestConfig, final String charSet) {
        return httpRequest(client, requestBuilder, header, requestConfig, charSet, null);
    }

    /***
     * httpRequest
     * 1. 设置header
     * 2. 设置requestCfg
     * 3. buildRequest
     * 4. createResponseHandler
     *
     * @param client client
     * @param requestBuilder requestBuilder
     * @param header header
     * @param requestConfig requestConfig
     * @param charSet charSet
     * @return java.lang.String
     * @author haoxp
     * @date 20/1/27 0:43
     */
    public static String httpRequest(CloseableHttpClient client,
                                     final RequestBuilder requestBuilder, final Map<String, String> header,
                                     RequestConfig requestConfig, final String charSet,
                                     final OperatorResponse operatorResponse) {
        logger.info("uri: {}", requestBuilder.getUri());

        inflateHeader(requestBuilder, header);

        inflateConfig(requestBuilder, requestConfig);

        HttpUriRequest request = requestBuilder.build();

        logger.info("execute requset info {}:", request.getRequestLine());

        ResponseHandler<String> resHandler = response -> {
            if (operatorResponse != null) {
                return operatorResponse.operator(response);
            }
            HttpEntity entity = response.getEntity();
            String responseEntity = entity != null ? EntityUtils.toString(entity, Charset.forName(charSet)) : null;

            int status = response.getStatusLine().getStatusCode();
            if (status >= SUCCESS && status < REDIRECT) {
                return responseEntity;
            } else if (status >= REDIRECT && status < BAD_REQUEST) {
                Header[] headers = response.getAllHeaders();
                for (Header h : response.getAllHeaders()) {
                    if (h.getName().equalsIgnoreCase(REDIRECT_HEADER)) {
                        requestBuilder.setUri(h.getValue());
                        return httpRequest(client, requestBuilder, null, requestConfig, charSet, operatorResponse);
                    }
                }
                throw new HttpResponseException(status, "Unexpected response status: " + status);
            } else {

                throw new HttpResponseException(status, "Unexpected response status: " + status);
            }
        };

        try {
            return client.execute(request, resHandler);
        } catch (HttpResponseException e) {
            throw new ServiceException(500, e.getMessage(), e);
        } catch (ConnectTimeoutException e) {
            throw new ServiceException(500, "request timeout", e);
        } catch (SocketTimeoutException e) {
            throw new ServiceException(500, "response timeout", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private static void inflateHeader(RequestBuilder requestBuilder, Map<String, String> header) {
        if (!CollectionUtils.isEmpty(header)) {
            logger.info("header: {} ", header);
            header.entrySet().stream()
                    .filter(e -> StringUtils.hasText(e.getKey()))
                    .forEach(e -> requestBuilder.addHeader(e.getKey(), e.getValue()));
        }
    }

    private static void inflateConfig(RequestBuilder requestBuilder, RequestConfig requestConfig) {
        if (requestConfig == null) {
            requestConfig = defaultCfg;
        }
        requestBuilder.setConfig(requestConfig);
    }

    /**
     * OperatorResponse
     * 自定义操作httpClient返回结果
     *
     * @author haoxp
     * @date 19/10/23 11:41
     */
    public interface OperatorResponse {
        String operator(HttpResponse response) throws IOException;
    }

}
