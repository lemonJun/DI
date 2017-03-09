package net.cassite.pure.aop;

/**
 * interface for weavers. this interface is an 'Around' AOP. implement other interfaces if you want to do 'Introduction'.
 *
 * @param <T> target object type
 * @since 0.2.1
 */
public interface Weaver<T> {
    /**
     * before invoking the method
     *
     * @param point aop point
     */
    void doBefore(AOPPoint<T> point);

    /**
     * after the method returned
     *
     * @param point aop point
     */
    void doAfter(AOPPoint<T> point);

    /**
     * after throwing
     *
     * @param point aop point
     * @throws Throwable possible exceptions
     */
    void doException(AOPPoint<T> point) throws Throwable;

    /**
     * invoke when it's destroyed
     *
     * @param target target object
     * @since 0.3.1
     */
    void doDestroy(T target);
}
