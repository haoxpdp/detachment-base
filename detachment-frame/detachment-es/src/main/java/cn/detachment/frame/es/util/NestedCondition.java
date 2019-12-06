package cn.detachment.frame.es.util;

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
