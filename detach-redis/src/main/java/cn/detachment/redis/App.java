package cn.detachment.redis;

import cn.detachment.redis.test.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * @author haoxp
 * @date 20/12/22
 */
@SpringBootApplication
@EnableRetry
public class App implements CommandLineRunner {

    static Integer count = 0;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Resource
    private Test test;

    @Override
    public void run(String... args) throws Exception {
        test.tests();
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(8);
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMaxWaitMillis(8 * 1000);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "192.168.137.128", 6379, 30000,"940330");
        Jedis jedis =  jedisPool.getResource();
        SetParams setParams = SetParams.setParams().nx().ex(15);
        String test_key = jedis.set("test_key", String.valueOf(555), setParams);
        System.out.println(test_key);
    }

}
