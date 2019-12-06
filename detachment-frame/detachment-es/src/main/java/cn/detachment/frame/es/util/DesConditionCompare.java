package cn.detachment.frame.es.util;

import org.elasticsearch.index.query.QueryBuilder;
import java.io.Serializable;

/**
 * @author haoxp
 */
public interface DesConditionCompare<Children,F extends Serializable> {

    <V> QueryBuilder termEq(F f, String value);
    <V> QueryBuilder termEq(F f, int value);
    <V> QueryBuilder termEq(F f, long value);
    <V> QueryBuilder termEq(F f, float value);
    <V> QueryBuilder termEq(F f, double value);
    <V> QueryBuilder termEq(F f, boolean value);
    <V> QueryBuilder termEq(F f, Object value);

    /**
     * >=
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> QueryBuilder ge(F f,Object val);

    /**
     * >
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> QueryBuilder gt(F f,Object val);

    /**
     * <=
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> QueryBuilder le(F f,Object val);

    /**
     * <
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> QueryBuilder lt(F f,Object val);

    /**
     * from v1 - v2
     * @param f
     * @param v1
     * @param v2
     * @param <V>
     * @return
     */
    <V> QueryBuilder between(F f,Object v1,Object v2);
}
