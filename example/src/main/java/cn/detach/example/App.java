package cn.detach.example;

import cn.detach.api.annoation.RemoteApiScanner;
import cn.detach.example.api.AuthParam;
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
    public void run(String... args) {

        HashMap<String, String> header = new HashMap<>();
        header.put("company", "zichan360Test");
        header.put("key", "8sxf0ZRAzdFyTbXP3H");

        AuthParam authParam = new AuthParam("zichan360Test", "8sxf0ZRAzdFyTbXP3H");

        try {

            System.out.println(demoApi.getTest(authParam));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
