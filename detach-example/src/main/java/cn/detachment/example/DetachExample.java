package cn.detachment.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detachment.example.api.TestApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @date 20/9/16
 */
@SpringBootApplication
@RemoteApiScanner(scanClasses = TestApi.class)
public class DetachExample implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DetachExample.class);

    public static void main(String[] args) {
        SpringApplication.run(DetachExample.class, args);
    }

    @Resource
    private TestApi testApi;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(testApi.baidu("https://www.baidu.com"));
    }
}
