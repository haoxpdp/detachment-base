package cn.detachment.es.executor;


import cn.detachment.es.support.DesSearchWrapper;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

/**
 * es搜索执行器
 *
 * @param <T> es实体表
 * @author haoxp
 */
public abstract class BaseSearchExecutor<T> implements EsExecutor<T> {


    /**
     * list
     * 列表查询
     *
     * @param searchWrapper searchWrapper
     * @return java.util.List<T>
     * @author haoxp
     * @date 20/5/21 15:50
     */
    public List<T> list(DesSearchWrapper<T> searchWrapper) {
//        Class<T> clazz =
        return null;
    }

}
