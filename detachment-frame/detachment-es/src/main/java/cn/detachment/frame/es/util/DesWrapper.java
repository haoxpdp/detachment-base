package cn.detachment.frame.es.util;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.CollectionUtils;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public class DesWrapper<T, F extends FiledFunction<T, ?>, Children extends DesWrapper<T, F, Children>> implements DesConditionCompare<Children, F>
        ,Nested<Children> {

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

    protected BoolQueryBuilder boolQueryBuilder;

    protected final Children thisType = (Children) this;

    @Override
    public <V> Children termEq(F f, Object val) {
        boolQueryBuilder.must();
        boolQueryBuilder.mustNot();
        boolQueryBuilder.filter();
        return thisType;
    }

    @Override
    public <V> Children ge(F f, Object val) {
        return thisType;
    }

    @Override
    public <V> Children gt(F f, Object val) {
        return thisType;
    }

    @Override
    public <V> Children le(F f, Object val) {
        return thisType;
    }

    @Override
    public <V> Children lt(F f, Object val) {
        return thisType;
    }

    @Override
    public <V> Children between(F f, Object v1, Object v2) {
        return thisType;
    }


    public <V> Children must(DesWrapper<T, F, Children> wrapper) {
        boolQueryBuilder.must(wrapper.getQueryBuilders());
        return thisType;
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

    public BoolQueryBuilder getQueryBuilders() {
        return boolQueryBuilder;
    }

    public SearchRequest finish() {
        /**
         * 无条件时返回全部结果
         */
        if (this.searchSourceBuilder.query() == null
                || CollectionUtils.isEmpty(boolQueryBuilder.must())
                || CollectionUtils.isEmpty(boolQueryBuilder.mustNot())
                || CollectionUtils.isEmpty(boolQueryBuilder.filter())
                || CollectionUtils.isEmpty(boolQueryBuilder.should())) {
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.query(boolQueryBuilder);
        }
        return searchRequest.source(searchSourceBuilder);
    }

    @Override
    public Children or(Children children) {
        return thisType;
    }
}
