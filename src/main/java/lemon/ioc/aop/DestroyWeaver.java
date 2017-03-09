package lemon.ioc.aop;

/**
 * weaver that destroys target
 */
public interface DestroyWeaver<T> extends Weaver<T> {
    @Override
    default void doBefore(AOPPoint<T> point) {
    }

    @Override
    default void doAfter(AOPPoint<T> point) {
    }

    @Override
    default void doException(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }

    @Override
    void doDestroy(T target);
}
