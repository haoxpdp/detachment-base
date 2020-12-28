package cn.detachment.redis.interceptor;

import cn.detachment.redis.lock.RedisLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class RedisLockInterceptor implements MethodInterceptor {

    private RedisLock redisLock = new RedisLock();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        String lockVal = redisLock.lock("fasdfasdf", 123123L);
        Object returnVal = invocation.proceed();
        redisLock.release("fasdfasdf",lockVal);
        return returnVal;
    }

    public RedisLock getRedisLock() {
        return redisLock;
    }

    public void setRedisLock(RedisLock redisLock) {
        this.redisLock = redisLock;
    }
}
