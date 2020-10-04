package cn.detach.api.support;

import cn.detach.api.constant.HttpMethod;
import cn.detach.api.http.RemoteRequest;
import cn.detachment.utils.http.DefaultStringHandler;
import cn.detachment.utils.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoxp
 * @date 20/7/27
 */
@SuppressWarnings("unused")
public class DefaultHttpApiSupport implements HttpUtilApi {

    public static volatile RequestConfig DEFAULT_REQUEST = RequestConfig.custom()
            .setConnectionRequestTimeout(RemoteRequest.DEFAULT_TIMEOUT)
            .setSocketTimeout(RemoteRequest.DEFAULT_TIMEOUT)
            .setConnectTimeout(RemoteRequest.DEFAULT_CONNECTION_TIME)
            .build();


    public static final String URL_QUERY_CONTACT = "?";

    public static final int SUCCESS = 200;
    public static final int REDIRECT = 300;

    public static final int BAD_REQUEST = 400;

    private static final String REDIRECT_HEADER = "location";


    public String requestExecute(RemoteRequest remoteRequest) {
        return "";
    }

    @Override
    public String parserRemoteRequest(RemoteRequest remoteRequest) throws IOException {

        return HttpUtil.request(createRequestBuilderByRemoteRequest(remoteRequest), remoteRequest.getHeader(),
                DefaultStringHandler.getInstanceByCharset(remoteRequest.getResponseCharset()), getRequestConfig(remoteRequest));
    }

    public RequestBuilder createRequestBuilderByRemoteRequest(RemoteRequest remoteRequest) throws IOException {
        String url = inflateUrl(remoteRequest);
        RequestBuilder requestBuilder = HttpUtil.methodInstanceMap.get(remoteRequest.getHttpMethod().name())
                .instanceByHttpMethod(remoteRequest.getUrl());
        inflateForm(requestBuilder, remoteRequest);
        inflateBody(requestBuilder, remoteRequest);
        return requestBuilder;
    }

    public RequestBuilder inflateBody(RequestBuilder requestBuilder, RemoteRequest remoteRequest) {
        if (remoteRequest.getRequestBody() != null) {
            StringEntity entity = new StringEntity(JSONObject.toJSONString(remoteRequest.getRequestBody()), ContentType.APPLICATION_JSON);
            requestBuilder.setEntity(entity);
        }
        return requestBuilder;
    }

    public RequestBuilder inflateForm(RequestBuilder requestBuilder, RemoteRequest remoteRequest) {
        if (CollectionUtils.isEmpty(remoteRequest.getFormData()) && HttpMethod.GET.equals(remoteRequest.getHttpMethod())) {
            return requestBuilder;
        }
        List<NameValuePair> pairs = new ArrayList<>();
        if (!CollectionUtils.isEmpty(remoteRequest.getFormData())) {
            remoteRequest.getFormData().entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .forEach(e -> pairs.add(new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue()))));
        }
        requestBuilder.setEntity(new UrlEncodedFormEntity(pairs, remoteRequest.getEncode()));
        return requestBuilder;
    }

    public static String inflateUrl(RemoteRequest remoteRequest) throws IOException {
        String url = remoteRequest.getUrl();
        if (HttpMethod.GET.equals(remoteRequest.getHttpMethod()) && !CollectionUtils.isEmpty(remoteRequest.getUrlParams())) {
            List<BasicNameValuePair> collect = remoteRequest.getUrlParams().entrySet().stream()
                    .filter(e -> Objects.nonNull(e.getValue()))
                    .map(e -> new BasicNameValuePair(e.getKey(), String.valueOf(e.getValue())))
                    .collect(Collectors.toList());
            String paramStr = EntityUtils.toString(new UrlEncodedFormEntity(collect, StandardCharsets.UTF_8));
            url = url + "?" + paramStr;
        }
        return url;
    }

    private RequestConfig getRequestConfig(RemoteRequest remoteRequest) {
        if (RemoteRequest.DEFAULT_TIMEOUT == remoteRequest.getSocketTimeout()
                && RemoteRequest.DEFAULT_CONNECTION_TIME == remoteRequest.getConnectTimeout()
        ) {
            return DEFAULT_REQUEST;
        }

        return RequestConfig.custom()
                .setConnectTimeout(remoteRequest.getConnectTimeout())
                .setConnectionRequestTimeout(remoteRequest.getConnectTimeout())
                .setSocketTimeout(remoteRequest.getSocketTimeout())
                .build();
    }


}
