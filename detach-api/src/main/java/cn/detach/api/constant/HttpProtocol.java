package cn.detach.api.constant;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 18:39
 */
@SuppressWarnings("unused")
public enum HttpProtocol {
    /**
     * http protocol
     */
    HTTP("http"), HTTPS("https");
    String protocol;

    HttpProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getVal(){
        return protocol;
    }

}
