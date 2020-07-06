package cn.detachment.es.adapter;

import cn.detachment.es.constant.AggregationType;
import cn.detachment.es.exception.DesSearchException;
import cn.detachment.es.support.EsClientSupport;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregatorFactories;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.*;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haoxp
 */
public class DefaultEsAdapter extends EsClientSupport implements EsAdapter {

    private SearchConfiguration searchConfiguration;

    public DefaultEsAdapter() {
        searchConfiguration = new SearchConfiguration();
    }

    static Map<String, GetAggregationResult> getAggregationResultMap = new HashMap<>();

    static {

        getAggregationResultMap.put(AggregationType.avg, (aggregation -> BigDecimal.valueOf(((Avg) aggregation).getValue())));
        getAggregationResultMap.put(AggregationType.sum, (aggregation -> BigDecimal.valueOf(((Sum) aggregation).getValue())));
        getAggregationResultMap.put(AggregationType.max, (aggregation -> BigDecimal.valueOf(((Max) aggregation).getValue())));
        getAggregationResultMap.put(AggregationType.min, (aggregation -> BigDecimal.valueOf(((Min) aggregation).getValue())));
        getAggregationResultMap.put(AggregationType.cardinality, (aggregation -> BigDecimal.valueOf(((Cardinality) aggregation).getValue())));
    }


    @Override
    public SearchConfiguration getSearchConfiguration() {
        return null;
    }

    @Override
    public RestHighLevelClient getClient() {
        return null;
    }

    @Override
    public <E> List<E> selectList(SearchRequest request, Class<E> clzz) throws IOException {
        Assert.isTrue(request.source().size() <= 10000, "search size must less than 10000, current is " + request.source().size());
        SearchResponse searchResponse = esClient.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();
        return Arrays.stream(hits).map(SearchHit::getSourceAsString).map(s -> JSONObject.parseObject(s, clzz)).collect(Collectors.toList());
    }

    @Override
    public <T> T selectOne(SearchRequest request, Class<T> clzz) throws IOException {
        List<T> list = selectList(request, clzz);
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() > 1) {
            throw new DesSearchException("Expected one or null result to be returned,but found : " + list.size());
        }
        return null;
    }

    @Override
    public Long count(CountRequest request) throws IOException {
        CountResponse countResponse = esClient.count(request, RequestOptions.DEFAULT);

        return countResponse.getCount();
    }

    @Override
    public Map<?, ?> aggregation(SearchRequest request) throws IOException {
        SearchResponse searchResponse = esClient.search(request, RequestOptions.DEFAULT);
        AggregatorFactories.Builder aggregations = request.source().aggregations();
        Assert.notNull(aggregations, "aggregations is null");
        Collection<AggregationBuilder> aggregationBuilders = aggregations.getAggregatorFactories();
        if (CollectionUtils.isEmpty(aggregationBuilders)) {
            throw new DesSearchException("aggregation condition is empty!");
        }
        Map<String, ?> aggregationResult = getAggregationResult(aggregationBuilders, searchResponse);

        return null;
    }

    private Map<String, ?> getAggregationResult(Collection<AggregationBuilder> aggregationBuilders, SearchResponse searchResponse) {
        Map<String, Object> result = new HashMap<>(aggregationBuilders.size());
        for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
            String key = aggregationBuilder.getName();
            String type = aggregationBuilder.getType();
            Aggregation aggregation = searchResponse.getAggregations().get(key);
            if (getAggregationResultMap.containsKey(type)) {
                result.put(key, getAggregationResultMap.get(type).getValue(aggregation));

            } else {
                result.put(key, aggregation);
            }
        }
        return result;
    }

    interface GetAggregationResult {
        BigDecimal getValue(Aggregation aggregation);
    }

}
