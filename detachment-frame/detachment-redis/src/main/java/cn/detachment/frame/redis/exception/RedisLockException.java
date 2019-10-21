package cn.detachment.frame.redis.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/20 1:32
 */
public class RedisLockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RedisLockException(Throwable cause) {
        super(cause);
    }

    public RedisLockException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public RedisLockException(String msg) {
        super(msg);
    }
}
