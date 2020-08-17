package cn.detach.api.annoation;

import cn.detach.api.constant.ContentType;
import cn.detach.api.constant.HttpMethod;

import java.lang.annotation.*;

/**
 * @author haoxp
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface RemoteApi {
    String url() default "";

    HttpMethod method() default HttpMethod.GET;

    int timeout() default -1;

    int retry() default 1;

    boolean async() default false;

    ContentType contentType() default ContentType.JSON;
}
