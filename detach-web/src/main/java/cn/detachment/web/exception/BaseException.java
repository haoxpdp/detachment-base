package cn.detachment.web.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haoxp
 * @date 20/9/16
 */
public class BaseException extends RuntimeException {

    @Getter
    @Setter
    private int code;

    public BaseException(String msg) {
        this(500, msg);
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.code = code;
    }


    public BaseException(Throwable e, String msg) {
        this(e, 500, msg);
    }

    public BaseException(Throwable e, int code, String msg) {
        super(msg, e);
        this.code = code;
    }
}
