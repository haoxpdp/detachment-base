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
        notNull(object, null);
    }

    default void notNull(Object obj, String msg) {
        if (obj == null) {
            throw newException(msg);
        }
    }

    default void notEmpty(Collection<?> collection) {
        notEmpty(collection, null);
    }

    default void notEmpty(Collection<?> collection, String msg) {
        if (CollectionUtils.isEmpty(collection)) {
            throw newException(msg);
        }
    }

    default void isBlank(String string) {
        isBlank(string, null);
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
        isEmpty(string, null);
    }


    default void hasText(String text) {
        hasText(text, null);
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
