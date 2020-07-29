package cn.detach.api.exception;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 15:45
 */
public class UrlBuildException extends RuntimeException {

    public UrlBuildException(Throwable e, String msg) {
        super(msg, e);
    }

}
