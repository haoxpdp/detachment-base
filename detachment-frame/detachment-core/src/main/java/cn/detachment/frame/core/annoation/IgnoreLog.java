package cn.detachment.frame.core.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/3/13 10:29
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLog {
    /**
     * 日志切面不打印参数
     *
     * @return
     */
    boolean ignoreParams() default false;

    /**
     * 日志切面不打印response log
     *
     * @return
     */
    boolean ignoreResponse() default true;
}
