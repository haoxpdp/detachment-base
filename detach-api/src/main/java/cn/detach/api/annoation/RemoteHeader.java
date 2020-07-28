package cn.detach.api.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/27 22:30
 */
@Target({ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface RemoteHeader {
    String name();

    String method();
}
