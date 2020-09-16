package cn.detachment.example.api;

import cn.detach.api.annoation.ApiSupport;
import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.RemoteUrl;

/**
 * @author haoxp
 * @date 20/9/16
 */
@ApiSupport
public interface TestApi {


    @RemoteApi
    String baidu(@RemoteUrl String url);
}
