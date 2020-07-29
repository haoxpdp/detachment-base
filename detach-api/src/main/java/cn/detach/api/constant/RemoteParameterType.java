package cn.detach.api.constant;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 11:38
 */
public enum RemoteParameterType {

    /**
     * url string, don't need replace.
     */
    STRING,

    /**
     * need replace with args, specified the value name.
     */
    REMOTE_PARAMETER,

    /**
     * url parameter, need replace with args.
     */
    PARAMETER;

}
