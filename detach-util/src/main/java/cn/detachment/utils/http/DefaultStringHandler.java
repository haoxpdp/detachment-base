package cn.detachment.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haoxp
 * @date 20/10/2
 */
public class DefaultStringHandler implements ResponseHandler<String> {

    public static final int SUCCESS = 200;
    public static final int REDIRECT = 300;

    public static final int BAD_REQUEST = 400;

    public final Charset charset;

    private DefaultStringHandler(Charset charset) {
        this.charset = charset;
    }

    private static ConcurrentHashMap<Charset, DefaultStringHandler> instanceCacheMap = new ConcurrentHashMap<>();

    public static DefaultStringHandler getInstanceByCharset(Charset charset) {

        if (instanceCacheMap.containsKey(charset)) {
            return instanceCacheMap.get(charset);
        }
        synchronized (charset.toString()) {
            if (!instanceCacheMap.containsKey(charset)) {
                instanceCacheMap.put(charset, new DefaultStringHandler(charset));
            }
        }
        return instanceCacheMap.get(charset);
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        HttpEntity entity = response.getEntity();
        String responseEntity = entity != null ? EntityUtils.toString(entity, charset) : null;

        int status = response.getStatusLine().getStatusCode();
        if (status >= SUCCESS && status < REDIRECT) {
            return responseEntity;
        } else {
            throw new HttpResponseException(status, "Unexpected response status: " + status);
        }
    }

}
