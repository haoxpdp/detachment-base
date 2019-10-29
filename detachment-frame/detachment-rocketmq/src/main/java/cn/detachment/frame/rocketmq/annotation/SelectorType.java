package cn.detachment.frame.rocketmq.annotation;

import org.apache.rocketmq.common.filter.ExpressionType;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 14:15
 */
public enum SelectorType {

    /**
     * @see ExpressionType#TAG
     */
    TAG,
    /**
     * @see ExpressionType#SQL92
     */
    SQL92

}
