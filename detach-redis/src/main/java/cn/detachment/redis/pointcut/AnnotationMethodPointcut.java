package cn.detachment.redis.pointcut;

import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.ComposablePointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class AnnotationMethodPointcut extends StaticMethodMatcherPointcut {

    private final MethodMatcher methodResolver;

    public AnnotationMethodPointcut(Class<? extends Annotation> annotationType) {
        this.methodResolver = new AnnotationMethodMatcher(annotationType);
    }

    /**
     * 暂时只有单一注解，多注解锁时可用 ComposablePointcut
     */
    public static Pointcut buildPointCut(Class<? extends Annotation> annotation) {

        Pointcut ac = new AnnotationMethodPointcut(annotation);
        ComposablePointcut pointcut = new ComposablePointcut(ac);
        return pointcut;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        return this.methodResolver.matches(method, targetClass);
    }
}
