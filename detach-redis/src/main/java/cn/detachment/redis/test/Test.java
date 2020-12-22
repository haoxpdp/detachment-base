package cn.detachment.redis.test;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author haoxp
 * @date 20/12/22
 */
public class Test {

    public static void main(String[] args) {
        String pointcutExpression = "execution( int cn.detachment.redis.test.Test.run())";
        ProxyFactoryBean factory = new ProxyFactoryBean();
        factory.setTarget(new Test());
        AspectJExpressionPointcut cut = new AspectJExpressionPointcut();
        cut.setExpression(pointcutExpression);
        Advice advice = (MethodInterceptor) methodInvocation -> {
            System.out.println("----before proceed");
            Object returnVal = methodInvocation.proceed();
            System.out.println("----after proceed");
            return returnVal;
        };
        Advisor advisor = new DefaultPointcutAdvisor(cut, advice);
        factory.addAdvisor(advisor);
        Test test = (Test) factory.getObject();
        test.run();
        test.tests();
    }

    public int run() {
        System.out.println("run");
        return 1;
    }

    public void tests() {
        System.out.println("test");
    }
}
