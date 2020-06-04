package cn.detachment.es.adapter;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.List;

/**
 * @author haoxp
 */
public interface EsAdapter {


    /**
     * getSearchConfiguration
     *
     * @param
     * @return cn.detachment.es.adapter.SearchConfiguration
     * @author haoxp
     */
    SearchConfiguration getSearchConfiguration();

    RestHighLevelClient getClient();

    <E> List<E> selectList(SearchRequest request, Class<E> clzz) throws IOException;

    <T> T selectOne(SearchRequest request, Class<T> clzz) throws IOException;

    Long count(SearchRequest request);

}
