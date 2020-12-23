package cn.detachment.redis.interceptor;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class AnnotationAwareRedisLockInterceptor implements IntroductionInterceptor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("----AnnotationAwareRedisLockInterceptor----");
        return invocation.proceed();
    }

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return cn.detachment.redis.lock.Lockable.class.isAssignableFrom(intf);
    }
}
