package cn.detachment.frame.redis.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/20 1:33
 */
public class RedisLockExecutionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RedisLockExecutionException(Throwable cause) {
        super(cause);
    }

    public RedisLockExecutionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisLockExecutionException(String msg) {
        super(msg);
    }

}
