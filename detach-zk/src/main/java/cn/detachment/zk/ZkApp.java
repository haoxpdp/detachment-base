package cn.detachment.zk;

import cn.detachment.zk.executor.ZkExecutor;
import org.apache.zookeeper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/20 21:38
 */
@SpringBootApplication
public class ZkApp implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(ZkApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ZkApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String[] exec = "line1,line2".split(", ");

        new ZkExecutor("localhost:2181", "/t1", "t1c", exec).run();

    }
}
