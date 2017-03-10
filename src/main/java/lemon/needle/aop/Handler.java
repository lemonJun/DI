package lemon.needle.aop;

/**
 * handler for weavers and proxy objects
 *
 * @since 0.3.1
 */
public interface Handler {
    /**
     * create proxy object
     *
     * @return proxy object
     */
    Object proxy();

    /**
     * destroy target object
     *
     * @throws Throwable exceptions
     */
    void destroy() throws Throwable;
}
