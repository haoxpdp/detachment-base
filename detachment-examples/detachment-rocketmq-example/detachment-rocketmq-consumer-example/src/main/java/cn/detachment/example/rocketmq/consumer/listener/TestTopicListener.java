package cn.detachment.example.rocketmq.consumer.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;


/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 17:41
 */

@Service
@RocketMQMessageListener(topic = "topicTest", consumerGroup = "rocketmq-consume-demo")
public class TestTopicListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("topicTest receive msg --> " + message);
    }
}