package lemon.needle.aop;

import java.lang.reflect.Constructor;

/**
 * Creates {@link ConstructionProxy} instances.
 *
 * @author crazybob@google.com (Bob Lee)
 */
interface ConstructionProxyFactory<T> {
    /** Gets a construction proxy for the given constructor. */
    ConstructionProxy<T> create(Constructor<T> contructor) throws Exception;
}
