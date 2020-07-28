package cn.detach.api.relection;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.RemoteHeader;
import cn.detach.api.builder.UrlBuilder;
import cn.detach.api.constant.HttpMethod;
import cn.detach.api.support.HttpUtilApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

/**
 * @author haoxp
 * @date 20/7/27
 */
public class RemoteApiMethod {

    private static final Logger logger = LoggerFactory.getLogger(RemoteApiMethod.class);

    private Method method;


    private int headerArgIndex = -1;

    private String urlTemplate;

    private Map<String, Object> parameterMap;

    private Parameter[] parameters;


    private RemoteApi remoteApi;

    public RemoteApiMethod(Method method) {
        this.method = method;
        this.remoteApi = method.getAnnotation(RemoteApi.class);
        if (Objects.isNull(remoteApi)) {
            throw new RuntimeException("remote api must have RemoteApi annotation, "
                    + method.getDeclaringClass() + " " + method.getName());
        }
        this.parameters = method.getParameters();
        this.urlTemplate = remoteApi.url();
        parseParameters();
    }

    public Object execute(Object[] args, HttpUtilApi apiSupport) {
        String url = urlTemplate;
        if (url.contains("?")) {
            url = new UrlBuilder(urlTemplate, method, args).build();
        }
        String response = null;
        if (remoteApi.method() == HttpMethod.GET) {
            response = apiSupport.get(url);
        }
        if (method.getReturnType().equals(String.class)) {
            return response;
        }

        return null;
    }

    private void parseParameters() {
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.getAnnotation(RemoteHeader.class) != null) {
                if (headerArgIndex != -1) {
                    throw new IllegalArgumentException("find multiple headers , only need 1");
                }
                headerArgIndex = i;
            }
        }
    }

}
