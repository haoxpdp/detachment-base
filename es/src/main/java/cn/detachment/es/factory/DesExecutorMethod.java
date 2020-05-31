package cn.detachment.es.factory;

import cn.detachment.es.annoation.DesIndex;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author haoxp
 */
public class DesExecutorMethod {

    private static Logger logger = LoggerFactory.getLogger(DesExecutorMethod.class);

    private Method method;

    private Class<?> entityClass;

    private Type entityType;

    public DesExecutorMethod(Class<?> api, Method method, Type entityType) {
        this.entityClass = api;
        this.method = method;
        this.entityType = entityType;
    }

    public Object execute(Object[] params, RestHighLevelClient client, DesIndex desIndex) {
        return null;
    }


    private class MethodWrapper {
        private final Method method;

        private final Class<?> returnType;

        MethodWrapper(Method method) {
            this.method = method;
            this.returnType = method.getReturnType();
        }

        Method getMethod() {
            return this.method;
        }
    }

}
