package cn.detachment.es.adapter;

import cn.detachment.es.exception.DesSearchException;
import cn.detachment.es.support.EsClientSupport;
import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoxp
 */
public class DefaultEsAdapter extends EsClientSupport implements EsAdapter {

    private SearchConfiguration searchConfiguration;

    public DefaultEsAdapter() {
        searchConfiguration = new SearchConfiguration();
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
    public Long count(SearchRequest request) {
        return null;
    }
}
