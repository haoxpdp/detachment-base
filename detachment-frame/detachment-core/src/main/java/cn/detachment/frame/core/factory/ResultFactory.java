package cn.detachment.frame.core.factory;

import cn.detachment.frame.core.bean.Result;
import cn.detachment.frame.core.constant.HttpCode;

/**
 * @author haoxpdp
 * @date 19/7/5 18:03
 */
public class ResultFactory {
    public static <T> Result<T> buildSuccess(T t) {
        return buildSuccess(t, null);
    }

    public static <T> Result<T> buildSuccess(T t, String msg) {
        return new Result<T>(HttpCode.SUCCESS, t, msg);
    }

    public static <T> Result<T> serverError(String msg) {
        return new Result<>(HttpCode.SERVER_ERROR, msg);
    }

    public static Result buildSuccess() {
        return buildSuccess(null);
    }

    public static Result buildError(int errorCode, String msg) {
        return buildError(errorCode, null, msg);
    }

    public static <T> Result<T> buildError(int errCode, T t, String msg) {
        return new Result<>(errCode, t, msg);
    }

    public static <T> Result<T> paramError(T t, String msg) {
        return buildError(HttpCode.PARAM_ERROR, t, msg);
    }
}
