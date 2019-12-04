package cn.detachment.example.es.util;


import java.io.Serializable;
import java.util.function.Function;

/**
 * @author haoxp
 */
@FunctionalInterface
public interface FiledFunction<T, R> extends Function<T, R>, Serializable {
}
