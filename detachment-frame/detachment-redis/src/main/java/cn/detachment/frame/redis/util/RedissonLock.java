package cn.detachment.frame.redis.util;

import cn.detachment.frame.redis.bean.LockInfo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author haoxp
 */
public class RedissonLock implements RedisLock {

    private static final Logger logger = LoggerFactory.getLogger(RedissonLock.class);

    private RedissonClient client;

    public RedissonLock(RedissonClient client) {
        this.client = client;
    }


    @Override
    public <T> Boolean lock(LockInfo<T> lockInfo) {
        RLock rLock = client.getLock(lockInfo.getKey());
        try {
            return rLock.tryLock(lockInfo.getTimeOut(), lockInfo.getTimeOut(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            String errorMsg = String.format("加锁失败 %s %s ", rLock.getName(), e.getMessage());
            logger.error("加锁失败！ " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public <T> Boolean lock(String key, T val, Long timeOut) {
        RLock rLock = client.getLock(key);
        try {
            return rLock.tryLock(timeOut, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            String errorMsg = String.format("加锁失败 %s %s ", rLock.getName(), e.getMessage());
            logger.error("加锁失败！ " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Boolean releaseLock(String key) {
        return client.getLock(key).forceUnlock();
    }

    @Override
    public Boolean releaseLock(LockInfo lockInfo) {
        return client.getLock(lockInfo.getKey()).forceUnlock();
    }
}
