package cn.detachment.web.exception;

/**
 * @author haoxp
 * @date 20/9/16
 */
public class BaseException extends RuntimeException {

    public BaseException(Object... args) {
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Throwable e, String msg) {
        super(msg, e);
    }
}
