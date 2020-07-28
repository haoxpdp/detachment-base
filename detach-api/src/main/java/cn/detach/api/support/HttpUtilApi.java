package cn.detach.api.support;

import java.util.Map;

/**
 * @author haoxp
 */
public interface HttpUtilApi {

    /**
     * get
     *
     * @param url url
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/27 16:09
     */
    String get(String url);

    /**
     * post
     *
     * @param url  url
     * @param body body
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/27 16:10
     */
    String post(String url, String body);

    /**
     * post
     *
     * @param url     url
     * @param body    body
     * @param timeout timeout
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/27 17:04
     */
    String post(String url, String body, int timeout);

    /**
     * post
     *
     * @param url   url
     * @param param param
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/27 17:04
     */
    String post(String url, Map<String, Object> param);

    /**
     * post
     *
     * @param url     url
     * @param param   param
     * @param timeout timeout
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/27 17:05
     */
    String post(String url, Map<String, Object> param, int timeout);
}
