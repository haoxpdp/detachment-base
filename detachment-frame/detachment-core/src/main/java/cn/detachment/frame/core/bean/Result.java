package cn.detachment.frame.core.bean;

import lombok.Data;

/**
 * @author haoxpdp
 * @date 19/7/5 0:40
 */
@Data
public class Result<T> {

    int code;
    String msg;
    T data;

    public Result() {

    }

    public Result(int code, T t, String msg) {
        this.data = t;
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
