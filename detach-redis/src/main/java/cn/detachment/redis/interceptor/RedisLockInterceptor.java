package cn.detachment.redis.interceptor;

import cn.detachment.redis.lock.RedisLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class RedisLockInterceptor implements MethodInterceptor {

    private RedisLock redisLock;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        return null;
    }

    public RedisLock getRedisLock() {
        return redisLock;
    }

    public void setRedisLock(RedisLock redisLock) {
        this.redisLock = redisLock;
    }
}
