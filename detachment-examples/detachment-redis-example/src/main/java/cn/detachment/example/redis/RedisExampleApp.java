package cn.detachment.example.redis;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/21 22:28
 */
@SpringBootApplication
public class RedisExampleApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApp.class);
    }

    private static final String LOCK_KEY = "lock";

    @Override
    public void run(String... args) throws Exception {

    }
}
