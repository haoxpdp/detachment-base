package cn.detachment.example.config;

import cn.detachment.example.annoation.TAop;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author haoxp
 * @date 20/11/4
 */
@Configuration
public class Interceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        TAop annotation = method.getAnnotation(TAop.class);
        if (annotation == null) {
            return invocation.proceed();
        }


        System.out.println("method " + invocation.getMethod() + " is called on " + invocation.getThis() + " with args" +
                " " + invocation.getArguments());
        Object proceed = invocation.proceed();
        System.out.println("method " + invocation.getMethod() + " returns " + proceed);
        return proceed;
    }



}
