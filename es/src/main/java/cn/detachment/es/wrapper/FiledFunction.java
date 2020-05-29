package cn.detachment.es.wrapper;


import java.io.Serializable;
import java.util.function.Function;

/**
 * 继承下 Serializable 以便获取 SerializedLambda
 * @author haoxp
 */
@FunctionalInterface
public interface FiledFunction<T, R> extends Function<T, R>, Serializable {
}
