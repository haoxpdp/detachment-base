package cn.detach.api.support;

import cn.hutool.http.HttpUtil;

import java.util.List;
import java.util.Map;

/**
 * @author haoxp
 * @date 20/7/27
 */
public class DefaultHttpApiSupport implements HttpApiSupport {
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


}
