package cn.detachment.redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class RedisLock {

    private Jedis jedis;

    public String lock(String key, Long expire) {
        String val = UUID.randomUUID().toString();
        SetParams setParams = new SetParams();
        setParams.nx().ex(expire.intValue());
        jedis.set(key, val, setParams);
        return lock(key, val, expire, TimeUnit.SECONDS);
    }

    public String lock(String key, String val, Long expire, TimeUnit timeUnit) {
        System.out.println("lock .." + val);
        return val;
    }


    public void release(String key, String val) {
        System.out.println("release ..");
    }

    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
