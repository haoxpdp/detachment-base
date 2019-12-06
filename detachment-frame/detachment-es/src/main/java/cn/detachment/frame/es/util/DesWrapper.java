package cn.detachment.frame.es.util;

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
public abstract class DesWrapper<T, F extends FiledFunction<T, ?>, Children extends DesWrapper<T, F, Children, Param>, Param> implements DesConditionCompare<Children, F>
        , Nested<Children, Param> {

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected BoolQueryBuilder boolQueryBuilder;

    protected final Children thisType = (Children) this;

    @Override
    public <V> QueryBuilder termEq(F f, String value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, int value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, long value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, float value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, double value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, boolean value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder termEq(F f, Object value) {
        return QueryBuilders.termQuery(RefUtil.getFiledName(f), value);
    }

    @Override
    public <V> QueryBuilder ge(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gte(val);
    }

    @Override
    public <V> QueryBuilder gt(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gt(val);
    }

    @Override
    public <V> QueryBuilder le(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lte(val);
    }

    @Override
    public <V> QueryBuilder lt(F f, Object val) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lt(val);
    }

    @Override
    public <V> QueryBuilder between(F f, Object v1, Object v2) {
        return QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).from(v1).to(v2);
    }

    @Override
    public <V> QueryBuilder match(F f, Object val) {
        return QueryBuilders.matchQuery(RefUtil.getFiledName(f), val);
    }


    private void addCondition() {
        if (boolQueryBuilder == null) {
            boolQueryBuilder = QueryBuilders.boolQuery();
        }
    }

    public <V> Children size(int size) {
        this.searchSourceBuilder.size(size);
        return thisType;
    }

    public <V> Children orderBy(F f) {
        this.searchSourceBuilder.sort(RefUtil.getFiledName(f));
        return thisType;
    }

    public <V> Children orderBy(F f, SortOrder sortOrder) {
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
