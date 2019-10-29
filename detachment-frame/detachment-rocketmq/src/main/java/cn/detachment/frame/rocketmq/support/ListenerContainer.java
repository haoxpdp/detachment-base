package cn.detachment.frame.rocketmq.support;

import cn.detachment.frame.rocketmq.annotation.ConsumeMode;
import cn.detachment.frame.rocketmq.annotation.RocketMQMessageListener;
import cn.detachment.frame.rocketmq.annotation.SelectorType;
import cn.detachment.frame.rocketmq.core.RocketMQPushConsumerLifecycleListener;
import cn.detachment.frame.rocketmq.core.RocketmqListener;
import cn.detachment.frame.rocketmq.util.RocketmqUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.RPCHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 15:56
 */
public class ListenerContainer implements InitializingBean,
        RocketmqListenerContainer, SmartLifecycle, ApplicationContextAware {

    private static Logger logger = LoggerFactory.getLogger(ListenerContainer.class);

    @Getter
    @Setter
    /**
     * bean instance name
     */
    private String name;

    @Getter
    @Setter
    private long suspendCurrentQueueTimeMillis = 1000;

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    /**
     * Message consume retry strategy<br> -1,no retry,put into DLQ directly<br> 0,broker control retry frequency<br>
     * >0,client control retry frequency.
     */
    @Getter
    @Setter
    private int delayLevelWhenNextConsume = 0;

    @Getter
    @Setter
    private String nameServer;

    @Getter
    @Setter
    private AccessChannel accessChannel = AccessChannel.LOCAL;


    private String charset = "UTF-8";

    @Getter
    @Setter
    private ObjectMapper objectMapper;

    @Getter
    @Setter
    private RocketmqListener rocketmqListener;

    private RocketMQMessageListener rocketMQMessageListener;

    @Getter
    @Setter
    private DefaultMQPushConsumer consumer;

    @Getter
    @Setter
    private Class messageType;

    @Getter
    @Setter
    private boolean running;

    /**
     * The following properties came from @RocketMQMessageListener.
     */
    @Getter
    private String consumerGroup;
    @Getter
    private String topic;
    @Getter
    private ConsumeMode consumeMode;
    @Getter
    private SelectorType selectorType;
    @Getter
    private String selectorExpression;
    @Getter
    private MessageModel messageModel;
    @Getter
    private int consumerThreadMax;
    @Getter
    private long consumeTimeout;

    public void setRocketmqMessageListener(RocketMQMessageListener ann) {
        this.rocketMQMessageListener = ann;
        this.topic = ann.topic();
        this.consumerGroup = ann.consumerGroup();
        this.consumeMode = ann.consumeMode();
        this.selectorType = ann.selectorType();
        this.selectorExpression = ann.selectorExpression();
        this.messageModel = ann.messageModel();
        this.consumerThreadMax = ann.consumeThreadMax();
        this.consumeTimeout = ann.consumeTimeout();
    }

    @Override
    public void setupMessageListener(RocketmqListener<?> messageListener) {
        this.rocketmqListener = messageListener;
    }

    @Override
    public void destroy() throws Exception {
        this.setRunning(false);
        if (Objects.nonNull(consumer)) {
            consumer.shutdown();
        }
        logger.info("container destroyed, {}", this.toString());
    }

    public boolean isAutoStartUp() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initRocketmqConsumer();
        this.messageType = getMessageType();
        logger.info("rocket mq message type {}", messageType);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        if (this.isRunning()) {
            throw new IllegalStateException("container already running. " + this.toString());
        }

        try {
            consumer.start();
        } catch (MQClientException e) {
            throw new IllegalStateException("Failed to start RocketMQ push consumer", e);
        }
        this.setRunning(true);

        logger.info("running container: {}", this.toString());
    }


    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public void stop() {
        if (this.isRunning()) {
            if (Objects.nonNull(consumer)) {
                consumer.shutdown();
            }
            setRunning(false);
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private void initRocketmqConsumer() throws MQClientException {
        Assert.notNull(rocketmqListener, "Property 'rocketmqListener' is required");
        Assert.notNull(consumerGroup, "Property 'consumerGroup' is required");
        Assert.notNull(nameServer, "Property 'nameServer' is required");
        Assert.notNull(topic, "Property 'topic' is required");

        RPCHook rpcHook = RocketmqUtil.getRPCHookByAkSk(applicationContext.getEnvironment(),
                this.rocketMQMessageListener.accessKey(), this.rocketMQMessageListener.secretKey());

        boolean enableMsgTrace = rocketMQMessageListener.enableMsgTrace();

        if (Objects.nonNull(rpcHook)) {
            consumer = new DefaultMQPushConsumer(consumerGroup, rpcHook, new AllocateMessageQueueAveragely(),
                    enableMsgTrace, this.applicationContext.getEnvironment().
                    resolveRequiredPlaceholders(this.rocketMQMessageListener.customizedTraceTopic()));
            consumer.setVipChannelEnabled(false);
            consumer.setInstanceName(RocketmqUtil.getInstanceName(rpcHook, consumerGroup));
        } else {
            logger.debug("Access-key or secret-key not configure in " + this + ".");
            consumer = new DefaultMQPushConsumer(consumerGroup, enableMsgTrace,
                    this.applicationContext.getEnvironment().
                            resolveRequiredPlaceholders(this.rocketMQMessageListener.customizedTraceTopic()));
        }

        String customizedNameServer = this.applicationContext.getEnvironment().resolveRequiredPlaceholders(this.rocketMQMessageListener.nameServer());

        if (StringUtils.isEmpty(customizedNameServer)) {
            consumer.setNamesrvAddr(nameServer);
        } else {
            consumer.setNamesrvAddr(customizedNameServer);
        }

        if (accessChannel != null) {
            consumer.setAccessChannel(accessChannel);
        }

        consumer.setConsumeThreadMax(this.consumerThreadMax);

        if (consumerThreadMax < consumer.getConsumeThreadMin()) {
            consumer.setConsumeThreadMin(consumerThreadMax);
        }

        consumer.setConsumeTimeout(consumeTimeout);

        switch (messageModel) {
            case BROADCASTING:
                consumer.setMessageModel(org.apache.rocketmq.common.protocol.heartbeat.MessageModel.BROADCASTING);
                break;
            case CLUSTERING:
                consumer.setMessageModel(org.apache.rocketmq.common.protocol.heartbeat.MessageModel.CLUSTERING);
                break;
            default:
                throw new IllegalArgumentException("Property 'messageModel' was wrong.");
        }

        switch (selectorType) {
            case TAG:
                consumer.subscribe(topic, selectorExpression);
                break;
            case SQL92:
                consumer.subscribe(topic, MessageSelector.bySql(selectorExpression));
                break;
            default:
                throw new IllegalArgumentException("Property 'selectorType' was wrong.");
        }

        switch (consumeMode) {
            case ORDERLY:
                consumer.setMessageListener(new DefaultMessageListenerOrderly());
                break;
            case CONCURRENTLY:
                consumer.setMessageListener(new DefaultMessageListenerConcurrently());
                break;
            default:
                throw new IllegalArgumentException("Property 'consumeMode' was wrong.");
        }

        if (rocketmqListener instanceof RocketMQPushConsumerLifecycleListener) {
            ((RocketMQPushConsumerLifecycleListener) rocketmqListener).prepareStart(consumer);
        }


    }

    public class DefaultMessageListenerOrderly implements MessageListenerOrderly {

        @SuppressWarnings("unchecked")
        @Override
        public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
            for (MessageExt messageExt : msgs) {
                logger.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    rocketmqListener.onMessage(doConvertMessage(messageExt));
                    long costTime = System.currentTimeMillis() - now;
                    logger.info("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    logger.warn("consume message failed. messageExt:{}", messageExt, e);
                    context.setSuspendCurrentQueueTimeMillis(suspendCurrentQueueTimeMillis);
                    return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
                }
            }

            return ConsumeOrderlyStatus.SUCCESS;
        }
    }

    private Object doConvertMessage(MessageExt messageExt) {
        if (Objects.equals(messageType, MessageExt.class)) {
            return messageExt;
        } else {
            String str = new String(messageExt.getBody(), Charset.forName(charset));
            if (Objects.equals(messageType, String.class)) {
                return str;
            } else {
                // If msgType not string, use objectMapper change it.
                try {
                    return objectMapper.readValue(str, messageType);
                } catch (Exception e) {
                    logger.error("convert failed. str:{}, msgType:{}", str, messageType);
                    throw new RuntimeException("cannot convert message to " + messageType, e);
                }
            }
        }
    }

    public class DefaultMessageListenerConcurrently implements MessageListenerConcurrently {

        @SuppressWarnings("unchecked")
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            for (MessageExt messageExt : msgs) {
                logger.debug("received msg: {}", messageExt);
                try {
                    long now = System.currentTimeMillis();
                    rocketmqListener.onMessage(doConvertMessage(messageExt));
                    long costTime = System.currentTimeMillis() - now;
                    logger.debug("consume {} cost: {} ms", messageExt.getMsgId(), costTime);
                } catch (Exception e) {
                    logger.warn("consume message failed. messageExt:{}", messageExt, e);
                    context.setDelayLevelWhenNextConsume(delayLevelWhenNextConsume);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
    }
}
