package cn.detachment.es.condition;

import cn.detachment.es.util.FiledFunction;
import cn.detachment.es.util.RefUtils;
import lombok.Getter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class DesConditionWrapper<T, F extends FiledFunction<T, ?>, Children extends DesConditionWrapper<T, F, Children, Param>, Param> implements Nested<Children, Param> {

    private static Logger logger = LoggerFactory.getLogger(DesConditionWrapper.class);

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected List<AggregationBuilder> aggregationBuilderList;

    @Getter
    protected BoolQueryBuilder boolQueryBuilder;

    protected final Children thisType = (Children) this;

    private void addCondition() {
        if (boolQueryBuilder == null) {
            boolQueryBuilder = QueryBuilders.boolQuery();
        }
    }

    public Children size(int size) {
        this.searchSourceBuilder.size(size);
        return thisType;
    }

    public Children orderBy(F f) {
        this.searchSourceBuilder.sort(RefUtils.getFiledName(f));
        return thisType;
    }

    public Children orderBy(F f, SortOrder sortOrder) {
        this.searchSourceBuilder.sort(RefUtils.getFiledName(f), sortOrder);
        return thisType;
    }


}
