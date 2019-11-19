package cn.detachment.example.web;

import cn.detachment.frame.core.bean.Result;
import cn.detachment.frame.core.factory.ResultFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/11/20 0:09
 */
@SpringBootApplication
@RestController
@RequestMapping("/")
public class App {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RequestMapping("/test")
    public Result testApi() {
        logger.info("test");
        return ResultFactory.buildSuccess();
    }

}
