package net.cassite.pure.aop;

/**
 * After Return
 *
 * @param <T> target object type
 * @author lemon
 * @since 0.1.1
 */
public abstract class LoggedAfterWeaver<T> extends LoggedWeaver<T> {
    @Override
    public final void before(AOPPoint<T> point) {
    }

    @Override
    public final void exception(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }

    @Override
    protected final void destroy(T target) {
    }
}
