package cn.detachment.frame.core.http;

import cn.detachment.frame.core.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ConnectTimeoutException;
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
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.*;
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
                .setSocketTimeout(1300000)
                .setConnectTimeout(1000000)
                .setConnectionRequestTimeout(1200000)
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

    public static String get(CloseableHttpClient client, String url, final Map<String, Object> params,
                             RequestConfig requestConfig, OperatorResponse operatorResponse) {
        return get(client, url, null, params, requestConfig, UTF8, operatorResponse);
    }

    public static String get(CloseableHttpClient client, String url) {
        return get(client, url, null, null);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, Object> params) {
        return get(client, url, null, params, null, UTF8);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> header, final Map<String, Object> params,
                             RequestConfig requestConfig, final String character) {
        return get(client, url, header, params, requestConfig, character, null);
    }

    public static String get(CloseableHttpClient client, String url, final Map<String, String> header, final Map<String, Object> params,
                             RequestConfig requestConfig, final String character, OperatorResponse operatorResponse) {
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> list = params.entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue()))).collect(Collectors.toList());
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

    public static String post(final CloseableHttpClient client, final String url,
                              final Map<String, Object> params) {
        return post(client, url, params, UTF8);
    }

    public static String post(final CloseableHttpClient client, final String url, final Map<String, Object> params, final String charSet) {
        return post(client, url, null, params, null, charSet);
    }

    public static String post(final CloseableHttpClient client, final String url, final Map<String, String> headers,
                              final Map<String, Object> params, final RequestConfig requestConfig, String charSet) {
        try {
            RequestBuilder requestBuilder = RequestBuilder.post(url);
            List<NameValuePair> pairs = new ArrayList<>();
            if (!CollectionUtils.isEmpty(params)) {
                params.entrySet().stream()
                        .filter(e -> Objects.nonNull(e.getValue()))
                        .forEach(e -> pairs.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue()))));
            }
            requestBuilder.setEntity(new UrlEncodedFormEntity(pairs, charSet));
            return httpRequest(client, requestBuilder, headers, requestConfig, charSet);
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException(e.getMessage(), e);
        }

    }

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

    public static String postFile(CloseableHttpClient client, String uri, Map<String, Objects> param, Map<String, File> files) {
        return postFile(client, uri, null, param, files, defaultCfg);
    }

    public static String postFile(CloseableHttpClient client, String uri, final Map<String, String> header,
                                  Map<String, Objects> param, Map<String, File> files, RequestConfig requestConfig) {
        RequestBuilder requestBuilder = RequestBuilder.post(uri);
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
        return httpRequest(client, requestBuilder, header, requestConfig, "utf-8");
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
        HttpEntity entity = requestBuilder.getEntity();
        requestBuilder.addHeader(entity.getContentEncoding());
        requestBuilder.addHeader(entity.getContentType());
//        requestBuilder.addHeader("Content-Length", String.valueOf(entity.getContentLength()));
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


    public static void main(String[] args) {
        String url = "https://d.pcs.baidu.com/rest/2.0/pcs/superfile2?";
        Map<String, Object> param = new HashMap<>();
        param.put("path", "/1.pdf");
        param.put("type", "tmpfile");
        param.put("method", "upload");
        param.put("BDUSS", "pansec_DCb740ccc5511e5e8fedcff06b081203-%2B7lIHst03cf%2FUARZks3PLx4t39UySTMct0Cd3D05X%2FTjh4AHf3CgpXWeH%2BnlLRmqxE6aZB3QtJGEU1SG2u0W9ri4fFoulRdt4LZgqa6%2B79pF24yDLUym9prjxxHAfhI770xeHH56NdHpUlqHWrisj%2B2MH149SzbywEL03ngt9DofuLrZRt8ewGmkAMoZiT9FAC6bVySlwdLq4sbcHDvIZysPLZz1B5nGyIp3lOMT9Mv8IlT40X9XX0z2lSzEi0U1t1ux%2F3gFQ0cFwbjtvHIvhA%3D%3D");
        param.put("app_id", 250528);
        param.put("uploadid", "P1-MTAuOTIuMTQyLjU0OjE1ODcwNTIzNDI6MjQ4MTUwOTMyMjgwMTc1NDU4MA==");
        for (int i = 0; i < 3; i++) {
            param.put("partseq", i);
            String tmpUrl = url;
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                tmpUrl = tmpUrl + entry.getKey() + "=" + entry.getValue() + "&";
            }
            tmpUrl = tmpUrl.substring(0, tmpUrl.length() - 1);
            Map<String, File> file = new HashMap<>();
            file.put("file", new File("H:\\Desktop\\tmp_UNIX网络编程卷2：进程间通信（第2版）\\" + i));
            String res = HttpUtil.postFile(HttpClientFactory.newInstance().ssl().build(), tmpUrl, null, file);
            logger.info("res {}", res);
        }
    }
}