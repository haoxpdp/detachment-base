package cn.detachment.frame.core.http;

import javax.net.ssl.SSLException;

/**
 * SSLSocketFactoryCreateException
 *
 * @author haoxp
 * @version v1.0
 * @date 20/1/28 17:11
 */
public class SSLFCreateException extends RuntimeException {
    public SSLFCreateException(String msg) {
        super(msg);
    }
    public SSLFCreateException(Throwable e) {
        super(e);
    }
}
