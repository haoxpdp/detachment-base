package cn.detachment.es.exception;

/**
 * @author haoxp
 * @date 21/3/8
 */
public class ReflectionException extends RuntimeException{

    public ReflectionException() {
        super();
    }

    public ReflectionException(String message) {
        super(message);
    }

    public ReflectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectionException(Throwable cause) {
        super(cause);
    }

}
