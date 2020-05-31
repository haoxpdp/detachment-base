package cn.detachment.es.factory;

import cn.detachment.es.config.EsProperties;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:43
 */
public class EsClientFactoryBean implements FactoryBean<RestHighLevelClient>, InitializingBean {

    private RestHighLevelClient client;

    @Autowired
    private EsProperties esProperties;

    public EsClientFactoryBean(EsProperties properties) {
        this.esProperties = properties;
    }

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
        RestClientBuilder builder = RestClient.builder(esProperties.getHosts())
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(esProperties.getConnectTimeout());
                    requestConfigBuilder.setSocketTimeout(esProperties.getSocketTimeout());
                    requestConfigBuilder.setConnectionRequestTimeout(esProperties.getRequestTimeout());
                    return requestConfigBuilder;
                });
        if (!StringUtils.isEmpty(esProperties.getUserName()) && !StringUtils.isEmpty(esProperties.getPassword())) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(esProperties.getUserName(), esProperties.getPassword()));
            builder.setHttpClientConfigCallback(httpClientBuilder -> {
                httpClientBuilder.disableAuthCaching();
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }


        client = new RestHighLevelClient(builder);
    }
}
