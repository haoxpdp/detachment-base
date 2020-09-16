package cn.detachment.web.constant;

import cn.detachment.web.bean.BusinessExceptionAssert;
import cn.detachment.web.bean.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author haoxp
 * @date 20/9/16
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum implements BusinessExceptionAssert {

    /**
     * 请求参数错误
     */
    BAD_ARGUMENTS(403, "bad request argument");

    int code;
    String message;

    Result<?> result;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
        this.result = new Result<>(code, null, message);
    }

    ResponseEnum(Result<?> result) {
        this.result = result;
        this.code = result.getCode();
        this.message = result.getMessage();
    }
}
