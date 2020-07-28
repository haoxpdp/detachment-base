package cn.detach.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detach.example.api.DemoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/6/6 0:48
 */
@SpringBootApplication
@RemoteApiScanner(scanClasses = DemoApi.class)
public class App implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Resource
    private DemoApi demoApi;

    @Override
    public void run(String... args) throws Exception {
        HashMap<String, String> header = new HashMap<>();
        header.put("token", "12333");
        System.out.println(demoApi.getTest(header, "haoxp"));
    }

}
