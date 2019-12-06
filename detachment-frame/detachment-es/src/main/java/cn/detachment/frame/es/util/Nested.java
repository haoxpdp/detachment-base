package cn.detachment.frame.es.util;

import java.io.Serializable;

/**
 * @param <Children>
 * @author haoxp
 */
public interface Nested<Children,Param> extends Serializable {

    /**
     * 内嵌or方法
     *
     * @param func
     * @return
     */
    Children should(NestedCondition<Param> func);

    /**
     * 内嵌must
     *
     * @param func
     * @return
     */
    Children must(NestedCondition<Param> func);


    /**
     * 内嵌mustNot
     *
     * @param func
     * @return
     */
    Children mustNot(NestedCondition<Param> func);
}