package cn.detachment.es.utils;


import java.io.Serializable;
import java.util.function.Function;

/**
 * 继承下 Serializable 以便获取 SerializedLambda
 * @author haoxp
 */
@FunctionalInterface
public interface FieldFunction<T, R> extends Function<T, R>, Serializable {
}
