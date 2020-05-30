package cn.detachment.es.executor;

import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.client.RestHighLevelClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author haoxp
 */
public class DesExecutorProxy<T> implements InvocationHandler {

    private final Class<T> api;

    private RestHighLevelClient client;

    public DesExecutorProxy(Class<T> api) {
        this.api = api;
    }

    public DesExecutorProxy(Class<T> api, RestHighLevelClient client) {
        this.api = api;
        this.client = client;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("-----------");
        return null;
    }
}
