package cn.detachment.frame.redis.util;

import cn.detachment.frame.redis.bean.LockInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/17 23:14
 */
public class BaseRedisLock implements RedisLock {

    /**
     * 解决Redis分布式锁setnx后setexpire因某种问题导致没执行，导致锁一直被占的问题；
     */
    private static final RedisScript<String> SCRIPT_LOCK = new DefaultRedisScript<>(
            "return redis.call('set',KEYS[1],ARGV[1],'NX','PX',ARGV[2])", String.class);


    private static final RedisScript<String> SCRIPT_UNLOCK = new DefaultRedisScript<>(
            "if redis.call('get',KEYS[1]) == ARGV[1] then return tostring(redis.call('del', KEYS[1])==1) else return 'false' end", String.class);

    private RedisTemplate<String, Object> redisTemplate;

    public BaseRedisLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private final static String locked_flag = "OK";

    @Override
    public <T> Boolean lock(LockInfo<T> lockInfo) {
        return lock(lockInfo.getKey(), lockInfo.getValue(), lockInfo.getTimeOut());
    }

    @Override
    public <T> Boolean lock(String key, T val, Long timeOut) {
        String result = redisTemplate.execute(SCRIPT_LOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(key),
                val, String.valueOf(timeOut));
        return locked_flag.equalsIgnoreCase(result);
    }

    @Override
    public Boolean releaseLock(String key) {
        throw new UnsupportedOperationException("This method is not implemented!");
    }

    @Override
    public Boolean releaseLock(LockInfo lockInfo) {
        String result = redisTemplate.execute(SCRIPT_UNLOCK,
                redisTemplate.getStringSerializer(),
                redisTemplate.getStringSerializer(),
                Collections.singletonList(lockInfo.getKey()), lockInfo.getValue());
        return Boolean.valueOf(result);
    }
}
