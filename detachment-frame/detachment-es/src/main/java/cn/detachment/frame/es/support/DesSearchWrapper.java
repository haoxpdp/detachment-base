package cn.detachment.frame.es.support;

import cn.detachment.frame.es.condition.DesConditionWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * @author haoxp
 */
public class DesSearchWrapper<T> extends DesConditionWrapper<T, FiledFunction<T, ?>, DesSearchWrapper<T>, DesSearchWrapper<T>> {

    private String index;

    public DesSearchWrapper(String index) {
        this.index = index;
        searchRequest = new SearchRequest(index);
        searchSourceBuilder = new SearchSourceBuilder();
        boolQueryBuilder = QueryBuilders.boolQuery();
    }


    @Override
    public DesSearchWrapper<T> should(NestedCondition<DesSearchWrapper<T>> func) {
        DesSearchWrapper<T> param = new DesSearchWrapper<>(index);
        boolQueryBuilder.should(func.condition(param));
        return thisType;
    }

    @Override
    public DesSearchWrapper<T> must(NestedCondition<DesSearchWrapper<T>> func) {
        DesSearchWrapper<T> param = new DesSearchWrapper<>(index);
        boolQueryBuilder.must(func.condition(param));
        return thisType;
    }

    @Override
    public DesSearchWrapper<T> mustNot(NestedCondition<DesSearchWrapper<T>> func) {
        DesSearchWrapper<T> param = new DesSearchWrapper<>(index);
        boolQueryBuilder.mustNot(func.condition(param));
        return thisType;
    }

    @Override
    public DesSearchWrapper<T> filter(NestedCondition<DesSearchWrapper<T>> func) {
        DesSearchWrapper<T> param = new DesSearchWrapper<>(index);
        boolQueryBuilder.filter(func.condition(param));
        return thisType;
    }
}
