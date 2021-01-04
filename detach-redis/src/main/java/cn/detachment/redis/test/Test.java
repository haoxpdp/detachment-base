package cn.detachment.redis.test;

import cn.detachment.redis.annoatation.DetachLock;
import cn.detachment.redis.interceptor.RedisLockInterceptor;
import cn.detachment.redis.pointcut.AnnotationMethodPointcut;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author haoxp
 * @date 20/12/22
 */
@Component
public class Test {

    @Resource
    private Test test;

    public static void main(String[] args) {
        System.out.println(Float.valueOf("0.1"));
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setTarget(new Test());
        Pointcut cut = new AnnotationMethodPointcut(DetachLock.class);
        Advice advice = new RedisLockInterceptor();
        Advisor advisor = new DefaultPointcutAdvisor(cut, advice);
        factory.addAdvisor(advisor);
        Test test = (Test) factory.getObject();
        test.tests();
    }

    @DetachLock
    public int run() {
        System.out.println("run");
        return 1;
    }

    public void tests() {
        test.run();
        System.out.println("test");
    }

    public void A() {
        B();
    }

    @Retryable(Exception.class)
    public void B() {
        throw new RuntimeException("retry...");
    }
}
