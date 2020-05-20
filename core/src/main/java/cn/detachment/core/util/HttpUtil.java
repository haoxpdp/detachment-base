package cn.detachment.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * support http，https
 * http_version 1.1
 *
 * @author haoxp
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static ExecutorService httpExecutor = new ThreadPoolExecutor(4, 8,
            300, TimeUnit.SECONDS, new LinkedBlockingDeque<>(1024 * 10),
            new ThreadPoolExecutor.CallerRunsPolicy());

    /**
     * default executor is newCachedThreadPool，
     * change it to {#httpExecutor}
     */
    public static HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(30))
            .executor(httpExecutor)
            .build();

//    public static CompletableFuture<String> get() {
//        return null;
//    }

//    public String get() throws InterruptedException {
//        CompletableFuture<String> future = httpClient.sendAsync(null, HttpResponse.BodyHandlers.ofString())
//                .thenApply(HttpResponse::body);
//        return null;
//    }

    public static CompletableFuture<String> asyncGet(final String uri, final Map<String, Object> header) throws URISyntaxException {
        return asyncGet(uri, header, Duration.ofSeconds(30));
    }

    public static CompletableFuture<String> asyncGet(final String uri, final Map<String, Object> header, Duration timeout) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        requestBuilder.GET().uri(new URI(uri));
        return asyncRequest(requestBuilder, header, timeout);
    }

    public static CompletableFuture<String> asyncPostJson(final String uri, Map<String, Object> header, final String jsonStr) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        if (CollectionUtils.isEmpty(header)) {
            header = new HashMap<>(8);
        }
        header.put("Content-Type", "application/json");

        requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .uri(new URI(uri));
        return asyncRequest(requestBuilder, header, null, null);
    }

    public static CompletableFuture<String> asyncPost(final String uri, Map<String, Object> header,
                                                      final Map<String, Object> param) throws URISyntaxException {
        return asyncPost(uri, header, param, null, null);
    }

    public static CompletableFuture<String> asyncPost(final String uri, Map<String, Object> header,
                                                      final Map<String, Object> param, final Duration timeout,
                                                      final Charset charset) throws URISyntaxException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder();
        StringBuilder paramStr = new StringBuilder();
        if (CollectionUtils.isEmpty(header)) {
            header = new HashMap<>();
        }
        header.put("Content-Type", "application/x-www-form-urlencoded");
        param.forEach((k, v) -> paramStr.append(k).append("=").append(v).append("&"));

        requestBuilder
                .POST(HttpRequest.BodyPublishers.ofString(paramStr.subSequence(0, paramStr.length() - 1).toString(),
                        charset)
                )
                .uri(new URI(uri));
        return asyncRequest(requestBuilder, header, timeout, charset);
    }

    public static CompletableFuture<String> asyncRequest(HttpRequest.Builder reqBuilder,
                                                         final Map<String, Object> header,
                                                         final Duration timeout) {
        return asyncRequest(reqBuilder, header, timeout, StandardCharsets.UTF_8);
    }

    public static CompletableFuture<String> asyncRequest(HttpRequest.Builder reqBuilder,
                                                         final Map<String, Object> header,
                                                         Duration timeout,
                                                         Charset charset) {
        header.forEach((k, v) -> reqBuilder.header(k, String.valueOf(v)));
        if (timeout == null) {
            timeout = Duration.ofSeconds(30);
        }
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        reqBuilder.timeout(timeout);

        return asyncRequest(reqBuilder.build());
    }

    public static CompletableFuture<String> asyncRequest(HttpRequest request) {
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply(HttpResponse::body);
    }

    public static String sync(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, URISyntaxException {
        Map<String, Object> param = new HashMap<>();
        param.put("name", "haoxp");
        param.put("age", 15);
        CompletableFuture<String> future = asyncPost("http://localhost:7997/testpost", new HashMap<>(), param, Duration.ofSeconds(30), StandardCharsets.UTF_8);
        System.out.println(future.get());
    }
}
