package cn.detachment.es.support;

import cn.detachment.es.condition.DesConditionWrapper;
import cn.detachment.es.util.RefUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

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


    public DesSearchWrapper<T> and(DesSearchWrapper<T> queryWrapper) {
        boolQueryBuilder.must(queryWrapper.getBoolQueryBuilder());
        return thisType;
    }


    public DesSearchWrapper<T> filter(DesSearchWrapper<T> queryWrapper) {
        List<QueryBuilder> queryBuilderList = queryWrapper.getBoolQueryBuilder().must();
        if (!CollectionUtils.isEmpty(queryBuilderList)) {
            queryBuilderList.forEach(i -> boolQueryBuilder.filter(i));
        }
        return thisType;
    }


    public DesSearchWrapper<T> or(DesSearchWrapper<T> queryWrapper) {
        List<QueryBuilder> queryBuilderList = queryWrapper.getBoolQueryBuilder().must();
        if (!CollectionUtils.isEmpty(queryBuilderList)) {
            queryBuilderList.forEach(i -> boolQueryBuilder.should(i));
        }
        return thisType;
    }

    public DesSearchWrapper<T> or(BoolQueryBuilder boolQueryBuilder) {
        boolQueryBuilder.should(boolQueryBuilder);
        return thisType;
    }

    public DesSearchWrapper<T> not(DesSearchWrapper<T> queryWrapper) {
        boolQueryBuilder.must(queryWrapper.getBoolQueryBuilder());
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, String value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, int value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, long value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, float value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, double value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, boolean value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, Object value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, String value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, int value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, long value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, float value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, double value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, boolean value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, Object value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtil.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, String... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, long... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, int... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, double... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, float... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, Object... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, Collection<?> values) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), values));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, String... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, long... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, int... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, double... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, float... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, Object... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, Collection<?> values) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtil.getFiledName(f), values));
        return thisType;
    }

    public DesSearchWrapper<T> ge(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gte(val));
        return thisType;
    }

    public DesSearchWrapper<T> gt(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).gt(val));
        return thisType;
    }

    public DesSearchWrapper<T> le(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lte(val));
        return thisType;
    }

    public DesSearchWrapper<T> lt(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).lt(val));
        return thisType;
    }

    public DesSearchWrapper<T> between(FiledFunction<T, ?> f, Object v1, Object v2) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtil.getFiledName(f)).from(v1).to(v2));
        return thisType;
    }

    public DesSearchWrapper<T> match(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.matchQuery(RefUtil.getFiledName(f), val));
        return thisType;
    }

    public DesSearchWrapper<T> transferToFilter() {
        return thisType;
    }
}
