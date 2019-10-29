package cn.detachment.frame.rocketmq.config;

import lombok.Data;
import org.apache.rocketmq.common.MixAll;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/29 14:32
 */
@Data
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {
    /**
     * The name server for rocketMQ, formats: `host:port;host:port`.
     */
    private String nameServer;

    /**
     * Enum type for accesChannel, values: LOCAL, CLOUD
     */
    private String accessChannel;

    private Producer producer;

    /**
     * Configure enable listener or not.
     * In some particular cases, if you don't want the the listener is enabled when container startup,
     * the configuration pattern is like this :
     * rocketmq.consumer.listeners.<group-name>.<topic-name>.enabled=<boolean value, true or false>
     * <p>
     * the listener is enabled by default.
     */
    private Consumer consumer = new Consumer();

    public static class Producer {

        /**
         * Group name of producer.
         */
        private String group;

        /**
         * Millis of send message timeout.
         */
        private int sendMessageTimeout = 3000;

        /**
         * Compress message body threshold, namely, message body larger than 4k will be compressed on default.
         */
        private int compressMessageBodyThreshold = 1024 * 4;

        /**
         * Maximum number of retry to perform internally before claiming sending failure in synchronous mode.
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendFailed = 2;

        /**
         * <p> Maximum number of retry to perform internally before claiming sending failure in asynchronous mode. </p>
         * This may potentially cause message duplication which is up to application developers to resolve.
         */
        private int retryTimesWhenSendAsyncFailed = 2;

        /**
         * Indicate whether to retry another broker on sending failure internally.
         */
        private boolean retryNextServer = false;

        /**
         * Maximum allowed message size in bytes.
         */
        private int maxMessageSize = 1024 * 1024 * 4;

        /**
         * The property of "access-key".
         */
        private String accessKey;

        /**
         * The property of "secret-key".
         */
        private String secretKey;

        /**
         * Switch flag instance for message trace.
         */
        private boolean enableMsgTrace = true;

        /**
         * The name value of message trace topic.If you don't config,you can use the default trace topic name.
         */
        private String customizedTraceTopic = MixAll.RMQ_SYS_TRACE_TOPIC;

    }

    @Data
    public static final class Consumer {
        /**
         * listener configuration container
         * the pattern is like this:
         * group1.topic1 = false
         * group2.topic2 = true
         * group3.topic3 = false
         */
        private Map<String, Map<String, Boolean>> listeners = new HashMap<>();

        public Map<String, Map<String, Boolean>> getListeners() {
            return listeners;
        }

        public void setListeners(Map<String, Map<String, Boolean>> listeners) {
            this.listeners = listeners;
        }
    }

}
