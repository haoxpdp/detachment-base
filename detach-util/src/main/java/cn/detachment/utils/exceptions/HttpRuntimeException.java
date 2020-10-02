package cn.detachment.utils.exceptions;

/**
 * @author haoxp
 * @date 20/10/2
 */
public class HttpRuntimeException extends RuntimeException {

    public HttpRuntimeException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
