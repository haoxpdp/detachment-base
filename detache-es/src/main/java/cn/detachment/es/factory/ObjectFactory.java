package cn.detachment.es.factory;

import java.util.List;

/**
 * 对象工厂，所有对象由此创建
 *
 * @author haoxp
 * @date 21/3/8
 */
public interface ObjectFactory {

    <T> T create(Class<T> type);

    <T> T create(Class<T> type, List<Class<?>> argType, List<Object> objectList);

    <T> boolean isCollection(Class<T> type);

    void setProperties();
}
