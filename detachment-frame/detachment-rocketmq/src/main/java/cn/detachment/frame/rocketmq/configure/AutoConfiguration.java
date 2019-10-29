package cn.detachment.frame.rocketmq.configure;

import cn.detachment.frame.rocketmq.config.RocketMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.MQAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 14:52
 */

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass({MQAdmin.class, ObjectMapper.class})
@ConditionalOnProperty(prefix = "rocketmq", value = "name-server", matchIfMissing = true)
@Import({JacksonFallbackConfiguration.class, ListenerContainerConfiguration.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class AutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(AutoConfiguration.class);

    public AutoConfiguration() {
        logger.info("start auto configuration");
    }

    @Autowired
    private Environment environment;

    @PostConstruct
    public void checkProperties() {
        String nameServer = environment.getProperty("rocketmq.name-server", String.class);
        logger.debug("rocketmq.nameServer = {}", nameServer);
        if (nameServer == null) {
            logger.warn("The necessary spring property 'rocketmq.name-server' is not defined, all rockertmq beans creation are skipped!");
        }
    }
}


