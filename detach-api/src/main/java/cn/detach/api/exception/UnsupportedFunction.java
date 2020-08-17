package cn.detach.api.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/8/4 22:49
 */
public class UnsupportedFunction extends RuntimeException {

    public UnsupportedFunction(String msg) {
        super(msg);
    }

    public UnsupportedFunction(String msg, Throwable e) {
        super(msg, e);
    }

}
