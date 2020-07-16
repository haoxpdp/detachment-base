package cn.detachment.es.exception;

/**
 * @author haoxp
 */
public class DesSearchException extends RuntimeException {

    public DesSearchException(String msg, Throwable t) {
        super(msg, t);
    }

    public DesSearchException(String msg) {
        super(msg);
    }
}
