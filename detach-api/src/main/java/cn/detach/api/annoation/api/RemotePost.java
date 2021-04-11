package cn.detach.api.annoation.api;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.constant.ContentType;
import cn.detach.api.constant.HttpMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author haoxp
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@RemoteApi(method = HttpMethod.POST)
@Inherited
public @interface RemotePost {

    @AliasFor(annotation = RemoteApi.class)
    String url() default "";

    @AliasFor(annotation = RemoteApi.class)
    HttpMethod method() default HttpMethod.POST;

    @AliasFor(annotation = RemoteApi.class)
    int connectTimeout() default 1000 * 15;

    @AliasFor(annotation = RemoteApi.class)
    int socketTimeout() default 10000;

    @AliasFor(annotation = RemoteApi.class)
    int connectRequestTimeout() default 3000;

    @AliasFor(annotation = RemoteApi.class)
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    @AliasFor(annotation = RemoteApi.class)
    int retry() default 1;

    @AliasFor(annotation = RemoteApi.class)
    boolean async() default false;

    @AliasFor(annotation = RemoteApi.class)
    ContentType contentType() default ContentType.JSON;
}
