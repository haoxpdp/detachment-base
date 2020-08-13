package cn.detachment.core.constant;

import lombok.Getter;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/6/5 11:13
 */
public enum ResultCode {

    /**
     *
     */
    SUCCESS(200, "success!"), SERVER_ERROR(500, "server_error!");

    @Getter
    private final Integer code;

    @Getter
    private final String codeMessage;

    ResultCode(Integer code, String codeMessage) {
        this.code = code;
        this.codeMessage = codeMessage;
    }
}
