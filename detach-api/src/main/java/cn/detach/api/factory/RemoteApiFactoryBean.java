package cn.detach.api.factory;

import cn.detach.api.support.HttpUtilApi;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/27 20:59
 */
public class RemoteApiFactoryBean<T> implements FactoryBean<T> {

    private final Class<T> remoteApi;

    @Getter
    @Setter
    private HttpUtilApi apiSupport;

    public RemoteApiFactoryBean(Class<T> remoteApi) {
        this.remoteApi = remoteApi;
    }

    @Override
    public T getObject() throws Exception {
        return newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return remoteApi;
    }

    @SuppressWarnings("unchecked")
    public T newInstance() {
        RemoteApiProxyFactory proxyFactory = new RemoteApiProxyFactory(remoteApi, apiSupport);
        return (T) Proxy.newProxyInstance(remoteApi.getClassLoader(), new Class[]{remoteApi}, proxyFactory);
    }
}
