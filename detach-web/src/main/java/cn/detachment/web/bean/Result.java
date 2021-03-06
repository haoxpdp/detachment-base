package cn.detachment.web.bean;

import cn.detachment.web.constant.ResultCode;
import lombok.Data;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/6/5 11:01
 */

@Data
@SuppressWarnings("unused")
public class Result<T> implements DetachResponse {

    private T data;

    private int code;

    private String message;

    public Result(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.message = msg;
    }


    public static <E> Result<E> success() {
        return Result.success(null);
    }

    public static <E> Result<E> instanceByCodeEnum(ResultCode code, E e) {
        return new Result<>(code.getCode(), e, code.getCodeMessage());
    }

    public static <E> Result<E> instanceByCodeEnum(ResultCode code) {
        return new Result<>(code.getCode(), null, code.getCodeMessage());
    }

    public static <E> Result<E> success(E e) {
        return new Result<>(ResultCode.SUCCESS.getCode(), e, ResultCode.SUCCESS.getCodeMessage());
    }

    public static <E> Result<E> error(String errorMessage) {
        return new Result<>(ResultCode.SERVER_ERROR.getCode(), null, ResultCode.SERVER_ERROR.getCodeMessage());
    }

    public static <E> Result<E> error(Integer code, String errorMessage) {
        return new Result<>(code, null, errorMessage);
    }


    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode().equals(getCode());
    }


}
