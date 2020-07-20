package cn.detachment.es.adapter;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author haoxp
 */
public interface EsAdapter {

    /**
     * search
     *
     * @param request request
     * @return org.elasticsearch.action.search.SearchResponse
     * @throws IOException
     * @author haoxp
     * @date 20/7/14 9:40
     */
    SearchResponse search(SearchRequest request) throws IOException;

    /**
     * list
     *
     * @param response response
     * @param eClass   eClass
     * @return java.util.List<E>
     * @author haoxp
     * @date 20/7/13 10:56
     */
    <E> List<E> list(SearchResponse response, Class<E> eClass);

    /**
     * aggregationResult
     *
     * @param response response
     * @return java.util.Map<java.lang.String, ?>
     * @author haoxp
     * @date 20/7/13 11:00
     */
    Map<String, ?> aggregationResult(SearchResponse response);

}
