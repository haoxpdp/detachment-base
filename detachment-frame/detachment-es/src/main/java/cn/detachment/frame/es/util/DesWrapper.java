package cn.detachment.frame.es.util;

import cn.detachment.example.es.constant.DesCondition;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public class DesWrapper<T, F extends FiledFunction<T,?> ,Children extends DesWrapper<T,F,Children>> implements DesConditionCompare<Children,F> {

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected final Children thisType = (Children) this;

    @Override
    public <V> Children termEq(F f, Object val) {
        this.searchSourceBuilder.query(QueryBuilders.termQuery(RefUtil.getFiledName(f),val));
        return thisType;
    }

    @Override
    public <V> Children termGe(F f, Object val) {

        return thisType;
    }

    public Children addCondition(boolean condition, F field, DesCondition desCondition, Object val){
        return thisType;
    }

    public Children add(){
        return thisType;
    }

    public SearchRequest finish(){
        return searchRequest.source(searchSourceBuilder);
    }
}
