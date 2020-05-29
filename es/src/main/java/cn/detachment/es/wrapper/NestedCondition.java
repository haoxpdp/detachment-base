package cn.detachment.es.wrapper;

import org.elasticsearch.index.query.QueryBuilder;

/**
 *
 * @author haoxp
 */
public interface NestedCondition<T> {
    /**
     *
     * @param t
     */
    QueryBuilder condition(T t);
}
