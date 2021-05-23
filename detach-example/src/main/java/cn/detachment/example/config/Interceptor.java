package cn.detachment.example.config;

import cn.detachment.example.annoation.TAop;
import cn.detachment.utils.http.HttpUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @author haoxp
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


    public static void main(String[] args) {
        System.out.println(HttpUtil.get("https://github.com.ipaddress.com/"));
    }

}
