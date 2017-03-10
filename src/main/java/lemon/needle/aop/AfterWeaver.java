package lemon.needle.aop;

/**
 * weaver only concerns AfterReturn
 */
public interface AfterWeaver<T> extends Weaver<T> {
    @Override
    default void doBefore(AOPPoint<T> point) {
    }

    @Override
    default void doException(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }

    @Override
    default void doDestroy(T target) {
    }
}
