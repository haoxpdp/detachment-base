package cn.detach.api.http;

import lombok.*;

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
public class RemoteRequest {

    private String protocol;

    private String url;

    private boolean async;

    private String encode = "utf-8";

    private String responseEncode = "utf-8";

    private Integer timeout = 3000;

    private Integer retryCount = 0;

    private Map<String, String> header;

    private Map<String, Object> formData;

    private Object requestBody;


}
