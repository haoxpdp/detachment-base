package cn.detachment.redis.configuration;

import cn.detachment.redis.annoatation.DetachLock;
import cn.detachment.redis.interceptor.AnnotationAwareRedisLockInterceptor;
import cn.detachment.redis.interceptor.RedisLockInterceptor;
import cn.detachment.redis.pointcut.AnnotationMethodPointcut;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author haoxp
 * @date 20/12/23
 */
@Configuration
public class RedisLockConfiguration extends AbstractPointcutAdvisor implements BeanFactoryAware, InitializingBean {
    private BeanFactory beanFactory;

    private Advice advice;

    private Pointcut pointcut;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.advice = new RedisLockInterceptor();
        this.pointcut = AnnotationMethodPointcut.buildPointCut(DetachLock.class);

    }

    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        return advice;
    }
}
