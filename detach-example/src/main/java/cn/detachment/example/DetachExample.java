package cn.detachment.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detach.api.support.HttpUtilApi;
import cn.detachment.example.api.TestApi;
import cn.detachment.example.beans.db.DUser;
import cn.detachment.example.dao.DUserMapper;
import cn.detachment.web.bean.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

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


    @Value("${spring.profiles.active:}")
    String active;

    @Resource
    private TestApi testApi;

    @Resource
    private DUserMapper userMapper;

    @GetMapping("/test")
    public Result<?> test() {
        logger.info("test");
        return Result.success();
    }

    @PostMapping("/uploadFile")
    public Result<?> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        File f = new File("H:\\Desktop\\tmp");
        file.transferTo(f);

        return Result.success();
    }

    @Resource
    private HttpUtilApi httpUtilApi;

    @Override
    public void run(String... args) throws Exception {
//        System.out.println(testApi.testIpSb());
        DUser dUser = userMapper.selectById(1);
        System.out.println(dUser);
    }

}
