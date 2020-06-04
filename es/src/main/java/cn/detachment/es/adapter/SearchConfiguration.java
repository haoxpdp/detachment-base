package cn.detachment.es.adapter;

import java.util.HashMap;
import java.util.Map;

/**
 * search configuration
 *
 * @author haoxp
 */
public class SearchConfiguration {

    /**
     * es search builder includes cache
     */
    protected Map<Class<?>, String[]> includesFields = new HashMap<>();

    /**
     * es search builder exclude cache
     */
    protected Map<Class<?>, String[]> excludesFields = new HashMap<>();


    public String[] getIncludesFields(Class<?> entityClass) {
        if (includesFields.containsKey(entityClass)) {
            return includesFields.get(entityClass);
        }
        return null;
    }

    public String[] getExcludesFields(Class<?> entityClass) {
        if (excludesFields.containsKey(entityClass)) {
            return excludesFields.get(entityClass);
        }
        return null;
    }

    public void setIncludesFields(Class<?> clazz, String[] fields) {
        if (includesFields.containsKey(clazz)) {
            return;
        }
        this.includesFields.put(clazz, fields);
    }

    public void setExcludesFields(Class<?> clazz, String[] fields) {
        if (excludesFields.containsKey(clazz)) {
            return;
        }
        this.excludesFields.put(clazz, fields);
    }

}
