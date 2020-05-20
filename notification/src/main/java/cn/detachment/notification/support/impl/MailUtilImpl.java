package cn.detachment.notification.support.impl;

import cn.detachment.notification.beans.MailInfo;
import cn.detachment.notification.support.MailUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/4/15 17:36
 */
public class MailUtilImpl implements MailUtil {

    private Transport transport;

    private Session session;

    private static Logger logger = LoggerFactory.getLogger(MailUtilImpl.class);

    public MailUtilImpl(Transport transport, Session session) {
        this.transport = transport;
        this.session = session;
    }

    @Override
    public void sendMsg(MailInfo mailInfo) {
        if (transport.isConnected()) {
            try {
                MimeMessage message = this.createMimeMessage(mailInfo);
                transport.sendMessage(message, message.getAllRecipients());
            } catch (Exception e) {
                logger.error(" send email failed ,  " + e.getMessage(), e);
            }
        } else {
            logger.error(" send email failed , transport has bean closed! ");
        }
    }

    @Override
    public MimeMessage createMimeMessage(MailInfo mailInfo) throws UnsupportedEncodingException, MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        Address address = new InternetAddress(mailInfo.getFrom(), "detachment", "UTF-8");
        // 设置发送地址
        mimeMessage.setFrom(address);
        // 设置标题
        mimeMessage.setSubject(mailInfo.getSubject());
        // 设置收件人
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, getAddressFromList(mailInfo.getTo()));
        Multipart multipart = new MimeMultipart();
        // 设置内容
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
        multipart.addBodyPart(contentPart);

        // 设置附件
        if (!CollectionUtils.isEmpty(mailInfo.getAttachments())) {
            fileAttachment(mailInfo.getAttachments()).stream()
                    .forEach(part -> {
                        try {
                            multipart.addBodyPart(part);
                        } catch (MessagingException e) {
                            logger.error("add file attachment failed! " + e.getMessage(), e);
                        }
                    });
        }
        mimeMessage.setContent(multipart);
        return mimeMessage;
    }

    public List<BodyPart> fileAttachment(List<String> fp) {
        if (CollectionUtils.isEmpty(fp)) {
            return new ArrayList<>();
        }
        return fp.stream().map(File::new)
                .filter(File::exists)
                .map(file -> new DataHandler(new FileDataSource(file)))
                .map(this::filePart)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private BodyPart filePart(DataHandler dataHandler) {

        BodyPart attachmentBodyPart = new MimeBodyPart();
        try {
            attachmentBodyPart.setDataHandler(dataHandler);
            attachmentBodyPart.setFileName(dataHandler.getName());
            return attachmentBodyPart;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    private InternetAddress[] getAddressFromList(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            throw new RuntimeException("unsupported param!");
        }
        List<InternetAddress> internetAddresses = list.stream()
                .map(s -> {
                    try {
                        return new InternetAddress(s, s, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        InternetAddress[] addresses = new InternetAddress[internetAddresses.size()];
        return internetAddresses.toArray(addresses);
    }
}
