package cn.detachment.es.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/5/30 23:32
 */
public class ScanClassException extends RuntimeException {

    public ScanClassException(String msg, Throwable e) {
        super(msg, e);
    }

    public ScanClassException(String msg) {
        super(msg);
    }

    public ScanClassException(Throwable e) {
        super(e);
    }

}
