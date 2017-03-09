package lemon.ioc.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Around.<br>
 * extends this class to do aop and implements interfaces which you want to introduce
 *
 * @param <T> target object type
 * @author lemon
 * @since 0.1.1
 */
public abstract class LoggedWeaver<T> implements Weaver<T> {
    private static Logger LOGGER = LoggerFactory.getLogger(LoggedWeaver.class);

    /**
     * before invoking the method
     *
     * @param point aop point
     */
    protected abstract void before(AOPPoint<T> point);

    /**
     * after the method returned
     *
     * @param point aop point
     */
    protected abstract void after(AOPPoint<T> point);

    /**
     * after throwing
     *
     * @param point aop point
     * @throws Throwable possible exceptions
     */
    protected abstract void exception(AOPPoint<T> point) throws Throwable;

    /**
     * invoked when destroyed
     *
     * @param target target object
     * @since 0.3.1
     */
    protected abstract void destroy(T target);

    public final void doBefore(AOPPoint<T> point) {
        LOGGER.debug("do [Before] with point [{}]", point);
        before(point);
    }

    public final void doAfter(AOPPoint<T> point) {
        LOGGER.debug("do [After Return] with point [{}]", point);
        after(point);
        LOGGER.debug("[After Return] return value is {}", point.returnValue());
    }

    public final void doException(AOPPoint<T> point) throws Throwable {
        LOGGER.debug("do [After Exception] with point [{}]", point);
        exception(point);
        LOGGER.debug("[After Exception] return value is {}", point.returnValue());
    }

    @Override
    public final void doDestroy(T target) {
        LOGGER.debug("do [Destroy] on {}", target);
        destroy(target);
    }
}
