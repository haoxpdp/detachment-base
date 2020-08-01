package cn.detach.api.support;

import cn.detach.api.http.RemoteRequest;
import cn.hutool.http.HttpUtil;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

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


    public String requestExecute(RemoteRequest remoteRequest) {
        return "";
    }

    public String parserRemoteRequest(RemoteRequest remoteRequest) {
//        HttpUriRequest request = new Http
        HttpClient client = HttpClients.createDefault();


        return "";
    }

}
