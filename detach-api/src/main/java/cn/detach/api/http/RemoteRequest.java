package cn.detach.api.http;

import cn.detach.api.constant.HttpMethod;
import lombok.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/29 18:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuppressWarnings("unused")
public class RemoteRequest {

    public static final int DEFAULT_TIMEOUT = 3000;

    public static final int DEFAULT_CONNECTION_TIME = 3000;

    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private String protocol;

    private String url;

    private boolean async;

    private HttpMethod httpMethod;

    private Charset encode = DEFAULT_CHARSET;

    private Charset responseCharset = DEFAULT_CHARSET;

    private Integer socketTimeout = DEFAULT_TIMEOUT;

    private Integer connectTimeout = DEFAULT_CONNECTION_TIME;

    private Integer retryCount = 0;

    private Map<String, String> header;

    private Map<String, Object> formData;

    private Map<String, Object> urlParams;

    private String bodyType;

    private Object requestBody;


    public void addFormData(String name, Object val) {
        if (formData == null) {
            formData = new HashMap<>();
        }
        formData.put(name, val);
    }

}
