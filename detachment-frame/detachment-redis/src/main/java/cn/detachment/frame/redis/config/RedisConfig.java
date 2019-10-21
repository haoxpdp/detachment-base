package cn.detachment.frame.redis.config;

import cn.detachment.frame.redis.util.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/12 17:42
 */
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {
    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);


    @Bean
    public FastJsonRedisSerializer redisSerializer() {
        return new FastJsonRedisSerializer<>(Object.class);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();

        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);

        setSerializer(template, redisSerializer);
        // 初始化RedisTemplate
        template.afterPropertiesSet();

        logger.info("RedisTemplate init... successfully!");
        return template;
    }


    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> redisSerializer) {
        StringRedisTemplate template = new StringRedisTemplate();

        // 设置连接工厂
        template.setConnectionFactory(redisConnectionFactory);

        setSerializer(template, redisSerializer);
        // 初始化RedisTemplate
        template.afterPropertiesSet();

        logger.info("StringRedisTemplate init... successfully!");
        return template;
    }

    private <K, V> void setSerializer(RedisTemplate<K, V> template, RedisSerializer<Object> redisSerializer) {
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
    }

    @Bean
    @ConditionalOnBean(name = "redisTemplate")
    public RedisTool redisTool(RedisTemplate redisTemplate) {
        RedisTool redisTool = new RedisTool(redisTemplate);
        return redisTool;
    }


}
