package cn.detachment.frame.redis.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/19 19:45
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DetachLock {

    /**
     * 过期时间(必须是大于业务代码执行时间) 单位: 毫秒
     */
    long expire() default 30 * 1000;

    /**
     * 获取锁超时时间 单位: 毫秒
     */
    long timeout() default 30000;

}
