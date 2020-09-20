package cn.detachment.web.bean;

import cn.detachment.web.utils.Assert;
import cn.detachment.web.exception.BaseException;

/**
 * @author haoxp
 * @date 20/9/16
 */
public interface BizExceptionAssert extends DetachResponse, Assert {

    @Override
    default BaseException newException(String msg) {
        return new BaseException(msg);
    }

    @Override
    default BaseException newException(Throwable t, String msg) {
        return new BaseException(t, msg);
    }
}
