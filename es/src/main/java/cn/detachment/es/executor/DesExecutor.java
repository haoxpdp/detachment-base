package cn.detachment.es.executor;

import cn.detachment.es.wrapper.DesSearchWrapper;

import java.util.List;

/**
 * es 操作执行器
 * search
 * update
 * create
 * delete
 *
 * @param <T> 实体类
 * @author haoxp
 */
public interface DesExecutor<T> {

    /**
     * queryList
     *
     * @param searchWrapper searchWrapper
     * @return java.util.List<T>
     * @author haoxp
     * @date 20/5/31 19:57
     */
    List<T> queryList(DesSearchWrapper<T> searchWrapper);

    /**
     * filterList
     *
     * @param filterWrapper filterWrapper
     * @return java.util.List<T>
     * @author haoxp
     * @date 20/5/31 19:57
     */
    List<T> filterList(DesSearchWrapper<T> filterWrapper);

    /**
     * update
     *
     * @param t t
     * @return void
     * @author haoxp
     * @date 20/5/31 19:58
     */
    void update(T t);

    /**
     * updateWithVersion
     *
     * @param t       t
     * @param version version
     * @return void
     * @author haoxp
     * @date 20/5/31 19:58
     */
    void updateWithVersion(T t, String version);


    /**
     * insert
     *
     * @param t t
     * @return void
     * @author haoxp
     * @date 20/5/31 19:58
     */
    void insert(T t);
}
