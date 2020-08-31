package cn.detach.api.annoation;

import cn.detach.api.constant.ContentType;
import cn.detach.api.constant.HttpMethod;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface RemoteApi {
    String url() default "";

    HttpMethod method() default HttpMethod.GET;

    int connectTimeout() default 1000 * 15;

    int socketTimeout() default 10000;

    int connectRequestTimeout() default 3000;

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    int retry() default 1;

    boolean async() default false;

    ContentType contentType() default ContentType.JSON;
}
