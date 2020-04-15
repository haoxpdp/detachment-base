package cn.detachment.example.redis;

import cn.detachment.example.redis.service.LockTestService;
import cn.detachment.example.redis.service.RedisTestService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
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
    private RedisTestService redisService;

    @Resource
    private RedissonClient redissonClient;

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

    @Test
    public void testBloomFilter() {
//        RClusteredBloomFilter<String> bloomFilter = redissonClient.getClusteredBloomFilter("sample");
//// 采用以下参数创建布隆过滤器
//// expectedInsertions = 255000000
//// falseProbability = 0.03
//        bloomFilter.tryInit(255000000L, 0.03);
//        bloomFilter.add(new SomeObject("field1Value", "field2Value"));
//        bloomFilter.add(new SomeObject("field5Value", "field8Value"));
//        bloomFilter.contains(new SomeObject("field1Value", "field8Value"));

        RBloomFilter<String> rBloomFilter = redissonClient.getBloomFilter("smaple");
        if (rBloomFilter.tryInit(255000000L, 0.03)){
            rBloomFilter.add("www.haoxpdp.com");
            rBloomFilter.add("www.detachment.cn");
            logger.info("containes {} ",rBloomFilter.contains("www.haoxpdp.com"));
        }

        logger.info("container detachment {}",redissonClient.getBloomFilter("smaple").contains("www.detachment.cn"));
    }

    @Test
    public void testExist(){
        String index = "lijingrui_html_bloom_key";
        String val = "https://www.sustech.edu.cn/?page_id=363&amp;lang=zh";
        RBloomFilter<String> rBloomFilter = redissonClient.getBloomFilter(index);
        logger.info("exists : {}",rBloomFilter.contains(val));

    }
}
