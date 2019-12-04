package cn.detachment.example.es.util;

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
    <V> Children termEq(F f,Object val);

    /**
     * termGe
     *
     * @param f
     * @param val
     * @param <V>
     * @return
     */
    <V> Children termGe(F f,Object val);
}
