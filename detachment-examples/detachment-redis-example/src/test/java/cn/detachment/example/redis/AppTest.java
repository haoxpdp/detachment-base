package cn.detachment.example.redis;

import cn.detachment.example.redis.service.LockTestService;
import cn.detachment.example.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 22:36
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AppTest {

    private static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Resource
    private LockTestService lockTestService;

    @Resource
    private RedisService redisService;

    @Test
    public void testLock() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> lockTestService.syncPlusNano());
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(1000);
        }
        logger.info("result {}", lockTestService.getNano());
    }

    @Test
    public void testRedis() {
        redisService.testRedisSet();
        logger.info(redisService.testRedisGetAndRemove());
        logger.info("has key {} ", redisService.testHasKey());
    }

    private static Integer nano = 0;

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedissonLock() throws InterruptedException {
        String lockN = "testLock";
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10000; i++) {
            executorService.execute(() -> {
                RLock rLock = redissonClient.getLock(lockN);
                try {
                    boolean res = rLock.tryLock(100,5, TimeUnit.SECONDS);
                    if (res){
                        nano ++;
                        rLock.unlock();
                    }else {
                        logger.error("加锁失败！");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            Thread.sleep(1000);
        }
        logger.info("nano {} ",nano);
    }
}
