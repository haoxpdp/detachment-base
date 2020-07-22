package cn.detachment.es.condition;

import cn.detachment.es.util.FiledFunction;
import cn.detachment.es.util.RefUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author haoxp
 */
@SuppressWarnings("unused")
public class DesSearchWrapper<T> extends DesConditionWrapper<T, FiledFunction<T, ?>, DesSearchWrapper<T>, DesSearchWrapper<T>> {

    public DesSearchWrapper() {
        this.searchSourceBuilder = new SearchSourceBuilder();
        this.boolQueryBuilder = QueryBuilders.boolQuery();
        this.aggregationBuilderList = new ArrayList<>();
    }

    public DesSearchWrapper(SearchSourceBuilder searchSourceBuilder) {
        this.searchSourceBuilder = searchSourceBuilder;
        this.boolQueryBuilder = QueryBuilders.boolQuery();
        this.aggregationBuilderList = new ArrayList<>();
    }

    public DesSearchWrapper(BoolQueryBuilder boolQueryBuilder) {

        this.searchSourceBuilder = new SearchSourceBuilder();
        this.boolQueryBuilder = boolQueryBuilder;
        this.aggregationBuilderList = new ArrayList<>();
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
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, int value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, long value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, float value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, double value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, boolean value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> eq(FiledFunction<T, ?> f, Object value) {
        boolQueryBuilder.must(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, String value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, int value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, long value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, float value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, double value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, boolean value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> ne(FiledFunction<T, ?> f, Object value) {
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(RefUtils.getFiledName(f), value));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, Object... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtils.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> in(FiledFunction<T, ?> f, Collection<?> values) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtils.getFiledName(f), values));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, Object... vs) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtils.getFiledName(f), vs));
        return thisType;
    }

    public DesSearchWrapper<T> notIn(FiledFunction<T, ?> f, Collection<?> values) {
        boolQueryBuilder.must(QueryBuilders.termsQuery(RefUtils.getFiledName(f), values));
        return thisType;
    }

    public DesSearchWrapper<T> ge(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtils.getFiledName(f)).gte(val));
        return thisType;
    }

    public DesSearchWrapper<T> gt(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtils.getFiledName(f)).gt(val));
        return thisType;
    }

    public DesSearchWrapper<T> le(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtils.getFiledName(f)).lte(val));
        return thisType;
    }

    public DesSearchWrapper<T> lt(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtils.getFiledName(f)).lt(val));
        return thisType;
    }

    public DesSearchWrapper<T> between(FiledFunction<T, ?> f, Object v1, Object v2) {
        boolQueryBuilder.must(QueryBuilders.rangeQuery(RefUtils.getFiledName(f)).from(v1).to(v2));
        return thisType;
    }

    public DesSearchWrapper<T> match(FiledFunction<T, ?> f, Object val) {
        boolQueryBuilder.must(QueryBuilders.matchQuery(RefUtils.getFiledName(f), val));
        return thisType;
    }

    public DesSearchWrapper<T> sum(FiledFunction<T, ?> f, String name) {
        aggregationBuilderList.add(AggregationBuilders.sum(name).field(RefUtils.getFiledName(f)));
        return thisType;
    }

    public DesSearchWrapper<T> sum(FiledFunction<T, ?> f) {
        return sum(f, RefUtils.getFiledName(f) + "_sum");
    }

    public DesSearchWrapper<T> count(FiledFunction<T, ?> f, String name) {
        aggregationBuilderList.add(AggregationBuilders.count(name).field(RefUtils.getFiledName(f)));
        return thisType;
    }

    public DesSearchWrapper<T> count(FiledFunction<T, ?> f) {
        return count(f, RefUtils.getFiledName(f) + "_count");
    }

    public DesSearchWrapper<T> avg(FiledFunction<T, ?> f, String name) {
        aggregationBuilderList.add(
                AggregationBuilders.avg(name)
                        .field(RefUtils.getFiledName(f))
        );
        return thisType;
    }

    public DesSearchWrapper<T> avg(FiledFunction<T, ?> f) {
        return avg(f, RefUtils.getFiledName(f) + "_avg");
    }

    public DesSearchWrapper<T> min(FiledFunction<T, ?> f, String name) {
        aggregationBuilderList.add(
                AggregationBuilders.min(name).field(RefUtils.getFiledName(f))
        );
        return thisType;
    }

    public DesSearchWrapper<T> min(FiledFunction<T, ?> f) {
        return min(f, RefUtils.getFiledName(f) + "_min");
    }

    public DesSearchWrapper<T> max(FiledFunction<T, ?> f, String name) {
        aggregationBuilderList.add(
                AggregationBuilders.max(name).field(RefUtils.getFiledName(f))
        );
        return thisType;
    }

    public DesSearchWrapper<T> max(FiledFunction<T, ?> f) {
        return max(f, RefUtils.getFiledName(f) + "_max");
    }

    // todo refix
    public DesSearchWrapper<T> sumGroup(FiledFunction<T, ?> f, String name, int size) {
        AggregationBuilders.terms(name)
                .field(RefUtils.getFiledName(f))
                .subAggregation(AggregationBuilders.terms(name + "_item"))
                .size(size);
        return thisType;
    }

    public DesSearchWrapper<T> aggregation(FiledFunction<T, ?> f, String aggregateName, AggregationBuilder aggregationBuilder) {
        AggregationBuilders.terms(aggregateName).field(RefUtils.getFiledName(f));
        this.aggregationBuilderList.add(aggregationBuilder);
        return thisType;
    }

    public DesSearchWrapper<T> groupBy() {

        return thisType;
    }

    public DesSearchWrapper<T> transferToFilter() {
        return thisType;
    }
}
