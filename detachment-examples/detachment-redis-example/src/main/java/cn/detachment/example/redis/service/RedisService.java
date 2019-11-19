package cn.detachment.example.redis.service;

import cn.detachment.frame.redis.util.RedisTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/22 9:16
 */
@Service
public class RedisService {

    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Resource
    private RedisTool redisTool;

    public static final String key = "test_key";

    public void testRedisSet() {
        redisTool.set(key, "test val");
    }

    public String testRedisGetAndRemove() {
        String val = redisTool.get(key, String.class);
        logger.info(val);
        redisTool.expire(key);
        return val;
    }

    public boolean testHasKey() {
        return redisTool.hasKey(key);
    }
}
