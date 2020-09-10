package cn.detachment.es.annotation;

import cn.detachment.es.support.DesScanner;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author haoxp
 * @date 20/9/10
 */
@Import(DesScanner.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableDes {

    String[] value() default {};

    String[] basePackages() default {};

    Class<?>[] scanClasses() default {};

    String esClientName() default "esClient";

}
