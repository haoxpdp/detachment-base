package cn.detachment.frame.rocketmq.core;

import cn.detachment.frame.rocketmq.support.RocketMQConsumerLifecycleListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 17:19
 */
public interface RocketMQPushConsumerLifecycleListener extends RocketMQConsumerLifecycleListener<DefaultMQPushConsumer> {
}
