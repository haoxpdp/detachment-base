package cn.detachment.core.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

/**
 * @author haoxp
 * @version v1.0
 * @date 19/10/13 14:14
 */
public class StupidBeanUtil extends BeanUtils {

    public static void copy(Class<?> sourceClass, Class<?> targetClass, @Nullable String... ignoreProperties) throws BeansException {

        PropertyDescriptor[] targetPds = getPropertyDescriptors(targetClass);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : null);

        String resourceName = getObjectName(sourceClass);
        String targetName = getObjectName(targetClass);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {

                String sourcePropertyName = targetPd.getName();
                sourcePropertyName = formatNameToHump(sourcePropertyName);
                PropertyDescriptor sourcePd = getPropertyDescriptor(sourceClass, sourcePropertyName);
                if (sourcePd == null) {
                    sourcePropertyName = formatNameToDb(sourcePropertyName);
                    sourcePd = getPropertyDescriptor(sourceClass, sourcePropertyName);
                }
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                            ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            System.out.println(String.format("%s.%s(%s.%s());", targetName, writeMethod.getName(), resourceName, readMethod.getName()));
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                } else {

                    System.out.println(String.format("%s.%s(null);", targetName, writeMethod.getName()));
                }
            }
        }
    }

    public static <T> String getObjectName(Class<T> clazz) {
        String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
        StringBuilder objName = new StringBuilder();
        boolean flag = true;
        for (char c : className.toCharArray()) {
            char t;
            if (flag) {
                flag = false;
                if (Character.isUpperCase(c)) {
                    t = Character.toLowerCase(c);
                } else {
                    t = c;
                }
            } else {
                t = c;
            }
            objName.append(t);
        }
        return objName.toString();
    }

    public static String formatNameToHump(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (!s.contains("_")) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        boolean flag = false;
        for (char c : s.toCharArray()) {
            if (c == '_') {
                flag = true;
                continue;
            }
            stringBuilder.append(flag ? Character.toUpperCase(c) : Character.toLowerCase(c));
            flag = false;
        }
        return stringBuilder.toString();
    }

    /**
     * 将驼峰字符串格式化为xx_xx_xx
     */
    public static String formatNameToDb(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) {
                stringBuilder.append("_");
                stringBuilder.append(Character.toLowerCase(c));
            } else {

                stringBuilder.append(c);
            }
        }
        return stringBuilder.toString();
    }
}
