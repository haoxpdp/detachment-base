package cn.detachment.redis.interceptor;

import cn.detachment.redis.lock.Lock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class RedisLockInterceptor implements MethodInterceptor {

    private Lock redisLock = new Lock();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String lockVal = redisLock.lock("fasdfasdf", 123123L);
        Object returnVal = invocation.proceed();
        redisLock.release("fasdfasdf",lockVal);
        return returnVal;
    }

    public Lock getRedisLock() {
        return redisLock;
    }

    public void setRedisLock(Lock redisLock) {
        this.redisLock = redisLock;
    }
}
