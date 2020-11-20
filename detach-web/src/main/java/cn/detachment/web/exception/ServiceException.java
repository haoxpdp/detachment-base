package cn.detachment.web.exception;

import cn.detachment.web.bean.Result;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haoxp
 * @date 20/9/16
 */
public class ServiceException extends RuntimeException {

    @Getter
    @Setter
    private int code;

    public ServiceException(String msg) {
        this(500, msg);
    }

    public ServiceException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ServiceException(Result<?> result) {
        this(result.getCode(), result.getMessage());
    }

    public ServiceException(Throwable e, String msg) {
        this(e, 500, msg);
    }

    public ServiceException(Throwable e, int code, String msg) {
        super(msg, e);
        this.code = code;
    }
}
