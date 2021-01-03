package cn.detachment.example.api;

import cn.detach.api.annoation.ApiSupport;
import cn.detach.api.annoation.RemoteApi;

/**
 * @author haoxp
 * @date 20/9/16
 */
@ApiSupport
public interface TestApi {

    @RemoteApi(
            url = "https://ip.sb?key=${test.key}"
    )
    String testIpSb();


}
