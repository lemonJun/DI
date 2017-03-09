package net.cassite.pure.aop;

import net.cassite.pure.aop.AOPPoint;
import net.cassite.pure.aop.Weaver;

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
