package cn.detachment.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detachment.example.api.TestApi;
import cn.detachment.example.service.TestService;
import cn.detachment.web.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Resource
    private TestApi testApi;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(testApi.baidu("https://www.baidu.com"));
    }

    @Resource
    private TestService testService;

    @GetMapping("/test")
    public Result<?> test() {
        logger.info("test!!!!!!!!!!!!!!!!");
        testService.test();
        return Result.success();
    }
}
