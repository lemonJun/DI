package lemon.ioc.aop;

/**
 * After Throwing
 *
 * @param <T> target object type
 * @author lemon
 * @since 0.1.1
 */
public abstract class LoggedExceptionWeaver<T> extends LoggedWeaver<T> {
    @Override
    protected final void before(AOPPoint<T> point) {
    }

    @Override
    protected final void after(AOPPoint<T> point) {
    }

    @Override
    protected final void destroy(T target) {
    }
}
