package cn.detachment.frame.redis.interceptor;

import cn.detachment.frame.core.util.IpUtil;
import cn.detachment.frame.core.util.SystemClock;
import cn.detachment.frame.redis.annotation.DetachLock;
import cn.detachment.frame.redis.bean.LockInfo;
import cn.detachment.frame.redis.exception.RedisLockException;
import cn.detachment.frame.redis.exception.RedisLockExecutionException;
import cn.detachment.frame.redis.util.RedisLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/19 23:49
 */
@Component
public class RedisInterceptor implements MethodInterceptor {

    private static Logger logger = LoggerFactory.getLogger(RedisInterceptor.class);

    private RedisLock redisLock;

    public RedisInterceptor(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    private static final String PREFIX_LOCK = "redis_lock_";

    private static final String IP_ADDRESS = IpUtil.getIp();

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        String lockKey = generateKeyName(invocation);
        DetachLock lockAnn = invocation.getMethod().getAnnotation(DetachLock.class);

        final LockInfo<String> lockInfo = new LockInfo<>();
        lockInfo.setKey(lockKey);
        lockInfo.setValue(generateValue(lockKey));
        lockInfo.setTimeOut(lockAnn.timeout());
        lockInfo.setExpire(lockAnn.expire());
        lockInfo.setCount(0);
        long start = SystemClock.INSTANCE.currentTimeMillis();

        while ((SystemClock.INSTANCE.currentTimeMillis() - start) < lockInfo.getTimeOut()) {
            if (redisLock.lock(lockInfo)) {
                try {
                    logger.debug("lock {} - {} success!", lockKey, lockInfo.getValue());
                    return invocation.proceed();
                } catch (Exception e) {
                    throw new RedisLockExecutionException(e);
                } finally {
                    redisLock.releaseLock(lockInfo);
                }
            } else {
                lockInfo.setCount(lockInfo.getCount() + 1);
                logger.debug("try to lock {} {} ", lockInfo.getKey(), lockInfo.getCount());
            }
        }

        throw new RedisLockException("try to redis lock failed");
    }

    private String generateValue(String key) {
        return String.format("%s : %s - %s", key, key.hashCode(), UUID.randomUUID());
    }

    private String generateKeyName(MethodInvocation invocation) {
        StringBuilder key = new StringBuilder();
        Method method = invocation.getMethod();
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        key.append(PREFIX_LOCK)
                .append(IP_ADDRESS)
                .append("_")
                .append(className)
                .append("_")
                .append(methodName);
        return key.toString();
    }
}
