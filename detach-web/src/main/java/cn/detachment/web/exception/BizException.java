package cn.detachment.web.exception;

/**
 * @author haoxp
 * @date 20/9/20
 */
public class BizException extends BaseException {

    public BizException(String msg) {
        super(msg);
    }

    public BizException(int code, String msg) {
        super(code, msg);
    }

    public BizException(Throwable e, String msg) {
        super(e, msg);
    }

    public BizException(Throwable e, int code, String msg) {
        super(e, code, msg);
    }
}
