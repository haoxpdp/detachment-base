package cn.detachment.example.redis;

import cn.detachment.example.redis.service.LockTestService;
import cn.detachment.example.redis.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
}
