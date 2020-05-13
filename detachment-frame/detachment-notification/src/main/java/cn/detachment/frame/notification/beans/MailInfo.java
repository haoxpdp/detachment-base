package cn.detachment.frame.notification.beans;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/4/15 16:37
 */
@Data
public class MailInfo {

    /**
     * 标题
     */
    private String subject;

    /**
     * 内容
     */
    private String content;

    /**
     * 附件列表
     */
    private List<String> attachments;

    /**
     * 收件人列表
     */
    private List<String> to;

    /**
     * from
     */
    private String from;

    public void addReceiver(String receiver) {
        if (null == to) {
            to = new ArrayList<>();
        }
        to.add(receiver);
    }

    public void addAttachment(String path) {
        if (attachments == null) {
            attachments = new ArrayList<>();
        }
        attachments.add(path);
    }

}
