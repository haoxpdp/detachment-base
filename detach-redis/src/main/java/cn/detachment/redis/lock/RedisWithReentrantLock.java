package cn.detachment.redis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.Map;

/**
 * @author haoxp
 * @date 20/12/28
 */
public class RedisWithReentrantLock {
    private ThreadLocal<Map<String, String>> lockers = new ThreadLocal<>();

    private Jedis jedis;

    public RedisWithReentrantLock(Jedis jedis) {
        this.jedis = jedis;
    }

    public RedisWithReentrantLock() {

    }


    public Jedis getJedis() {
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
