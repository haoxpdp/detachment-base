package cn.detachment.es.factory;

import cn.detachment.es.exception.ReflectionException;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author haoxp
 * @date 21/3/8
 */
public abstract class SimpleObjectFactory implements ObjectFactory {
    @Override
    public <T> T create(Class<T> type) {
        return create(type, null, null);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> argType, List<Object> objectList) {
        Class<?> instanceType = resolveInterface(type);

        return (T) instantiateClass(instanceType, argType, objectList);
    }


    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }

    protected Class<?> resolveInterface(Class<?> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Iterable.class) {
            //List|Collection|Iterable-->ArrayList
            classToCreate = ArrayList.class;
        } else if (type == Map.class) {
            //Map->HashMap
            classToCreate = HashMap.class;
        } else if (type == SortedSet.class) {
            //SortedSet->TreeSet
            classToCreate = TreeSet.class;
        } else if (type == Set.class) {
            //Set->HashSet
            classToCreate = HashSet.class;
        } else {
            //除此以外，用原来的类型
            classToCreate = type;
        }
        return classToCreate;
    }

    private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        Constructor<T> constructor;
        try {
            if (type == null || constructorArgTypes == null) {
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructorArgs.toArray(new Object[0]));
        } catch (Exception e) {
            StringBuilder argType = new StringBuilder();
            if (constructorArgTypes != null) {
                for (Class<?> clazz : constructorArgTypes) {
                    argType.append(clazz.getSimpleName()).append(",");
                }
            }
            StringBuilder args = new StringBuilder();
            if (constructorArgs != null) {
                for (Object obj : constructorArgs) {
                    args.append(obj).append(",");
                }
            }
            throw new ReflectionException("error creating " + type.getSimpleName() +
                    " with invalid type(" + argType + ") or values (" + args + "), cause: " + e, e);
        }
    }

}
