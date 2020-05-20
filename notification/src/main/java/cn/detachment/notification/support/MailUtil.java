package cn.detachment.notification.support;


import cn.detachment.notification.beans.MailInfo;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/4/15 17:22
 */
public interface MailUtil {

    /**
     * sendMsg
     *
     * @param mailInfo mailInfo
     * @return void
     * @author haoxp
     * @date 20/4/15 17:38
     */
    void sendMsg(MailInfo mailInfo);

    /**
     * createMimeMessage
     *
     * @param mailInfo mailInfo
     * @return javax.mail.internet.MimeMessage
     * @author haoxp
     * @date 20/4/15 17:38
     */
    MimeMessage createMimeMessage(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException;
}
