package cn.detachment.frame.rocketmq.support;

import cn.detachment.frame.rocketmq.core.RocketmqListener;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 15:57
 */
public interface RocketmqListenerContainer extends DisposableBean {

    /**
     * Setup the message listener to use. Throws an {@link IllegalArgumentException} if that message listener type is
     * not supported.
     */
    void setupMessageListener(RocketmqListener<?> messageListener);

}
