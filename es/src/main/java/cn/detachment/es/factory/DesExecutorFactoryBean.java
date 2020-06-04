package cn.detachment.es.factory;

import cn.detachment.es.executor.DesExecutorProxy;
import cn.detachment.es.support.EsAdapterSupport;
import cn.detachment.es.support.EsClientSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author haoxp
 */
public class DesExecutorFactoryBean<T> extends EsAdapterSupport implements FactoryBean<T> {

    private static Logger logger = LoggerFactory.getLogger(DesExecutorFactoryBean.class);

    private Class<T> api;

    public DesExecutorFactoryBean(Class<T> api) {
        this.api = api;
    }

    @Override
    public T getObject() throws Exception {
        return newInstance();
    }


    public T newInstance() {
        DesExecutorProxy<T> proxy = new DesExecutorProxy<>(api, esClient);
        return (T) Proxy.newProxyInstance(api.getClassLoader(), new Class[]{api}, proxy);
    }

    @Override
    public Class<?> getObjectType() {
        return api;
    }
}
