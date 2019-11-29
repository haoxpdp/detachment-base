package cn.detachment.example.web;

import cn.detachment.example.web.service.TestService;
import cn.detachment.frame.core.bean.Result;
import cn.detachment.frame.core.factory.ResultFactory;
import cn.detachment.frame.core.util.TraceIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.*;

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

    public static final String THREAD_POOL = "ThreadTraceID";

    @Resource
    private TestService testService;

    @Bean
    public ExecutorService pool(){
        return new ThreadPoolExecutor(10, 10, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
    }

    @Resource
    private ExecutorService pool;

    @RequestMapping("/test")
    public Result testApi() {
        logger.info("test");

        testService.testLog();



        for (int i = 0; i < 10; i++) {
            final int tmp = i;
            pool.execute(() -> {
                MDC.put(THREAD_POOL, Long.toString(TraceIDUtil.incrementAndGet()));

                Logger logger1 = LoggerFactory.getLogger(App.class);
                logger1.info("{} --> {}", Thread.currentThread(), tmp);
                MDC.remove(THREAD_POOL);
            });
        }
        return ResultFactory.buildSuccess();
    }

}
