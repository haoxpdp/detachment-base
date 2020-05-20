package cn.detachment.es;

import cn.detachment.es.support.DesSearchWrapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@SpringBootTest
public class TestApp {

    private static Logger logger = LoggerFactory.getLogger(TestApp.class);

    @Test
    public void test() throws IOException {
        RestHighLevelClient esClient = esClient();
        String index = "Test";

        esClient.close();

    }


    public RestHighLevelClient esClient() {
        return new RestHighLevelClient(RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http")
        ));
    }
}
