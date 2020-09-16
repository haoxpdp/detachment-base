package cn.detachment.web.Annoation;

import cn.detachment.web.exception.BaseException;

/**
 * @author haoxp
 * @date 20/9/16
 */
public interface AssertInterface {

    /**
     * new exception
     *
     * @param args
     * @return
     */
    BaseException newException(Object... args);

    /**
     * new exception
     *
     * @param t
     * @param args
     * @return
     */
    BaseException newException(Throwable t, Object... args);

    /**
     * not null
     *
     * @param obj
     */
    default void assertNotNull(Object obj) {
        if (obj == null) {
            throw newException(obj);
        }
    }

    default void assertNotNull(Object obj, Object... args) {
        if (obj == null) {
            throw newException(args);
        }
    }
}
