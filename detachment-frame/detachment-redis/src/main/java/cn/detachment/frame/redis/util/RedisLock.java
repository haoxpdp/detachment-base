package cn.detachment.frame.redis.util;

import cn.detachment.frame.redis.bean.LockInfo;

/**
 * Lock
 * 分布式锁
 *
 * @author haoxp
 * @version v1.0
 * @date 19/10/14 8:28
 */
public interface RedisLock {

    /**
     * lock
     *
     * @param lockInfo lockInfo
     * @return java.lang.Boolean
     * @author haoxp
     * @date 19/10/20 0:56
     */
    <T> Boolean lock(LockInfo<T> lockInfo);

    /**
     * lock
     *
     * @param key     key
     * @param val     val
     * @param timeOut timeOut
     * @return java.lang.Boolean
     * @author haoxp
     * @date 19/10/17 23:11
     */
    <T> Boolean lock(String key, T val, Long timeOut);

    /**
     * unLock
     *
     * @param key key
     * @return java.lang.Boolean
     * @author haoxp
     * @date 19/10/14 8:29
     */
    Boolean releaseLock(String key);

    /**
     * releaseLock
     *
     * @param lockInfo lockInfo
     * @return java.lang.Boolean
     * @author haoxp
     * @date 19/10/17 23:13
     */
    Boolean releaseLock(LockInfo lockInfo);
}
