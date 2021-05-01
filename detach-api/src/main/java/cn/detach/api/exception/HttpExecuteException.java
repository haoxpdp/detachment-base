package cn.detach.api.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/8/4 22:49
 */
@SuppressWarnings("unused")
public class HttpExecuteException extends RuntimeException {

    public HttpExecuteException(String msg) {
        super(msg);
    }

    public HttpExecuteException(String msg, Throwable e) {
        super(msg, e);
    }

}
