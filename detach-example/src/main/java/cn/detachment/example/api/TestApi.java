package cn.detachment.example.api;

import cn.detach.api.annoation.ApiSupport;
import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.api.RemotePost;

/**
 * @author haoxp
 * @date 20/9/16
 */
@ApiSupport
public interface TestApi {

    @RemotePost(
            url = "https://ip.sb?key=${test.key}"
    )
    String testIpSb();


}
