package cn.detachment.frame.rocketmq.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 15:07
 */
@Configuration
@ConditionalOnMissingBean(ObjectMapper.class)
public class JacksonFallbackConfiguration {
    @Bean
    public ObjectMapper rocketMQMessageObjectMapper() {
        return new ObjectMapper();
    }

}
