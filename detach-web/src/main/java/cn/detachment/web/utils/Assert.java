package cn.detachment.web.utils;

import cn.detachment.web.exception.BaseException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * @author haoxp
 * @date 20/9/16
 */
@SuppressWarnings("all")
public interface Assert {

    BaseException newException(String msg);

    BaseException newException(Throwable t, String msg);

    default void notNull(Object object) {
        notNull(object, "param mustn't be null");
    }

    default void notNull(Object obj, String msg) {
        if (obj == null) {
            throw newException(msg);
        }
    }

    default void notEmpty(Collection<?> collection) {
        notEmpty(collection, "collection param mustn't be empty");
    }

    default void notEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw newException(msg);
        }
    }

    default void isBlank(String string) {
        isBlank(string, "param mustn't be blamk");
    }

    default void isBlank(String str, String msg) {
        if (StringUtils.isEmpty(str.trim())) {
            throw newException(msg);
        }
    }

    default void isEmpty(String string, String msg) {
        if (StringUtils.isEmpty(string)) {
            throw newException(msg);
        }
    }

    default void isEmpty(String string) {
        isEmpty(string, "param is empty");
    }


    default void hasText(String text) {
        hasText(text, "param must have text!");
    }

    default void hasText(String text, String msg) {
        if (StringUtils.hasText(text)) {
            throw newException(msg);
        }
    }

    default void condition(boolean expression, String msg) {
        if (expression) {
            throw newException(msg);
        }
    }

}
