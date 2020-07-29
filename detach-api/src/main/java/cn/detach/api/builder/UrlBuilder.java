package cn.detach.api.builder;

import cn.detach.api.annoation.RemoteHeader;
import cn.detach.api.annoation.RemoteParameter;
import cn.detach.api.constant.RemoteParameterType;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * url parse and build
 *
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 18:09
 */
public class UrlBuilder {

    /**
     * url caches
     */
    private final static Map<Method, WeakReference<List<TokenParser.ParameterMap>>> URL_TEMPLATE_CACHE
            = new ConcurrentHashMap<>();

    public static List<TokenParser.ParameterMap> getUrlTemplate(Method method, String originalUrl, Map<String, Object> map) {
        WeakReference<List<TokenParser.ParameterMap>> listWeakReference = URL_TEMPLATE_CACHE.get(method);
        if (listWeakReference == null || listWeakReference.get() == null) {
            URL_TEMPLATE_CACHE.put(method, new WeakReference<>(TokenParser.parseTemplate(originalUrl, map)));
        }
        return URL_TEMPLATE_CACHE.get(method).get();
    }

    public static String buildUrl(Method method, Object[] args, String originalUrl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        StringBuilder builder = new StringBuilder();

        Parameter[] parameters = method.getParameters();
        Queue<Object> queue = new LinkedList<>();
        Map<String, Object> paramMap = new HashMap<>();

        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            Object arg = args[i];
            if (parameter.isAnnotationPresent(RemoteHeader.class)) {
                continue;
            }
            if (parameter.isAnnotationPresent(RemoteParameter.class)) {
                paramMap.put(parameter.getAnnotation(RemoteParameter.class).name(), arg);
                continue;
            }
            queue.offer(arg);
        }
        List<TokenParser.ParameterMap> queryList = getUrlTemplate(method, originalUrl, paramMap);
        for (TokenParser.ParameterMap parameterMap : queryList) {
            if (parameterMap.getParameterType().equals(RemoteParameterType.STRING)) {
                builder.append(parameterMap.getValue());
                continue;
            }
            if (parameterMap.getParameterType().equals(RemoteParameterType.REMOTE_PARAMETER)) {
                String key = parameterMap.getValue();
                if (key.contains(".")) {
                    builder.append(reflectValue(key, paramMap.get(key.substring(0, key.indexOf(".")))));
                } else {
                    builder.append(paramMap.get(parameterMap.getValue()));
                }
                continue;
            }
            if (parameterMap.getParameterType().equals(RemoteParameterType.PARAMETER)) {
                builder.append(queue.poll());
            }
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private static String reflectValue(String key, Object arg) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String field = key.substring(key.indexOf(".") + 1);
        if (arg instanceof Map) {
            return ((Map<String, String>) arg).get(field);
        }
        String methodName = getGetterMethod(field);
        Method method = arg.getClass().getDeclaredMethod(methodName);
        return String.valueOf(method.invoke(arg));
    }

    public static String getGetterMethod(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
