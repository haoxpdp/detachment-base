package cn.detachment.frame.es.condition;

import cn.detachment.frame.es.support.FiledFunction;
import cn.detachment.frame.es.util.RefUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class DesConditionWrapper<T, F extends FiledFunction<T, ?>, Children extends DesConditionWrapper<T, F, Children, Param>, Param> implements Nested<Children, Param> {

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected BoolQueryBuilder boolQueryBuilder;

    protected final Children thisType = (Children) this;


    public QueryBuilder termEq(F f, String value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, int value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, long value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, float value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, double value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, boolean value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder termEq(F f, Object value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    public QueryBuilder ge(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gte(val);
    }

    public QueryBuilder gt(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gt(val);
    }

    public QueryBuilder le(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lte(val);
    }

    public QueryBuilder lt(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lt(val);
    }

    public QueryBuilder between(F f, Object v1, Object v2) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).from(v1).to(v2);
    }

    public QueryBuilder match(F f, Object val) {
        return QueryBuilders.matchQuery(RefUtil.getFiledName(f), val);
    }

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
        this.searchSourceBuilder.sort(RefUtil.getFiledName(f));
        return thisType;
    }

    public Children orderBy(F f, SortOrder sortOrder) {
        this.searchSourceBuilder.sort(RefUtil.getFiledName(f), sortOrder);
        return thisType;
    }

    public SearchRequest finish() {
        /**
         * 无条件时返回全部结果
         */
        if (this.searchSourceBuilder.query() == null
                && CollectionUtils.isEmpty(boolQueryBuilder.must())
                && CollectionUtils.isEmpty(boolQueryBuilder.mustNot())
                && CollectionUtils.isEmpty(boolQueryBuilder.filter())
                && CollectionUtils.isEmpty(boolQueryBuilder.should())) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.query(boolQueryBuilder);
        }
        return searchRequest.source(searchSourceBuilder);
    }

    private BoolQueryBuilder boolQueryBuilderInstance() {
        return QueryBuilders.boolQuery();
    }
}
