package cn.detachment.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detachment.example.api.TestApi;
import cn.detachment.web.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @date 20/9/16
 */
@SpringBootApplication
@RemoteApiScanner(scanClasses = TestApi.class)
@RestController
public class DetachExample implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DetachExample.class);

    public static void main(String[] args) {
        SpringApplication.run(DetachExample.class, args);
    }


    @Value("${spring.profiles.active:}")
    String active;

    @Resource
    private TestApi testApi;

    @GetMapping("/test")
    public Result<?> test() {
        logger.info("test");
        return Result.success();
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("{}", 123);
    }

}
