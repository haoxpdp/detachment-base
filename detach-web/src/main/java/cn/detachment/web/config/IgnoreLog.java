package cn.detachment.web.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreLog {

    /**
     * don't log params and  end response
     */
    boolean value() default true;

    /**
     * don't log method begin and method end
     */
    boolean ignore() default false;

}
