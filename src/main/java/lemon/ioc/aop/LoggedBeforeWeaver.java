package lemon.ioc.aop;

/**
 * Before
 *
 * @param <T> target object type
 * @author lemon
 * @since 0.1.1
 */
public abstract class LoggedBeforeWeaver<T> extends LoggedWeaver<T> {
    @Override
    protected final void exception(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }

    @Override
    protected final void after(AOPPoint<T> point) {
    }

    @Override
    protected final void destroy(T target) {
    }
}
