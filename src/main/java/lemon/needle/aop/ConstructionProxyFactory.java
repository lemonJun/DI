package lemon.needle.aop;

/**
 * Creates {@link ConstructionProxy} instances.
 *
 * @author crazybob@google.com (Bob Lee)
 */
interface ConstructionProxyFactory<T> {

    /** Gets a construction proxy for the given constructor. */
    ConstructionProxy<T> create() throws Exception;
}
