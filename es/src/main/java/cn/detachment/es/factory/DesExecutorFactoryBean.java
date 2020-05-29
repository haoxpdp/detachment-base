package cn.detachment.es.factory;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author haoxp
 */
public class DesExecutorFactoryBean<T> implements FactoryBean<T> {



    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
