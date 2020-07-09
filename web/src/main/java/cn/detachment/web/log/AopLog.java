package cn.detachment.web.log;

import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 * @author haoxp
 */
public abstract class AopLog implements InitializingBean {


    /**
     * point cut
     */
    public abstract void pointCut();

    /**
     * init Exclude List
     *
     * @return
     */
    public abstract List<String> initExcludeList();


    private static Logger monitor = LoggerFactory.getLogger(AopLog.class);

    protected List<String> excludeNames;

    private static final Integer INDEX_INIT_VALUE = 0;

    private static final ThreadLocal<Integer> THREAD_LOCAL = ThreadLocal.withInitial(() -> INDEX_INIT_VALUE);


    @Around(value = "pointCut()")
    public Object aroundLog() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        excludeNames = initExcludeList();
    }
}
