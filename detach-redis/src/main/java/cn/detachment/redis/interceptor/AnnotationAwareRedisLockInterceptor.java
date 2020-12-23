package cn.detachment.redis.interceptor;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.IntroductionAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class AnnotationAwareRedisLockInterceptor implements IntroductionAdvisor, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Override
    public ClassFilter getClassFilter() {
        return null;
    }

    @Override
    public void validateInterfaces() throws IllegalArgumentException {

    }

    @Override
    public Advice getAdvice() {
        return null;
    }

    @Override
    public boolean isPerInstance() {
        return false;
    }

    @Override
    public Class<?>[] getInterfaces() {
        return new Class[0];
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
