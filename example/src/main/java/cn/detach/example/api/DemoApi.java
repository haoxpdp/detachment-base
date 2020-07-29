package cn.detach.example.api;

import cn.detach.api.annoation.ApiSupport;
import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.RemoteHeader;
import cn.detach.api.annoation.RemoteParameter;
import cn.detach.api.constant.HttpMethod;

import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/27 20:41
 */
@ApiSupport
public interface DemoApi {

    @RemoteApi(
            url = "http://localhost:9031/account-synchronizer/auth/gettoken?company=${company}&key=${key}",
            method = HttpMethod.GET)
    String getTest(@RemoteHeader Map<String, String> header, String company, @RemoteParameter(name = "company") String key);

}
