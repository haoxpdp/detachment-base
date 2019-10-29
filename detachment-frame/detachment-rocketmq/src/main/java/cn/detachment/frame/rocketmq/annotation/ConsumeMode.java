package cn.detachment.frame.rocketmq.annotation;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 14:22
 */
public enum ConsumeMode {
    /**
     * Receive asynchronously delivered messages concurrently
     */
    CONCURRENTLY,

    /**
     * Receive asynchronously delivered messages orderly. one queue, one thread
     */
    ORDERLY
}
