package cn.detachment.frame.redis.config;

import cn.detachment.frame.redis.advisor.RedisLockAdvisor;
import cn.detachment.frame.redis.interceptor.RedisInterceptor;
import cn.detachment.frame.redis.util.BaseRedisLock;
import cn.detachment.frame.redis.util.RedisLock;
import cn.detachment.frame.redis.util.RedissonLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/17 23:31
 */
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class LockConfig {

    private static Logger logger = LoggerFactory.getLogger(LockConfig.class);

    @Resource
    RedisTemplate<String, Object> redisTemplate;


    @Bean("redisLock")
    @ConditionalOnBean(name = "redisTemplate")
    @ConditionalOnMissingBean({RedisLock.class,RedissonClient.class})
    public RedisLock redisLock() {
        logger.info("load detach-base redis_lock success!");
        return new BaseRedisLock(redisTemplate);
    }

    @Bean("redisLock")
    @ConditionalOnBean(name = "redissonClient")
    @ConditionalOnMissingBean(RedisLock.class)
    public RedisLock redissonLock(RedissonClient client) {
        logger.info("load detach-redisson redis_lock success!");
        return new RedissonLock(client);
    }

    @Bean
    @ConditionalOnBean(name = "redisLock")
    @ConditionalOnMissingBean(name = "redisInterceptor")
    public RedisInterceptor redisInterceptor(RedisLock redisLock) {
        logger.info("load detach-base redis_lock_interceptor success!");
        return new RedisInterceptor(redisLock);
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisLockAdvisor")
    @ConditionalOnBean(name = "redisInterceptor")
    public RedisLockAdvisor redisLockAdvisor(RedisInterceptor redisInterceptor) {
        logger.info("load detach-base redis_lock_advisor success!");
        return new RedisLockAdvisor(redisInterceptor);
    }
}
