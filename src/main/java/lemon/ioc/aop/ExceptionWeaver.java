package lemon.ioc.aop;

/**
 * weaver only concerns AfterThrown
 */
public interface ExceptionWeaver<T> extends Weaver<T> {
    @Override
    default void doBefore(AOPPoint<T> point) {
    }

    @Override
    default void doAfter(AOPPoint<T> point) {
    }

    @Override
    default void doDestroy(T target) {
    }
}
