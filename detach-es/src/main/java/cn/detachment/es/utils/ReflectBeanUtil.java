package cn.detachment.es.utils;

import cn.detachment.es.exception.ReflectionException;

import java.beans.PropertyDescriptor;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * @author haoxp
 * @date 21/3/13
 */
public class ReflectBeanUtil {

    public static volatile Map<Class<?>, WeakReference<WriteMethodCache>> WRITE_METHOD_CACHE = new ConcurrentHashMap<>();

    public static WriteMethodCache parseClass(Class<?> sourceClass) {
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(sourceClass);
        WriteMethodCache writeMethodCache = new WriteMethodCache(sourceClass);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            writeMethodCache.addMethod(descriptor.getName(), writeMethod, descriptor.getPropertyType());
        }
        return writeMethodCache;
    }

    public static Method getWriteMethod(Class<?> clazz, String propertyName) {
        WeakReference<WriteMethodCache> writeMethodCacheWeakReference = WRITE_METHOD_CACHE.get(clazz);
        WriteMethodCache writeMethodCache;
        if (writeMethodCacheWeakReference == null) {
            writeMethodCache = parseClass(clazz);
            WRITE_METHOD_CACHE.put(clazz, new WeakReference<>(writeMethodCache));
        } else {
            writeMethodCache = writeMethodCacheWeakReference.get();
        }

        assert writeMethodCache != null;
        return writeMethodCache.getMethod(propertyName);

    }

    static class WriteMethodCache {

        private final Class<?> sourceClass;

        private final Map<String, Method> methodCache = new HashMap<>();

        private final Map<String, Class<?>> argType = new HashMap<>();

        public WriteMethodCache(Class<?> clazz) {
            this.sourceClass = clazz;
        }

        public void addMethod(String propertyName, Method writeMethod, Class<?> paramType) {
            methodCache.put(propertyName, writeMethod);
            argType.put(propertyName, paramType);
        }

        public Method getMethod(String propertyName) {
            Method method = methodCache.get(propertyName);
            if (method == null) {
                throw new ReflectionException("there is no setter for property named : '" + propertyName + "' in class '" + sourceClass + "'");
            }
            return method;
        }

        public Class<?> getSetterType(String propertyName) {
            Class<?> aClass = argType.get(propertyName);
            if (aClass == null) {
                throw new ReflectionException("there is no setter param for property named : '" + propertyName + "' in class '" + sourceClass + "'");
            }
            return aClass;
        }
    }
}
