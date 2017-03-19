package lemon.needle.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * Proxies calls to a {@link java.lang.reflect.Constructor} for a class {@code T}.
 *
 * @author crazybob@google.com (Bob Lee)
 */
interface ConstructionProxy<T> {

    /** Constructs an instance of {@code T} for the given arguments. */
    T newInstance(Object... arguments) throws InvocationTargetException;

    /** Returns the injection point for this constructor. */
    InjectionPoint getInjectionPoint();

    /**
     * Returns the injected constructor. If the injected constructor is synthetic (such as generated
     * code for method interception), the natural constructor is returned.
     */
    Constructor<T> getConstructor();

    /*if[AOP]*/
    /** Returns the interceptors applied to each method, in order of invocation. */
    ImmutableMap<Method, List<org.aopalliance.intercept.MethodInterceptor>> getMethodInterceptors();
    /*end[AOP]*/
}
