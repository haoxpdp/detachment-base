package cn.detachment.frame.redis.advisor;

import cn.detachment.frame.redis.annotation.DetachLock;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.util.Assert;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/19 23:49
 */
public class RedisLockAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private Advice advice;

    private Pointcut pointcut;

    public RedisLockAdvisor(MethodInterceptor methodInterceptor) {
        this.advice = methodInterceptor;
        this.pointcut = AnnotationMatchingPointcut.forMethodAnnotation(DetachLock.class);
    }

    @Override
    public Pointcut getPointcut() {
        Assert.notNull(pointcut, "detach redis lock pointcut must not be null!");
        return pointcut;
    }

    @Override
    public Advice getAdvice() {
        Assert.notNull(advice, "detach redis lock advice must not be null!");
        return advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }
}
