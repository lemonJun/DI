package lemon.needle.aop;

/**
 * weaver that destroys target and is with log
 */
public abstract class LoggedDestroyWeaver<T> extends LoggedWeaver<T> {
    @Override
    protected final void before(AOPPoint<T> point) {
    }

    @Override
    protected final void after(AOPPoint<T> point) {
    }

    @Override
    protected final void exception(AOPPoint<T> point) throws Throwable {
        throw point.exception();
    }
}
