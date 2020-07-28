package cn.detach.api.factory;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.support.HttpUtilApi;
import cn.detach.api.support.RemoteApiWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 * @date 20/7/27
 */
@SuppressWarnings("unused")
public class RemoteApiProxyFactory implements InvocationHandler {

    private final Class api;

    private final HttpUtilApi httpSupport;

    public RemoteApiProxyFactory(Class api, HttpUtilApi httpApiSupport) {
        this.api = api;
        this.httpSupport = httpApiSupport;
    }

    private final Map<Method, RemoteApiWrapper> methodCaches = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        if (method.getAnnotation(RemoteApi.class) == null) {
            return method.invoke(this, args);
        }
        RemoteApiWrapper remoteApiWrapper = getRemoteApiWrapper(method);
        return remoteApiWrapper.execute(args, httpSupport);
    }

    private RemoteApiWrapper getRemoteApiWrapper(Method method) {
        if (!methodCaches.containsKey(method)) {
            methodCaches.put(method, new RemoteApiWrapper(method));
        }
        return methodCaches.get(method);
    }


}
