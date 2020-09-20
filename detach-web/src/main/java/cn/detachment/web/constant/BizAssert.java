package cn.detachment.web.constant;

import cn.detachment.web.bean.BizExceptionAssert;
import cn.detachment.web.bean.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
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
    BAD_ARGUMENTS(403, "bad request argument");

    int code;
    String message;

    Result<?> result;

    BizAssert(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = new Result<>(code, null, message);
    }

    BizAssert(Result<?> result) {
        this.result = result;
        this.code = result.getCode();
        this.message = result.getMessage();
    }


}
