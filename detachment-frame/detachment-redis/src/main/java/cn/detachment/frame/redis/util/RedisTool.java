package cn.detachment.frame.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/13 0:35
 */
public class RedisTool {
    private RedisTemplate<String, Object> redisTemplate;

    public RedisTool(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    private static Logger logger = LoggerFactory.getLogger(RedisTool.class);

    public Boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    public Long deleteKeys(String... keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return 0L;
        }
        return redisTemplate.delete(Arrays.asList(keys));
    }

    public Boolean expire(String key) {
        return redisTemplate.expire(key, 0, TimeUnit.SECONDS);
    }

    public Boolean expire(String key, int seconds) {
        return redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public Boolean expire(String key, TimeUnit timeUnit, int time) {
        return redisTemplate.expire(key, time, timeUnit);
    }

    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * getExpire
     * 返回秒
     *
     * @param key key
     * @return java.lang.Long
     * @author haoxp
     * @date 19/10/13 1:36
     */
    public Long getExpireSeconds(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public <T> T get(String key, Class<T> tClass) {
        return tClass.cast(redisTemplate.opsForValue().get(key));
    }

    public <T> Boolean set(String key, T t) {
        try {
            redisTemplate.opsForValue().set(key, t);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }



}
