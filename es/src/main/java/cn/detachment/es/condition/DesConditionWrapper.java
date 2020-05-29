package cn.detachment.es.condition;

import cn.detachment.es.wrapper.FiledFunction;
import cn.detachment.es.util.RefUtils;
import lombok.Getter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class DesConditionWrapper<T, F extends FiledFunction<T, ?>, Children extends DesConditionWrapper<T, F, Children, Param>, Param> implements Nested<Children, Param> {

    private static Logger logger = LoggerFactory.getLogger(DesConditionWrapper.class);

    protected SearchRequest searchRequest;

    protected SearchSourceBuilder searchSourceBuilder;

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

    public SearchRequest finishQuery() {
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
        if (logger.isDebugEnabled()) {
            logger.debug("es search dsl : {}", searchSourceBuilder.toString());
        }
        logger.info("es search dsl : {}", searchSourceBuilder.toString());
        return searchRequest.source(searchSourceBuilder);
    }

    public SearchRequest finishFilter() {
        /**
         * 无条件时返回全部结果
         */
        if (this.searchSourceBuilder.query() == null
                && CollectionUtils.isEmpty(boolQueryBuilder.must())
                && CollectionUtils.isEmpty(boolQueryBuilder.mustNot())
                && CollectionUtils.isEmpty(boolQueryBuilder.filter())
                && CollectionUtils.isEmpty(boolQueryBuilder.should())) {
            searchSourceBuilder.postFilter(QueryBuilders.matchAllQuery());
        } else {
            searchSourceBuilder.postFilter(boolQueryBuilder);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("es search dsl : {}", searchSourceBuilder.toString());
        }
        return searchRequest.source(searchSourceBuilder);
    }

    private BoolQueryBuilder boolQueryBuilderInstance() {
        return QueryBuilders.boolQuery();
    }

}
