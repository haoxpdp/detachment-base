package cn.detachment.es.executor;

import cn.detachment.es.condition.DesSearchWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;

/**
 * es 操作执行器
 * search
 * update
 * create
 * delete
 *
 * @param <T> 实体类
 * @author haoxp
 */
public interface DesExecutor<T> {

    /**
     * baseSearch
     *
     * @param searchWrapper searchWrapper
     * @return org.elasticsearch.action.search.SearchResponse
     * @author haoxp
     * @date 20/7/22 20:39
     */
    SearchResponse baseSearch(DesSearchWrapper<T> searchWrapper);


    SearchResponse baseSearch(SearchRequest searchRequest);

    SearchResponse baseSearch(SearchRequest searchRequest, DesSearchWrapper<T> searchWrapper);
}
