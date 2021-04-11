package cn.detach.api.factory;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.api.RemotePost;
import cn.detach.api.constant.ContentType;
import cn.detach.api.constant.HttpMethod;
import cn.detach.api.relection.RemoteApiMethod;
import cn.detach.api.support.HttpUtilApi;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @date 20/7/27
 */
@SuppressWarnings("unused")
public class RemoteApiProxyFactory implements InvocationHandler {

    private final HttpUtilApi httpSupport;

    public RemoteApiProxyFactory(Class<?> api, HttpUtilApi httpApiSupport) {
        this.httpSupport = httpApiSupport;
    }

    private final Map<Method, RemoteApiMethod> methodCaches = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        Annotation[] annotations = method.getAnnotations();
        RemoteApi remoteApi = method.getAnnotation(RemoteApi.class);
        if (remoteApi == null) {

            if (method.getAnnotation(RemotePost.class) != null) {
                remoteApi = (RemoteApi) method.getAnnotation(RemotePost.class);
            }
        }
        if (remoteApi == null) {
            return method.invoke(this, args);
        }
        RemoteApiMethod remoteApiWrapper = getRemoteApiWrapper(method);
        return remoteApiWrapper.execute(args, httpSupport);
    }

    private RemoteApiMethod getRemoteApiWrapper(Method method) {
        if (!methodCaches.containsKey(method)) {
            methodCaches.put(method, new RemoteApiMethod(method));
        }
        return methodCaches.get(method);
    }


}
