package cn.detach.api.http;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/30 11:21
 */
public interface HttpSupport {

    /**
     * request
     *
     * @param remoteRequest remoteRequest
     * @return java.lang.String
     * @author haoxp
     * @date 20/7/30 11:22
     */
    String request(RemoteRequest remoteRequest);

}
