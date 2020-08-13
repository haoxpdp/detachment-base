package cn.detachment.core.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author haoxp
 */
public abstract class AbstractMethodLog implements InitializingBean {


    /**
     * point cut
     */
    public abstract void pointCut();

    /**
     * init Exclude List
     *
     */
    public abstract List<String> initExcludeList();


    private static final Logger monitor = LoggerFactory.getLogger(AbstractMethodLog.class);

    protected List<String> excludeNames;

    private static final Integer INDEX_INIT_VALUE = 0;

    private static final ThreadLocal<Integer> THREAD_LOCAL = ThreadLocal.withInitial(() -> INDEX_INIT_VALUE);


    @Around(value = "pointCut()")
    public Object aroundLog(ProceedingJoinPoint point) throws Throwable {
        long start = System.currentTimeMillis();

        Class<?> targetCls = point.getTarget().getClass();

        Logger logger = LoggerFactory.getLogger(targetCls);

        Method targetMethod = getTargetMethod(point, targetCls);

        IgnoreLog ignoreLog = logParams(targetCls, targetMethod, logger, point.getArgs());

        Exception exception = null;

        Object returnVal = null;

        try {
            THREAD_LOCAL.set(THREAD_LOCAL.get() + 1);
            returnVal = point.proceed();
        } catch (Exception e) {
            exception = e;
        } finally {
            THREAD_LOCAL.set(THREAD_LOCAL.get() - 1);
        }

        if (exception != null) {
            monitor.error(exception.getMessage(), exception);
            throw exception;
        }

        logResponse(ignoreLog, returnVal, logger, System.currentTimeMillis() - start, targetCls, targetMethod);

        return returnVal;
    }

    private Method getTargetMethod(ProceedingJoinPoint point, Class<?> targetCls) throws NoSuchMethodException {
        MethodSignature ms = (MethodSignature) point.getSignature();

        return targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
    }

    private IgnoreLog logParams(Class<?> targetCls, Method method, Logger logger, Object logVal) {
        if (method.isAnnotationPresent(IgnoreLog.class)) {
            IgnoreLog annotation = method.getAnnotation(IgnoreLog.class);
            if (annotation.ignore()) {
                return annotation;
            } else if (annotation.value()) {
                logger.info("{}.{} begin ", targetCls.getSimpleName(), method.getName());
            }

        }
        logger.info("{}.{} begin --- ({}) ",
                targetCls.getSimpleName(), method.getName(), logVal);
        return null;
    }

    private Object getLogVal(Method method, Object returnValue) {
        if (CollectionUtils.isEmpty(excludeNames)) {
            return returnValue;
        }
        for (String name : excludeNames) {
            if (method.getName().startsWith(name)) {
                return "";
            }
        }
        return returnValue;
    }

    private void logResponse(IgnoreLog ignoreLog, Object returnValue, Logger logger, Long executeTime,
                             Class<?> targetClass, Method method) {
        if (ignoreLog == null) {
            logger.info("{}.{} end {} ms ---> {}", targetClass.getSimpleName(), method.getName(), executeTime, getLogVal(method, returnValue));
            return;
        }
        if (ignoreLog.ignore()) {
            return;
        }
        if (ignoreLog.value()) {
            logger.info("{}.{} end {} ms", targetClass.getSimpleName(), method.getName(), executeTime);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        excludeNames = initExcludeList();
    }
}
