package cn.detachment.es.executor;

import cn.detachment.es.annoation.DesIndex;
import cn.detachment.es.factory.DesExecutorMethod;
import org.elasticsearch.client.RestHighLevelClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 */
public class DesExecutorProxy<T> implements InvocationHandler {

    private final Class<T> api;

    private final RestHighLevelClient client;

    private Type entityType;

    private final Map<Method, DesExecutorMethod> methodCaches = new HashMap<>();

    private final DesIndex desIndex;

    public DesExecutorProxy(Class<T> api, RestHighLevelClient client) {
        this.api = api;
        this.client = client;

        Type[] types = api.getGenericInterfaces();
        for (Type t : types) {
            ParameterizedType pt = (ParameterizedType) t;
            if (DesExecutor.class.getName().equals(pt.getRawType().getTypeName())) {
                entityType = ((ParameterizedType) t).getActualTypeArguments()[0];
            }
        }
        assert entityType != null;
        desIndex = ((Class<?>) entityType).getAnnotation(DesIndex.class);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
        DesExecutorMethod desExecutorMethod = getMethod(method);
        return desExecutorMethod.execute(args, client, desIndex);
    }

    public DesExecutorMethod getMethod(Method method) {
        if (!methodCaches.containsKey(method)) {
            methodCaches.put(method, new DesExecutorMethod(api, method, entityType));
        }
        return methodCaches.get(method);

    }
}
