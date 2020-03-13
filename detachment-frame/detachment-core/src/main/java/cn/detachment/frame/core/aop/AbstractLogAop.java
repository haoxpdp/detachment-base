package cn.detachment.frame.core.aop;

import cn.detachment.frame.core.annoation.IgnoreLog;
import cn.detachment.frame.core.factory.ResultFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.aspectj.lang.reflect.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/14 14:17
 */
public abstract class AbstractLogAop {

    public abstract void pintCut();

    /**
     * initExcludeList
     * 初始化不打印返回值的方法名前缀
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author haoxp
     * @date 19/10/14 14:30
     */
    public abstract List<String> initExcludeList();

    private List<String> excludeName = initExcludeList();

    private static final Integer INDEX_INIT_VALUE = 0;

    private static final ThreadLocal<Integer> THREAD_LOCAL = ThreadLocal.withInitial(() -> INDEX_INIT_VALUE);

    private static Logger monitor = LoggerFactory.getLogger(AbstractLogAop.class);

    /**
     * around
     *
     * @param
     * @return java.lang.Object
     * @author haoxp
     * @date 19/10/14 14:31
     */
    @Around(value = "pintCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Long start = System.currentTimeMillis();
        Object returnValue = null;

        Class<?> targetCls = point.getTarget().getClass();

        Logger logger = LoggerFactory.getLogger(targetCls);

        Method targetMethod = getTargetMethod(point, targetCls);

        logParams(targetMethod, logger, point);

        Exception exception = null;
        try {
            THREAD_LOCAL.set(THREAD_LOCAL.get() + 1);
            returnValue = point.proceed();
        } catch (Exception e) {
            exception = e;
        } finally {
            THREAD_LOCAL.set(THREAD_LOCAL.get() - 1);
        }

        if (exception != null) {
            monitor.error(exception.getMessage(), exception);
            if (INDEX_INIT_VALUE.equals(THREAD_LOCAL.get())) {
                returnValue = ResultFactory.serverError(exception.getMessage());
            }
        }

        Long end = System.currentTimeMillis();

        Object logValue = getLogVal(point, returnValue);

        logResponse(targetMethod, logger, point, end - start, logValue);

        if (exception != null) {
            throw exception;
        }
        return returnValue;
    }

    private Object getLogVal(ProceedingJoinPoint point, Object returnValue) {
        if (!CollectionUtils.isEmpty(excludeName)) {
            for (int i = 0; i < excludeName.size(); i++) {
                if (point.getSignature().getName().startsWith(excludeName.get(i))) {
                    return "";
                }
            }
        }
        return returnValue;
    }

    private void logParams(Method method, Logger logger, ProceedingJoinPoint point) {
        if (method.isAnnotationPresent(IgnoreLog.class)) {
            IgnoreLog annotation = method.getAnnotation(IgnoreLog.class);
            if (annotation.ignoreParams()) {
                logger.info("{} #{} begin ", point.getSignature().getDeclaringTypeName(), point.getSignature().getName());
                return;
            }
        }
        logger.info("{} #{} begin --- ({}) ",
                point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), point.getArgs());
    }

    private void logResponse(Method method, Logger logger, ProceedingJoinPoint point, Long executeTime, Object logValue) {
        if (method.isAnnotationPresent(IgnoreLog.class)) {
            IgnoreLog annotation = method.getAnnotation(IgnoreLog.class);
            if (annotation.ignoreResponse()) {
                logger.info("{} #{} end {}",
                        point.getSignature().getDeclaringTypeName(),
                        point.getSignature().getName(), executeTime);
                return;
            }
        }
        logger.info("{} #{} end {} --- {}",
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName(), executeTime, logValue);
    }


    private Method getTargetMethod(ProceedingJoinPoint point, Class<?> targetCls) throws NoSuchMethodException {
        MethodSignature ms = (MethodSignature) point.getSignature();

        Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());

        return targetMethod;

    }
}
