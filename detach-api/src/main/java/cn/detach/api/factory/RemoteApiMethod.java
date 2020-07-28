package cn.detach.api.factory;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.constant.HttpMethod;
import cn.detach.api.support.HttpUtilApi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

/**
 * @author haoxp
 * @date 20/7/27
 */
public class RemoteApiMethod {

    private Method method;

    private Object[] args;

    private HttpUtilApi apiSupport;

    private RemoteApi remoteApi;


    public RemoteApiMethod(Method method) {
        this.method = method;
        this.remoteApi = method.getAnnotation(RemoteApi.class);
        if (Objects.isNull(remoteApi)) {
            throw new RuntimeException("remote api must have RemoteApi annotation, "
                    + method.getDeclaringClass() + " " + method.getName());
        }
        if (method.getParameterCount() > 0) {
            Parameter[] parameters = method.getParameters();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                Annotation[] annotations = parameterAnnotations[i];
                System.out.println(parameter.getName());

            }
        }
    }

    public Object execute(Object[] args, HttpUtilApi apiSupport) {
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
