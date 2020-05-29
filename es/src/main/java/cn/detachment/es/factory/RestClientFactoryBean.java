package cn.detachment.es.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author haoxp
 */
public class RestClientFactoryBean implements FactoryBean<RestClientFactoryBean>, InitializingBean {
    @Override
    public RestClientFactoryBean getObject() throws Exception {
        return null;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
