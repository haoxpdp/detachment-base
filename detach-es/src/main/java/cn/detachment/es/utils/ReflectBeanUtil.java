package cn.detachment.es.utils;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.beans.BeanUtils.getPropertyDescriptors;

/**
 * @author haoxp
 * @date 21/3/13
 */
public class ReflectBeanUtil {

    private static final Map<String, Method> writeMethod = new ConcurrentHashMap<>();

    private static final Map<String, PropertyDescriptor> propertyCache = new ConcurrentHashMap<>();

    public static void parseClass(Class<?> sourceClass) {
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(sourceClass);
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            Method writeMethod = descriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }
            System.out.println(writeMethod.getName());

        }
    }

    public static void main(String[] args) {

    }
}
