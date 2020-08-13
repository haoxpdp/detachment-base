package cn.detach.api.support;

import cn.detach.api.http.RemoteRequest;

import java.util.Map;

/**
 * @author haoxp
 */
@SuppressWarnings("unused")
public interface HttpUtilApi {

    /**
     * parserRemoteRequest
     *
     * @param remoteRequest remoteRequest
     * @return java.lang.String
     * @author haoxp
     * @date 20/8/5
     */
    String parserRemoteRequest(RemoteRequest remoteRequest);
}
