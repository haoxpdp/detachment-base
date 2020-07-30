package cn.detach.api.annoation;

import cn.detach.api.constant.HttpMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface RemoteApi {
    String url();

    HttpMethod method() default HttpMethod.GET;

    int timeout() default -1;

    int retry() default 1;

    boolean async() default false;

}
