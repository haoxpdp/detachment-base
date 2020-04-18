package cn.detachment.example.notification;

import cn.detachment.frame.notification.beans.MailInfo;
import cn.detachment.frame.notification.support.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/4/15 18:31
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }


    @Resource
    private MailUtil mailUtil;

    @Override
    public void run(String... args) throws Exception {

        for (int i = 0; i < 3; i++) {
            String path = "H:\\Desktop\\tmp_922275\\" + i;
            System.out.println(path);
            try (FileReader reader = new FileReader(path)) {
                char[] cbuf = new char[1024 * 10];
                int len = -1;
                StringBuilder s = new StringBuilder();
                while ((len = reader.read(cbuf)) != -1) {
                    s.append(cbuf, 0, len);
                    cbuf = new char[1024 * 10];
                }
                System.out.println();
                MailInfo mailInfo = new MailInfo();
                mailInfo.addReceiver("haoxpdp@outlook.com");
                mailInfo.setSubject("test " + i);
                mailInfo.setFrom("haoxpdp@qq.com");
                mailInfo.setContent(s.toString());
                mailUtil.sendMsg(mailInfo);
            }
        }
    }
}
