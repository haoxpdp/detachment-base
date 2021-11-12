package cn.detach.tools.download.exception;

/**
 * @author haoxp
 */
public class TaskInitException extends RuntimeException {

    public TaskInitException(String msg, Throwable e) {
        super(msg, e);
    }

    public TaskInitException(String info) {
        super(info);
    }

}
