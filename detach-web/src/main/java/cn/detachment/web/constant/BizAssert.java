package cn.detachment.web.constant;

import cn.detachment.web.bean.BizExceptionAssert;
import cn.detachment.web.bean.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务断言异常
 *
 * @author haoxp
 * @date 20/9/16
 */
@Getter
@AllArgsConstructor
public enum BizAssert implements BizExceptionAssert {

    /**
     * 请求参数错误
     */
    BAD_ARGUMENTS(ResultCode.BAD_ARGUMENT);

    int code;
    String message;

    BizAssert(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getCodeMessage();
    }

}
