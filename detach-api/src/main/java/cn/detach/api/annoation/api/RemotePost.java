package cn.detach.api.annoation.api;

import cn.detach.api.annoation.RemoteApi;
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
@RemoteApi(method = HttpMethod.POST)
public @interface RemotePost {
}
