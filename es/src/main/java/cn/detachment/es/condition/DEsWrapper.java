package cn.detachment.es.condition;

import cn.detachment.es.support.FiledFunction;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class DEsWrapper<T, F extends FiledFunction<T, ?>, Children extends DEsWrapper<T, F, Children, Param>, Param> {
    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected BoolQueryBuilder boolQueryBuilder;
    protected final Children thisType = (Children) this;

}
