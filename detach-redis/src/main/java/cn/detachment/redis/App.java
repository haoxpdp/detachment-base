package cn.detachment.redis;

import cn.detachment.redis.test.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

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
        test.A();
    }

}
