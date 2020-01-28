package cn.detachment.frame.core.exception;


/**
 * @author haoxp
 * @version v1.0
 * @date 20/1/28 16:57
 */
public class UnsupportedSSLVersionException extends RuntimeException {

    public UnsupportedSSLVersionException() {
        super();
    }

    public UnsupportedSSLVersionException(String msg) {
        super(msg);
    }

}
