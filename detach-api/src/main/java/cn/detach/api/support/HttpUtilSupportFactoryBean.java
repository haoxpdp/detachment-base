package cn.detach.api.support;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author haoxp
 * @date 21/1/3
 */
public class HttpUtilSupportFactoryBean implements FactoryBean<HttpUtilApi> {
    @Override
    public HttpUtilApi getObject() throws Exception {
        return new DefaultHttpApiSupport();
    }

    @Override
    public Class<?> getObjectType() {
        return HttpUtilApi.class;
    }
}
