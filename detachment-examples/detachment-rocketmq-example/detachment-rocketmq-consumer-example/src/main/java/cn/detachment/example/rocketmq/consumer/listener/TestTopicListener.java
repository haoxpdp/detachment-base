package cn.detachment.example.rocketmq.consumer.listener;

import cn.detachment.frame.rocketmq.annotation.RocketMQMessageListener;
import cn.detachment.frame.rocketmq.core.RocketmqListener;
import org.springframework.stereotype.Service;


/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 17:41
 */

@Service
@RocketMQMessageListener(topic = "topicTest", consumerGroup = "rocketmq-consume-demo")
public class TestTopicListener implements RocketmqListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("topicTest receive msg --> " + message);
    }
}
