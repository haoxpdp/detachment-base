package cn.detachment.es.adapter;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;

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


    /**
     * selectOne
     *
     * @param request request
     * @param clzz    clzz
     * @return T
     * @author haoxp
     * @date 20/6/10 11:40
     */
    <T> T selectOne(SearchRequest request, Class<T> clzz) throws IOException;

    /**
     * count
     *
     * @param request request
     * @return java.lang.Long
     * @author haoxp
     * @date 20/6/10 11:40
     */
    Long count(CountRequest request) throws IOException;

}
