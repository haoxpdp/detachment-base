package cn.detachment.redis.interceptor;

import cn.detachment.redis.lock.Lock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class RedisLockInterceptor implements MethodInterceptor {

    private Lock redisLock;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("---RedisLockInterceptor---");
        return invocation.proceed();
    }

    public Lock getRedisLock() {
        return redisLock;
    }

    public void setRedisLock(Lock redisLock) {
        this.redisLock = redisLock;
    }
}
