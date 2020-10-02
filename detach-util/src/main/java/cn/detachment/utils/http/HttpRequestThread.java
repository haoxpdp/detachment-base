package cn.detachment.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @author haoxp
 * @date 20/9/30
 */
public class HttpRequestThread implements Callable<String>, ResponseHandler<String> {

    private final CloseableHttpClient client;

    private final HttpContext context;

    private final HttpUriRequest uriRequest;


    public static final int SUCCESS = 200;
    public static final int REDIRECT = 300;

    public static final int BAD_REQUEST = 400;

    public HttpRequestThread(CloseableHttpClient httpClient, HttpUriRequest uriRequest) {
        this.client = httpClient;
        this.context = HttpClientContext.create();
        this.uriRequest = uriRequest;
    }

    @Override
    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

        HttpEntity entity = response.getEntity();
        String responseEntity = entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : null;

        int status = response.getStatusLine().getStatusCode();
        if (status >= SUCCESS && status < REDIRECT) {
            return responseEntity;
        } else {
            throw new HttpResponseException(status, "Unexpected response status: " + status);
        }

    }

    @Override
    public String call() throws Exception {
        try {
            return client.execute(uriRequest, this, context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CloseableHttpClient client = HttpClients.custom().setConnectionManager(HttpPoolManager.getDefaultSimpleConnectionPool()).build();
        HttpUriRequest request = new HttpGet("https://www.baidu.com");

        Future<String> submit = executorService.submit(new HttpRequestThread(client, request));
        System.out.println("hello world");
        System.out.println(submit.get());
    }
}
