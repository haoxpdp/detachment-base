package cn.detachment.notification.config;

import cn.detachment.notification.support.MailUtil;
import cn.detachment.notification.support.impl.MailUtilImpl;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import javax.mail.Transport;
import java.util.Properties;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/4/15 17:28
 */
@ConfigurationProperties(prefix = "detachment.mail")
@Getter
@Setter
@Configuration
public class MailConfiguration {

    private static Logger logger = LoggerFactory.getLogger(MailConfiguration.class);

    /**
     * 发送源
     */
    private String account;
    /**
     * 密码
     */
    private String password;

    /**
     * smtp服务器地址
     */
    private String host;

    /**
     * port
     */
    private String port;

    private boolean debug;

    @Bean
    public Session mailSession() {
        Properties properties = new Properties();
        //发送邮箱服务器
        properties.setProperty("mail.smtp.host", host);
        //发送端口
        properties.setProperty("mail.smtp.port", port);
        //是否开启权限控制
        properties.setProperty("mail.smtp.auth", "true");
        //true 打印信息到控制台
        properties.setProperty("mail.debug", String.valueOf(debug));
        //发送的协议是简单的邮件传输协议
        properties.setProperty("mail.transport", "smtp");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        Session session = Session.getInstance(properties);
        //开启日志
        session.setDebug(debug);
        return session;
    }

    @Bean(destroyMethod = "close")
    @ConditionalOnBean(Session.class)
    public Transport mailTransport(Session session) {
        try {
            Transport transport = session.getTransport();
            //密码以授权码的形式体现
            transport.connect(account, password);
            return transport;
        } catch (Exception e) {
            logger.error("create mail transport error! " + e.getMessage(), e);
        }
        return null;
    }

    @Bean
    @ConditionalOnBean(value = {Transport.class, Session.class})
    public MailUtil mailUtil(Transport mailTransport, Session mailSession) {
        return new MailUtilImpl(mailTransport, mailSession);
    }

}
