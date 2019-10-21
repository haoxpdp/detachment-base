package cn.detachment.frame.core.aop;

import cn.detachment.frame.core.factory.ResultFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/14 14:17
 */
public abstract class AbstractLogAop {

    abstract void pintCut();

    /**
     * initExcludeList
     * 初始化不打印返回值的方法名前缀
     *
     * @param
     * @return java.util.List<java.lang.String>
     * @author haoxp
     * @date 19/10/14 14:30
     */
    abstract List<String> initExcludeList();

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

        Logger logger = LoggerFactory.getLogger(point.getSignature().getDeclaringType());

        logger.info("{}.{} begin --- ({}) ",
                point.getSignature().getDeclaringTypeName(), point.getSignature().getName(), point.getArgs());

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

        Object logValue = returnValue;

        if (!CollectionUtils.isEmpty(excludeName)) {
            for (int i = 0; i < excludeName.size(); i++) {
                if (point.getSignature().getName().startsWith(excludeName.get(i))) {
                    logValue = "";
                    break;
                }
            }
        }

        logger.info("{}.{} end {} --- {}",
                point.getSignature().getDeclaringTypeName(),
                point.getSignature().getName(), end - start, logValue);

        if (exception != null) {
            throw exception;
        }
        return returnValue;
    }
}
