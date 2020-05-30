package cn.detachment.es.factory;

import cn.detachment.es.executor.DesExecutor;
import cn.detachment.es.support.ScanSupport;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author haoxp
 */
public class DesExecutorFactoryBean<T> extends ScanSupport implements FactoryBean<T> {

    private static Logger logger = LoggerFactory.getLogger(DesExecutorFactoryBean.class);

    private Class<T> api;

    public DesExecutorFactoryBean(Class<T> api) {
        this.api = api;
    }

    @Override
    public T getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
