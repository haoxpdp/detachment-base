package cn.detach.api.support;

import cn.detach.api.constant.HttpMethod;
import cn.detach.api.exception.HttpExecuteException;
import cn.detach.api.exception.UrlBuildException;
import cn.detach.api.http.RemoteRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoxp
 * @date 20/7/27
 */
@SuppressWarnings("unused")
public class DefaultHttpApiSupport implements HttpUtilApi {

    @Override
    public String get(String url) {
        return HttpUtil.get(url);
    }

    @Override
    public String post(String url, String body) {
        return HttpUtil.post(url, body);
    }

    @Override
    public String post(String url, String body, int timeout) {
        return HttpUtil.post(url, body, timeout);
    }

    @Override
    public String post(String url, Map<String, Object> param) {
        return HttpUtil.post(url, param);
    }

    @Override
    public String post(String url, Map<String, Object> param, int timeout) {
        return HttpUtil.post(url, param, timeout);
    }

    static Map<HttpMethod, HttpMethodInstance> methodInstanceMap;

    public static final String URL_QUERY_CONTACT = "?";

    public static RequestConfig defaultCfg;

    public static final int SUCCESS = 200;
    public static final int REDIRECT = 300;

    public static final int BAD_REQUEST = 400;

    private static final String REDIRECT_HEADER = "location";

    static {
        defaultCfg = RequestConfig.custom()
                .setSocketTimeout(1200000)
                .setConnectTimeout(1200000)
                .setConnectionRequestTimeout(1200000)
                .build();
    }

    static {
        methodInstanceMap = new HashMap<>();
        methodInstanceMap.put(HttpMethod.GET, RequestBuilder::get);
        methodInstanceMap.put(HttpMethod.POST, RequestBuilder::post);
        methodInstanceMap.put(HttpMethod.PUT, RequestBuilder::put);
        methodInstanceMap.put(HttpMethod.DELETE, RequestBuilder::delete);
        methodInstanceMap.put(HttpMethod.OPTIONS, RequestBuilder::options);
        methodInstanceMap.put(HttpMethod.TRACE, RequestBuilder::trace);
        methodInstanceMap.put(HttpMethod.HEAD, RequestBuilder::head);


    }


    public String requestExecute(RemoteRequest remoteRequest) {
        return "";
    }

    public String parserRemoteRequest(RemoteRequest remoteRequest) {

        if (remoteRequest.getHttpMethod() == null) {
            remoteRequest.setHttpMethod(HttpMethod.GET);
        }

        RequestBuilder requestBuilder = methodInstanceMap.get(remoteRequest.getHttpMethod())
                .instanceByHttpMethod(prepareUrl(remoteRequest));


        if (!CollectionUtils.isEmpty(remoteRequest.getHeader())) {
            remoteRequest.getHeader().forEach(requestBuilder::addHeader);
        }

        if (!CollectionUtils.isEmpty(remoteRequest.getFormData())) {
            List<NameValuePair> pairs = remoteRequest.getFormData().entrySet()
                    .stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
                    .collect(Collectors.toList());
            requestBuilder.setEntity(new UrlEncodedFormEntity(pairs, Charset.forName(remoteRequest.getEncode())));
        }

        if (remoteRequest.getRequestBody() != null) {
            Object jsonBody = remoteRequest.getRequestBody();
            String json;
            if (jsonBody instanceof String) {
                json = (String) jsonBody;
            } else if (jsonBody instanceof JSONObject) {
                json = ((JSONObject) jsonBody).toJSONString();
            } else {
                json = JSONObject.toJSONString(remoteRequest.getRequestBody());
            }
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            requestBuilder.setEntity(entity);
        }

        ResponseHandler<String> resHandler = response -> {

            HttpEntity entity = response.getEntity();
            String responseEntity = entity != null ? EntityUtils.toString(entity, Charset.forName(remoteRequest.getResponseEncode())) : null;

            int status = response.getStatusLine().getStatusCode();
            if (status >= SUCCESS && status < REDIRECT) {
                return responseEntity;
            } else if (status >= REDIRECT && status < BAD_REQUEST) {
                throw new HttpResponseException(status, "Unexpected response status: " + status);
            } else {

                throw new HttpResponseException(status, "Unexpected response status: " + status);
            }
        };
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return httpClient.execute(requestBuilder.build(), resHandler);
        } catch (HttpResponseException e) {
            throw new HttpExecuteException(e.getMessage(), e);
        } catch (ConnectTimeoutException e) {
            throw new HttpExecuteException("request timeout", e);
        } catch (SocketTimeoutException e) {
            throw new HttpExecuteException("response timeout", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String prepareUrl(RemoteRequest remoteRequest) {
        String url = remoteRequest.getUrl();
        if (!CollectionUtils.isEmpty(remoteRequest.getUrlParams())) {
            List<NameValuePair> list = remoteRequest.getUrlParams().entrySet().stream()
                    .filter(e -> e.getValue() != null)
                    .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
                    .collect(Collectors.toList());

            if (!url.contains(URL_QUERY_CONTACT)) {
                url += URL_QUERY_CONTACT;
            }
            try {
                String param = EntityUtils.toString(new UrlEncodedFormEntity(list, Charset.forName(remoteRequest.getEncode())));
                url += param;
            } catch (IOException e) {
                throw new UrlBuildException(e, "add url param error!");
            }

        }
        return url;
    }


    interface HttpMethodInstance {
        /**
         * instanceByHttpMethod
         *
         * @param url request url
         * @return org.apache.http.client.methods.HttpUriRequest
         * @author haoxp
         * @date 20/8/3 18:07
         */
        RequestBuilder instanceByHttpMethod(String url);
    }

}
