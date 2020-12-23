package cn.detachment.redis.lock;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class Lock {

    public String lock(String key, Long expire) {
        String val = UUID.randomUUID().toString();
        return lock(key, val, expire, TimeUnit.SECONDS);
    }

    public String lock(String key, String val, Long expire, TimeUnit timeUnit) {
        System.out.println("lock .." + val);
        return val;
    }


    public void release(String key, String val) {
        System.out.println("release ..");
    }

}
