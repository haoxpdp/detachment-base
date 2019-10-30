package cn.detachment.example.rocketmq.consumer.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/30 14:36
 */
@Service
@RocketMQMessageListener(topic = "topicTest", consumerGroup = "rocketmq-consume-demo")
public class AnoListener implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println(String.format("AnoListener receive : %s", message));
    }
}
