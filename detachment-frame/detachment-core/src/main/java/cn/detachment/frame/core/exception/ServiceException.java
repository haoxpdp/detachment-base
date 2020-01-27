package cn.detachment.frame.core.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/9/15 22:40
 */
import cn.detachment.frame.core.bean.Result;
import cn.detachment.frame.core.constant.HttpCode;
import lombok.Getter;
import lombok.Setter;

public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code;

    public ServiceException(Result bean) {
        super(bean.getMsg());
        this.code = bean.getCode();
    }

    public ServiceException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(String msg, Throwable e) {
        super(msg, e);
        this.code = 500;
    }

    public ServiceException(int code, String message, Throwable exception) {
        super(message, exception);
        this.code = code;
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = HttpCode.SERVER_ERROR;
    }

    public static ServiceException UnknownException(String message, Throwable cause) {
        return new ServiceException(HttpCode.SERVER_ERROR, message, cause);
    }

    public static ServiceException BadRequsetException(String message, Throwable cause) {
        return new ServiceException(HttpCode.BAD_REQUEST, message, cause);
    }

    public int getCode() {
        return code;
    }

    public ServiceException setCode(int code) {
        this.code = code;
        return this;
    }

}