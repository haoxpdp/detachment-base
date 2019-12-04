package cn.detachment.frame.es.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author haoxp
 */
public class DesSearchWrapper<T> extends DesWrapper<T,FiledFunction<T,?>,DesSearchWrapper<T>>{

    private String index;


    public DesSearchWrapper(String index){
        this.index = index;
        searchRequest = new SearchRequest(index);
        searchSourceBuilder = new SearchSourceBuilder();
    }

}
