package cn.detachment.frame.es.condition;

import cn.detachment.frame.es.support.NestedCondition;

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

    /**
     * filter
     * 过滤器
     *
     * @param func func
     * @return Children
     * @author haoxp
     */
    Children filter(NestedCondition<Param> func);
}
