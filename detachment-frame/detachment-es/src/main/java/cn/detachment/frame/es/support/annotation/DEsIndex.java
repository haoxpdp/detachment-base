package cn.detachment.frame.es.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * detachment index name support
 *
 * @author haoxp
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DEsIndex {

    /**
     * value
     * es index name
     * @param
     * @return java.lang.String
     * @author haoxp
     * @date 20/5/15 11:49
     */
    String value() default "";
}
