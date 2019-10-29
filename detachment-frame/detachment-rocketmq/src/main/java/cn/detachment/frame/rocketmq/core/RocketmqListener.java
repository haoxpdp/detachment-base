package cn.detachment.frame.rocketmq.core;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 15:40
 */
public interface RocketmqListener<T> {
    void onMessage(T message);
}
