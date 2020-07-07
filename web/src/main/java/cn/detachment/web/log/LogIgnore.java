package cn.detachment.web.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @author haoxp
 */
@Target(ElementType.METHOD)
public @interface LogIgnore {

    /**
     * don't log end response
     */
    boolean value() default true;

    /**
     * don't log method begin and method end
     */
    boolean ignore() default false;

}
