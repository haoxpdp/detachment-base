package cn.detachment.es.adapter;

import cn.detachment.es.constant.AggregationType;
import cn.detachment.es.exception.DesSearchException;
import cn.detachment.es.support.EsClientSupport;
import com.alibaba.fastjson.JSONObject;
import com.sun.istack.internal.NotNull;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregatorFactories;
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
        getAggregationResultMap.put(AggregationType.count, (aggregation -> BigDecimal.valueOf(((ValueCount) aggregation).getValue())));
        getAggregationResultMap.put(AggregationType.cardinality, (aggregation -> BigDecimal.valueOf(((Cardinality) aggregation).getValue())));
    }

    @Override
    public SearchResponse search(SearchRequest request) throws IOException {
        return esClient.search(request, RequestOptions.DEFAULT);
    }

    @Override
    public <E> List<E> list(SearchResponse response, Class<E> eClass) {
        SearchHit[] hits = response.getHits().getHits();
        return Arrays.stream(hits)
                .map(SearchHit::getSourceAsString)
                .map(s -> JSONObject.parseObject(s, eClass))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, ?> aggregationResult(SearchResponse response) {
        Map<String, Aggregation> aggregationMap = response.getAggregations().getAsMap();
        if (CollectionUtils.isEmpty(aggregationMap)) {
            return null;
        }

        return aggregationMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> getValueFromAggregation(v.getValue())));


    }

    @NotNull
    private Object getValueFromAggregation(Aggregation aggregation) {
        return aggregation;
    }

    interface GetAggregationResult {
        /**
         * getValue
         *
         * @param aggregation aggregation
         * @return java.math.BigDecimal
         * @author haoxp
         * @date 20/7/13 11:09
         */
        BigDecimal getValue(Aggregation aggregation);
    }

}
