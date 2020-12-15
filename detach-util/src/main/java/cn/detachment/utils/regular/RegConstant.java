package cn.detachment.utils.regular;

import java.util.regex.Pattern;

/**
 * 常用正则匹配
 *
 * @author haoxp
 */
@SuppressWarnings("all")
public interface RegConstant {

    /**
     * 中文正则
     */
    String CHINESE_CHAR_REGEX = "[\u4E00-\u9FA5|\\！|\\,|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】\\、]+";

    Pattern CHINESE_CHAR_PATTERN = Pattern.compile(CHINESE_CHAR_REGEX);

}
