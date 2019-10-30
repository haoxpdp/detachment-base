package cn.detachment.example.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/30 11:13
 */
@SpringBootApplication
public class ProducerApp implements CommandLineRunner {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public static void main(String[] args) {
        SpringApplication.run(ProducerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        SendResult result = rocketMQTemplate.syncSend("topicTest","hello world");
        System.out.println(String.format("%s --- %s",result.getSendStatus(),result.getMsgId()));
    }
}
