package cn.detachment.example.es;


import cn.detachment.example.es.bean.Person;
import cn.detachment.frame.es.util.DesSearchWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author haoxp
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private static final Logger logger = LoggerFactory.getLogger(App.class);

    @Resource
    private RestHighLevelClient esClient;

    @Override
    public void run(String... args) throws Exception {
        String index = "person";

        DesSearchWrapper<Person> desSearchWrapper = new DesSearchWrapper<>(index);
        DesSearchWrapper wrapper = new DesSearchWrapper<>(index);

        SearchRequest searchRequest1 = desSearchWrapper
                .or(new DesSearchWrapper<>(index))
                .orderBy(Person::getAge, SortOrder.DESC)
                .finish();

        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("age","33"));
        searchSourceBuilder.query(QueryBuilders.termQuery("name","ad"));
        searchRequest.source(searchSourceBuilder);

        RequestOptions options = RequestOptions.DEFAULT;


        SearchResponse response =esClient.search(searchRequest1,options);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits){
            logger.info("hit : version {} , data {} ",hit.getScore(),hit.getSourceAsMap());

        }

        esClient.close();
    }
}
