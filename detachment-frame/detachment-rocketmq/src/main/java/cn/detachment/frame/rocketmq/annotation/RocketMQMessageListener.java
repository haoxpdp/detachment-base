package cn.detachment.frame.rocketmq.annotation;

import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rocket mq listener
 *
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 11:38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RocketMQMessageListener {
    String NAME_SERVER_PLACEHOLDER = "${rocketmq.name-server:}";
    String ACCESS_KEY_PLACEHOLDER = "${rocketmq.consumer.access-key:}";
    String SECRET_KEY_PLACEHOLDER = "${rocketmq.consumer.secret-key:}";
    String TRACE_TOPIC_PLACEHOLDER = "${rocketmq.consumer.customized-trace-topic:}";
    String ACCESS_CHANNEL_PLACEHOLDER = "${rocketmq.access-channel:}";

    /**
     * <a href="http://rocketmq.apache.org/docs/core-concept/">document</a>
     */
    String consumerGroup();

    /**
     * topic
     */
    String topic();

    /**
     * control how to select msg
     */
    SelectorType selectorType() default SelectorType.TAG;

    /**
     * Control which message can be select.
     * Grammar please see {@link SelectorType#TAG} and {@link SelectorType#SQL92}
     */
    String selectorExpression() default "*";


    /**
     * Control consume mode, you can choice receive message concurrently or orderly.
     */
    ConsumeMode consumeMode() default ConsumeMode.CONCURRENTLY;

    /**
     * Control message mode, if you want all subscribers receive message all message,
     * broadcasting is a good choice.
     */
    MessageModel messageModel() default MessageModel.CLUSTERING;

    /**
     * Max consumer thread number.
     */
    int consumeThreadMax() default 64;

    /**
     * Max consumer timeout, default 30s.
     */
    long consumeTimeout() default 30000L;

    /**
     * The property of "access-key".
     */
    String accessKey() default ACCESS_KEY_PLACEHOLDER;

    /**
     * The property of "secret-key".
     */
    String secretKey() default SECRET_KEY_PLACEHOLDER;

    /**
     * Switch flag instance for message trace.
     */
    boolean enableMsgTrace() default true;

    /**
     * The name value of message trace topic.If you don't config,you can use the default trace topic name.
     */
    String customizedTraceTopic() default TRACE_TOPIC_PLACEHOLDER;

    /**
     * The property of "name-server".
     */
    String nameServer() default NAME_SERVER_PLACEHOLDER;

    /**
     * The property of "access-channel".
     */
    String accessChannel() default ACCESS_CHANNEL_PLACEHOLDER;
}
