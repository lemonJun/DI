package lemon.needle.aop;

import lemon.needle.aop.AOPPoint;
import lemon.needle.aop.Weaver;

/**
 * weaver only concerns before invoke
 */
public interface BeforeWeaver<T> extends Weaver<T> {
    @Override
    default void doAfter(AOPPoint<T> point) {
    }

    @Override
    default void doException(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }

    @Override
    default void doDestroy(T target) {
    }
}
