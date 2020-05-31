package cn.detachment.es.annoation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 指定实体类对应得es索引名
 *
 * @author haoxp
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DesIndex {

    /**
     * 索引名
     *
     * @return
     */
    String value();

    /**
     * 别名
     *
     * @return
     */
    String alias() default "";

}
