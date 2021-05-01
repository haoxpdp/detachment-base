package cn.detach.api.builder;

import cn.detach.api.annoation.RemoteApi;
import cn.detach.api.annoation.RemoteApiBody;
import cn.detach.api.annoation.RemoteForm;
import cn.detach.api.annoation.RemoteHeader;
import cn.detach.api.annoation.RemoteParameter;
import cn.detach.api.annoation.RemoteUrl;
import cn.detach.api.constant.ContentType;
import cn.detach.api.constant.RemoteParameterType;
import cn.detach.api.exception.UrlBuildException;
import cn.detach.api.http.RemoteRequest;
import cn.detach.api.support.HttpUtilApi;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * url parse and build
 *
 * @author haoxp
 * @version v1.0
 * @date 20/7/28 18:09
 */
@SuppressWarnings("unchecked")
public class UrlBuilder {

    private static final Logger logger = LoggerFactory.getLogger(UrlBuilder.class);

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

    public static RemoteRequest buildRemoteRequest(Method method, Object[] args, RemoteApi remoteApi, HttpUtilApi httpUtilApi) {
        String originalUrl = remoteApi.url();
        RemoteRequest remoteRequest = new RemoteRequest();

        remoteRequest.setHttpMethod(remoteApi.method());

        StringBuilder builder = new StringBuilder();

        Parameter[] parameters = method.getParameters();
        Queue<Object> queue = new LinkedList<>();
        Map<String, Object> paramMap = new HashMap<>();

        // 先遍历请求参数，处理包含注释的参数
        if (args != null) {

            for (int i = 0; i < args.length; i++) {
                Parameter parameter = parameters[i];
                parameter.getAnnotations();
                Object arg = args[i];
                Annotation[] annotations = parameter.getAnnotations();
                if (annotations != null && annotations.length > 0) {
                    logger.info("{} {} ", annotations, annotations.length);
                    for (Annotation annotation :
                            annotations) {
                        TransferParamArg transferParamArg = annotationHandler.get(annotation.annotationType());
                        if (transferParamArg != null) {
                            transferParamArg.paramArg(parameter, arg, remoteRequest);
                        }
                    }
                    if (parameter.isAnnotationPresent(RemoteParameter.class)) {
                        paramMap.put(parameter.getAnnotation(RemoteParameter.class).name(), arg);
                    }
                    if (parameter.isAnnotationPresent(RemoteUrl.class) && StringUtils.isEmpty(originalUrl)) {
                        originalUrl = String.valueOf(arg);
                    }
                    if (parameter.isAnnotationPresent(RemoteApiBody.class)) {
                        remoteRequest.setRequestBody(arg);
                    }
                    continue;
                }
                // 其他参数
                queue.offer(arg);
            }
        }
        // 解析url模板，获取参数列表
        List<TokenParser.ParameterMap> queryConditionList = getUrlTemplate(method, originalUrl, paramMap);

        for (TokenParser.ParameterMap parameterMap : queryConditionList) {
            if (parameterMap.getParameterType().equals(RemoteParameterType.STRING)) {
                builder.append(parameterMap.getValue());
                continue;
            }
            if (parameterMap.getParameterType().equals(RemoteParameterType.REMOTE_PARAMETER)) {
                String key = parameterMap.getValue();
                if (key.contains(".")) {
                    builder.append(reflectValue(key, paramMap.get(key.substring(0, key.indexOf("."))), httpUtilApi));
                } else {
                    if (paramMap.containsKey(parameterMap.getValue())) {
                        builder.append(paramMap.get(parameterMap.getValue()));
                    } else {
                        builder.append(httpUtilApi.getParamFromEnv("${" + parameterMap.getValue() + "}"));
                    }
                }
                continue;
            }
            if (parameterMap.getParameterType().equals(RemoteParameterType.PARAMETER)) {
                builder.append(queue.poll());
            }
        }
        if (queue.size() == 1) {
            contentTypeHandler.get(remoteApi.contentType()).paramArg(queue.poll(), remoteRequest);
        }

        remoteRequest.setUrl(builder.toString());
        return remoteRequest;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static String reflectValue(String key, Object arg, HttpUtilApi httpUtilApi) {
        String field = key.substring(key.indexOf(".") + 1);
        if (arg instanceof Map) {
            return ((Map<String, String>) arg).get(field);
        }
        String methodName = getGetterMethod(field);
        try {
            if (arg == null) {
                return httpUtilApi.getParamFromEnv("${" + key + "}");
            }
            Method method = arg.getClass().getDeclaredMethod(methodName);
            return String.valueOf(method.invoke(arg));

        } catch (NoSuchMethodException e) {
            throw new UrlBuildException(e, " can't find get method of [" + field + "] at " + arg.getClass().getName());
        } catch (IllegalAccessException e) {
            throw new UrlBuildException(e, "filed [" + field + "] " + arg.getClass().getName());
        }
    }

    public static String getGetterMethod(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }


    static Map<Class<?>, TransferParamArg> annotationHandler = new HashMap<>();

    static {

        annotationHandler.put(RemoteHeader.class, (param, arg, request) -> {
            if (arg instanceof Map) {
                request.setHeader((Map<String, String>) arg);
            } else {
                request.setHeader((Map<String, String>) JSONObject.toJSON(arg));
            }
        });

        annotationHandler.put(RemoteForm.class, ((parameter, arg, request) -> {
            if (arg instanceof String) {
                String keyName = parameter.getAnnotation(RemoteForm.class).name();
                request.addFormData(keyName, arg);
            } else if (arg instanceof Map) {
                request.setFormData((Map<String, Object>) arg);
            } else {
                request.setFormData((Map<String, Object>) JSONObject.toJSON(arg));
            }
        }));
    }


    interface TransferParamArg {
        /**
         * paramArg
         *
         * @param parameter parameter
         * @param arg       arg
         * @param request   request
         * @author haoxp
         * @date 20/8/5
         */
        void paramArg(Parameter parameter, Object arg, RemoteRequest request);
    }


    static Map<ContentType, ContentTypeTransfer> contentTypeHandler;

    static {
        contentTypeHandler = new HashMap<>();

        contentTypeHandler.put(ContentType.JSON, ((arg, request) -> request.setRequestBody(arg)));

        contentTypeHandler.put(ContentType.FORM, ((arg, request) -> request.setFormData((Map<String, Object>) JSONObject.toJSON(arg))));
    }

    interface ContentTypeTransfer {
        /**
         * paramArg
         *
         * @param arg     arg
         * @param request request
         * @author haoxp
         * @date 20/8/5
         */
        void paramArg(Object arg, RemoteRequest request);
    }
}
