package cn.detachment.example.notification;

import cn.detachment.frame.notification.beans.MailInfo;
import cn.detachment.frame.notification.support.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

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
        MailInfo mailInfo = new MailInfo();
        mailInfo.addReceiver("haoxpdp@outlook.com");
        mailInfo.setContent("test");
        mailInfo.setFrom("haoxpdp@qq.com");
        mailInfo.setSubject("test");
        mailUtil.sendMsg(mailInfo);
    }
}
