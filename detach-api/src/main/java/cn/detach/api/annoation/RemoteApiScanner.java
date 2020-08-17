package cn.detach.api.annoation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 11:37
 */
@Import(RemoteApiImportBeanDefinitionRegistrar.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteApiScanner {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] scanClasses() default {};

}
