package cn.detachment.example.rocketmq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 17:38
 */
@SpringBootApplication
public class ConsumerApp {

    private static Logger logger = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApp.class, args);
    }

}
