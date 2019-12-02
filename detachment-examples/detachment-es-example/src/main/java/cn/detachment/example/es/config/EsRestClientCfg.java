package cn.detachment.example.es.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haoxp
 */
@Configuration
public class EsRestClientCfg {

    @Bean
    public RestHighLevelClient esClient(){
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("192.168.137.128",9200,"http")
        ));
    }

}
