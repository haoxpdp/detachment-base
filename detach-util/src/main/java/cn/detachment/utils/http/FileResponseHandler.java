package cn.detachment.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;

import java.io.IOException;

/**
 * @author haoxp
 * @date 20/10/5
 */
public class FileResponseHandler implements ResponseHandler<String> {

    public String filePath;

    public FileResponseHandler(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        response.getEntity().getContent();
        return null;
    }

    public static void main(String[] args) {
        String res = HttpUtil.get("https://www.zhihu.com/hot", null);
        System.out.println(res);
    }
}
