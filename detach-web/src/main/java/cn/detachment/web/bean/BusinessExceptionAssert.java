package cn.detachment.web.bean;

import cn.detachment.web.Annoation.AssertInterface;
import cn.detachment.web.exception.BaseException;

/**
 * @author haoxp
 * @date 20/9/16
 */
public interface BusinessExceptionAssert extends DetachResponse, AssertInterface {

    @Override
    default BaseException newException(Object... args) {
        return new BaseException(args);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        return null;
    }
}
