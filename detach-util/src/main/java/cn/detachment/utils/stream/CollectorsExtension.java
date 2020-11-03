package cn.detachment.utils.stream;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 仿照Collectors类
 * 使用示例
 * <pre>
 *
 *  Map<String, BigDecimal> collect = beans.stream().collect(
 *      Collectors.groupingBy(
 *           Bean::getXxxx,
 *           CollectorsExtension.toBigDecimalFunction(Bean::getValue)
 *  ));
 * </pre>
 *
 * @author haoxp
 * @date 20/10/28
 */
@SuppressWarnings("all")
public class CollectorsExtension {

    static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();


    static class CollectorImpl<T, A, R> implements Collector<T, A, R> {
        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final BinaryOperator<A> combiner;
        private final Function<A, R> finisher;
        private final Set<Collector.Characteristics> characteristics;

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Function<A, R> finisher,
                      Set<Characteristics> characteristics) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.combiner = combiner;
            this.finisher = finisher;
            this.characteristics = characteristics;
        }

        @SuppressWarnings("unchecked")
        private static <I, R> Function<I, R> castingIdentity() {
            return i -> (R) i;
        }

        CollectorImpl(Supplier<A> supplier,
                      BiConsumer<A, T> accumulator,
                      BinaryOperator<A> combiner,
                      Set<Characteristics> characteristics) {
            this(supplier, accumulator, combiner, castingIdentity(), characteristics);
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BinaryOperator<A> combiner() {
            return combiner;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return characteristics;
        }
    }

    public static <T> Collector<T, ?, BigDecimal> toBigDecimalFunction(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{BigDecimal.ZERO},
                (a, t) -> {
                    a[0] = a[0].add(mapper.applyAsBigDecimal(t));
                },
                (a, b) -> {
                    a[0] = a[0].add(b[0]);
                    return a;
                },
                a -> a[0], CH_NOID);
    }


    public static <T> Collector<T, ?, BigDecimal>
    summingBigdecimal(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[]{BigDecimal.ZERO},
                (a, t) -> {
                    a[0] = a[0].add(mapper.applyAsBigDecimal(t));
                },
                (a, b) -> {
                    a[0] = a[0].add(b[0]);
                    return a;
                },
                a -> a[0], CH_NOID);
    }


    /**
     * Returns a {@code Collector} that produces the arithmetic mean of an integer-valued
     * function applied to the input elements.  If no elements are present,
     * the result is 0.
     *
     * @param <T>    the type of the input elements
     * @param mapper a function extracting the property to be averaged
     * @return a {@code Collector} that produces the arithmetic mean of a
     * derived property
     */
    public static <T> Collector<T, ?, Double>
    averagingBigDecimal(ToBigDecimalFunction<? super T> mapper) {
        return new CollectorImpl<>(
                () -> new BigDecimal[2],
                (a, t) -> {
                    a[0].add(mapper.applyAsBigDecimal(t));
                    a[1] = a[1].add(BigDecimal.ONE);
                },
                (a, b) -> {
                    a[0].add(b[0]);
                    a[1] = a[1].add(b[1]);
                    return a;
                },
                a -> (a[1].equals(BigDecimal.ZERO)) ? 0.0d : (double) a[0].intValue() / a[1].intValue(), CH_NOID);
    }


    public interface ToBigDecimalFunction<T> {
        BigDecimal applyAsBigDecimal(T t);
    }
}
