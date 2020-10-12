package cn.detachment.log.support;

import org.slf4j.spi.MDCAdapter;

import java.util.Map;

/**
 * @author haoxp
 * @version v1.0
 * @date 20/7/15 21:42
 */
public class ThreadPoolLogSupport implements MDCAdapter {
    @Override
    public void put(String key, String val) {

    }

    @Override
    public String get(String key) {
        return null;
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Map<String, String> getCopyOfContextMap() {
        return null;
    }

    @Override
    public void setContextMap(Map<String, String> contextMap) {

    }
}
