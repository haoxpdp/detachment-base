package cn.detachment.es.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haoxp
 */
public class RefUtils {

    private static final Map<Class<?>, WeakReference<SerializedLambda>> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(RefUtils.class);

    public static final String PREFIX_GET = "get";

    public static final String PREFIX_IS = "is";

    public static <T> String getFiledName(FiledFunction<T, ?> filedFunction) {
        SerializedLambda lambda = getSerializedLambda(filedFunction);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith(PREFIX_GET)) {
            prefix = PREFIX_GET;
        } else if (methodName.startsWith(PREFIX_IS)) {
            prefix = PREFIX_IS;
        }
        if (prefix == null) {
            logger.warn("invalid method name : {}", methodName);
        }
        return toLowerCaseFirstOne(methodName.replace(prefix, ""));
    }

    public static SerializedLambda getSerializedLambda(Serializable fn) {
        WeakReference<SerializedLambda> lambdaWeakReference = CLASS_LAMBDA_CACHE.get(fn.getClass());
        SerializedLambda lambda = null;
        if (lambdaWeakReference == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMBDA_CACHE.put(fn.getClass(), new WeakReference<>(lambda));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            lambda = lambdaWeakReference.get();
        }
        return lambda;
    }

    public static String toLowerCaseFirstOne(String str) {
        return str.substring(0, 1).toLowerCase() +
                str.substring(1);
    }
}
