package cn.detachment.redis.test;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.DynamicIntroductionAdvice;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * @author haoxp
 * @date 20/12/23
 */
public class TestIntroductionInterceptor implements IntroductionInterceptor, TestApi {

    public static void main(String[] args) {
        ProxyFactory factory = new ProxyFactory(new Test());
        factory.setProxyTargetClass(true);
        Advice advice = new TestIntroductionInterceptor();
        Advisor advisor = new DefaultIntroductionAdvisor((DynamicIntroductionAdvice) advice,TestApi.class);
        factory.addAdvisor(advisor);
        TestApi testApi = (TestApi) factory.getProxy();
        testApi.doSth();
        Test test = (Test) factory.getProxy();
        test.tests();
    }

    @Override
    public void doSth() {
        System.out.println("doSth();");
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (implementsInterface(invocation.getMethod().getDeclaringClass())) {
            System.out.println("introduction invocation");
            return invocation.getMethod().invoke(this, invocation.getArguments());
        }
        return invocation.proceed();
    }

    @Override
    public boolean implementsInterface(Class<?> clazz) {
        return clazz.isAssignableFrom(TestApi.class);
    }
}
