package cn.detachment.example.es.util;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haoxp
 */
public class RefUtil {

    private static final Map<Class<?>, WeakReference<SerializedLambda>> CLASS_LAMDBA_CACHE = new ConcurrentHashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(RefUtil.class);

    public static <T> String getFiledName(FiledFunction<T,?> filedFunction){
        SerializedLambda lambda = getSerializedLambda(filedFunction);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith("get")){
            prefix = "get";
        }
        if (prefix == null){
            logger.warn("invalid method name : {}",methodName);
        }
        return toLowerCaseFirstOne(methodName.replace(prefix,""));
    }

    public static SerializedLambda getSerializedLambda(Serializable fn) {
        WeakReference<SerializedLambda> lambdaWeakReference = CLASS_LAMDBA_CACHE.get(fn.getClass());
        SerializedLambda lambda = null;
        if(lambdaWeakReference == null) {
            try {
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMDBA_CACHE.put(fn.getClass(), new WeakReference<>(lambda));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            lambda = lambdaWeakReference.get();
        }
        return lambda;
    }

    public static String toLowerCaseFirstOne(String str){
        if (StringUtils.isEmpty(str)){
            String[] errMmsgDes = new String[1];
            errMmsgDes[0] = str;
//            throw new InvalidArgumentException(errMmsgDes);
        }
        return str.substring(0,1).toLowerCase() +
        str.substring(1);
    }
}
