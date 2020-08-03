package cn.detach.api.support;

import cn.detach.api.constant.HttpMethod;
import cn.detach.api.http.RemoteRequest;
import cn.hutool.http.HttpUtil;
import org.apache.http.client.methods.*;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 * @date 20/7/27
 */
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
                .instanceByHttpMethod(remoteRequest.getUrl());


        if (!CollectionUtils.isEmpty(remoteRequest.getHeader())) {
            remoteRequest.getHeader().forEach(requestBuilder::addHeader);
        }

        return httpRequest(null);
    }

    private String httpRequest(HttpUriRequest uriRequest) {
        return "";
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
