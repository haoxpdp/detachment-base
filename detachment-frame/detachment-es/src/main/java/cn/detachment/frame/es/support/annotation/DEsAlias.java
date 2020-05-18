package cn.detachment.frame.es.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * detachment es alias support
 *
 * @author haoxp
 * @date 20/5/15 11:44
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DEsAlias {
    /**
     * value
     * es index alias
     *
     * @param
     * @return java.lang.String
     * @author haoxp
     * @date 20/5/15 11:50
     */
    String value() default "";
}
