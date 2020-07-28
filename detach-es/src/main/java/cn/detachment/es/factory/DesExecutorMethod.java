package cn.detachment.es.factory;

import cn.detachment.es.adapter.EsAdapter;
import cn.detachment.es.annoation.DesIndex;
import cn.detachment.es.condition.DesSearchWrapper;
import org.elasticsearch.action.search.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author haoxp
 */
@SuppressWarnings("unused")
public class DesExecutorMethod {

    private static Logger logger = LoggerFactory.getLogger(DesExecutorMethod.class);

    private final Method method;

    private final Class<?> entityClass;

    private final Type entityType;

    private MethodWrapper methodWrapper;

    public DesExecutorMethod(Class<?> api, Method method, Type entityType) {
        this.entityClass = api;
        this.method = method;
        this.entityType = entityType;
        this.methodWrapper = new MethodWrapper(method);
    }

    public Object execute(Object[] params, EsAdapter esAdapter, DesIndex desIndex) {


        return null;
    }

    private SearchRequest inflateSearchRequest(DesIndex desIndex, DesSearchWrapper<?> searchWrapper) {
        SearchRequest searchRequest = new SearchRequest(desIndex.value());

        return searchRequest;
    }


    private class MethodWrapper {
        private final Method method;

        private final Class<?> returnType;

        private boolean returnsLong;

        private boolean returnsArray;

        private boolean returnsMap;

        MethodWrapper(Method method) {
            this.method = method;
            this.returnType = method.getReturnType();
        }

        Method getMethod() {
            return this.method;
        }
    }

}
