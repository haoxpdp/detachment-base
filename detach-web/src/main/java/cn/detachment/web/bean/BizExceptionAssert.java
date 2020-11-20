package cn.detachment.web.bean;

import cn.detachment.web.exception.BizException;
import cn.detachment.web.utils.Assert;
import cn.detachment.web.exception.ServiceException;
import org.springframework.util.StringUtils;

/**
 * @author haoxp
 * @date 20/9/16
 */
public interface BizExceptionAssert extends DetachResponse, Assert {

    /**
     * newException
     *
     * @param msg msg
     * @return exception
     */
    @Override
    default ServiceException newException(String msg) {
        return new BizException(getCode(), StringUtils.isEmpty(msg) ? getMessage() : msg);
    }

    /**
     * newException
     *
     * @param t t
     * @param msg msg
     * @return exception
     */
    @Override
    default ServiceException newException(Throwable t, String msg) {
        return new BizException(t, getCode(), StringUtils.isEmpty(msg) ? getMessage() : msg);
    }
}
