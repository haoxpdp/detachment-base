package cn.detach.api.support;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.constant.HttpMethod;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author haoxp
 * @date 20/7/27
 */
public class RemoteApiWrapper {

    private Method method;

    private Object[] args;

    private HttpApiSupport apiSupport;

    private RemoteApi remoteApi;

    public RemoteApiWrapper(Method method) {
        this.method = method;
        this.remoteApi = method.getAnnotation(RemoteApi.class);
        if (Objects.isNull(remoteApi)) {
            throw new RuntimeException("remote api must have RemoteApi annotation, "
                    + method.getDeclaringClass() + " " + method.getName());
        }
    }

    public Object execute(Object[] args, HttpApiSupport apiSupport) {
        String url = remoteApi.url();
        String response = null;
        if (remoteApi.method() == HttpMethod.GET) {
            response = apiSupport.get(url);
        }
        if (method.getReturnType().equals(String.class)) {
            return response;
        }

        return null;
    }

    static class ApiWrapper {

    }


}
