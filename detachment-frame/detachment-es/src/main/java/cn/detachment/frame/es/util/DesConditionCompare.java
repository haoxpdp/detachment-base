package cn.detachment.frame.es.util;

import java.io.Serializable;

/**
 * @author haoxp
 */
public interface DesConditionCompare<Children,F extends Serializable> {

    /**
     * termEq
     *
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children termEq(F f, Object val);

    /**
     * >=
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children ge(F f,Object val);

    /**
     * >
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children gt(F f,Object val);

    /**
     * <=
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children le(F f,Object val);

    /**
     * <
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children lt(F f,Object val);

    /**
     * from v1 - v2
     * @param f
     * @param v1
     * @param v2
     * @param <V>
     * @return
     */
    <V> Children between(F f,Object v1,Object v2);
}
