package cn.detachment.frame.rocketmq.configure;

import cn.detachment.frame.rocketmq.annotation.ConsumeMode;
import cn.detachment.frame.rocketmq.annotation.RocketMQMessageListener;
import cn.detachment.frame.rocketmq.config.RocketMQProperties;
import cn.detachment.frame.rocketmq.core.RocketmqListener;
import cn.detachment.frame.rocketmq.support.ListenerContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 15:06
 */
@Configuration
public class ListenerContainerConfiguration implements ApplicationContextAware, SmartInitializingSingleton {

    private static Logger logger = LoggerFactory.getLogger(ListenerContainerConfiguration.class);

    private AtomicLong counter = new AtomicLong(0);

    private ConfigurableApplicationContext applicationContext;

    @Resource
    private StandardEnvironment environment;

    @Resource
    private RocketMQProperties rocketMQProperties;

    @Resource
    private ObjectMapper objectMapper;


    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beans = this.applicationContext.getBeansWithAnnotation(RocketMQMessageListener.class);
        if (!CollectionUtils.isEmpty(beans)) {
            beans.forEach(this::registerContainer);
        }
    }

    private void registerContainer(String beanName, Object bean) {
        Class<?> clazz = AopProxyUtils.ultimateTargetClass(bean);

        if (!RocketmqListener.class.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(clazz + " is not instance of " + RocketMQMessageListener.class.getName());
        }

        RocketMQMessageListener annotation = clazz.getAnnotation(RocketMQMessageListener.class);

        String consumerGroup = this.environment.resolvePlaceholders(annotation.consumerGroup());
        String topic = this.environment.resolvePlaceholders(annotation.topic());

        boolean listenEnable = (boolean) rocketMQProperties.getConsumer()
                .getListeners()
                .getOrDefault(consumerGroup, Collections.EMPTY_MAP)
                .getOrDefault(topic, true);

        if (listenEnable) {
            logger.warn(
                    "Consumer Listener (group:{},topic:{}) is not enabled by configuration, will ignore initialization.",
                    consumerGroup, topic);
            return;
        }

        validate(annotation);

        String containerBeanName = String.format("%s_%s", ListenerContainerConfiguration.class.getName(),
                counter.incrementAndGet());

        GenericApplicationContext genericApplicationContext = (GenericApplicationContext) applicationContext;

        genericApplicationContext.registerBean(containerBeanName, ListenerContainer.class,
                () -> createRocketMQListenerContainer(containerBeanName, bean, annotation));

        ListenerContainer container = genericApplicationContext.getBean(containerBeanName, ListenerContainer.class);
        if (!container.isRunning()) {
            try {
                container.start();
            } catch (Exception e) {
                logger.error("Started container failed. {}", container, e);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    private void validate(RocketMQMessageListener annotation) {
        if (annotation.consumeMode() == ConsumeMode.ORDERLY &&
                annotation.messageModel() == MessageModel.BROADCASTING) {
            throw new BeanDefinitionValidationException(
                    "Bad annotation definition in @RocketMQMessageListener, messageModel BROADCASTING does not support ORDERLY message!");
        }
    }


    private ListenerContainer createRocketMQListenerContainer(String name, Object bean, RocketMQMessageListener annotation) {
        ListenerContainer container = new ListenerContainer();

        String nameServer = environment.resolvePlaceholders(annotation.nameServer());
        nameServer = StringUtils.isEmpty(nameServer) ? rocketMQProperties.getNameServer() : nameServer;
        String accessChannel = environment.resolvePlaceholders(annotation.accessChannel());
        container.setNameServer(nameServer);
        if (!StringUtils.isEmpty(accessChannel)) {
            container.setAccessChannel(AccessChannel.valueOf(accessChannel));
        }
        container.setRocketmqMessageListener(annotation);
        container.setRocketmqListener((RocketmqListener) bean);
        container.setObjectMapper(objectMapper);
        container.setName(name);  // REVIEW ME, use the same clientId or multiple?

        return container;
    }
}
