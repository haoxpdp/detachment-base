package cn.detachment.es.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:43
 */
public class EsClientFactoryBean implements FactoryBean<RestHighLevelClient>, InitializingBean {

    private RestHighLevelClient client;



    @Override
    public RestHighLevelClient getObject() throws Exception {
        if (client == null) {
            afterPropertiesSet();
        }
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return RestHighLevelClient.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
