package cn.detachment.frame.rocketmq.configure;

import cn.detachment.frame.rocketmq.config.RocketMQProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.rocketmq.client.MQAdmin;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 14:52
 */

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
@ConditionalOnClass({ MQAdmin.class, ObjectMapper.class })
@ConditionalOnProperty(prefix = "rocketmq", value = "name-server", matchIfMissing = true)
@Import({ JacksonFallbackConfiguration.class, ListenerContainerConfiguration.class})
@AutoConfigureAfter(JacksonAutoConfiguration.class)
public class AutoConfiguration {

}


