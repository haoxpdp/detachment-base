package cn.detachment.example.es.util;

import cn.detachment.example.es.constant.DesCondition;
import org.elasticsearch.index.query.QueryBuilders;

import java.io.Serializable;

/**
 * @author haoxp
 */
@SuppressWarnings({"serial", "unchecked"})
public class DesWrapper<T, F extends Serializable  ,Children extends DesWrapper<T,F,Children>> implements DesConditionCompare<Children,F> {



    protected final Children thisType = (Children) this;

    @Override
    public <V> Children termEq(F f, Object val) {
        return thisType;
    }

    @Override
    public <V> Children termGe(F f, Object val) {
        return thisType;
    }

    public Children addCondition(boolean condition, F field, DesCondition desCondition, Object val){
        return thisType;
    }

    public Children add(){
        return thisType;
    }

    public void finish(){
        return;
    }
}
