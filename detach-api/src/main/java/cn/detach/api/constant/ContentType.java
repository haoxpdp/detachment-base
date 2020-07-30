package cn.detach.api.constant;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 15:51
 */
@SuppressWarnings("unused")
public enum ContentType {
    /**
     * json
     */
    JSON("application/json"),
    /**
     * form data
     */
    FORM_DATA("multipart/form-data"),

    TEXT("text/plain"),

    FORM_URLENCODED("application/x-www-form-urlencoded");


    private final String enctype;

    ContentType(String enctype) {
        this.enctype = enctype;
    }

    public String getEnctype() {
        return enctype;
    }
}
