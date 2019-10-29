package cn.detachment.frame.rocketmq.support;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 17:19
 */
public interface RocketMQConsumerLifecycleListener<T> {
    void prepareStart(final T consumer);
}
