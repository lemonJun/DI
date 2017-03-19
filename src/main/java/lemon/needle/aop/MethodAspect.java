package lemon.needle.aop;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;

import lemon.needle.ioc.Matcher;

/**
 * Ties a matcher to a method interceptor.
 *
 * @author crazybob@google.com (Bob Lee)
 */
final class MethodAspect {

    private final Matcher<? super Class<?>> classMatcher;
    private final Matcher<? super Method> methodMatcher;
    private final List<MethodInterceptor> interceptors;

    /**
     * @param classMatcher matches classes the interceptor should apply to. For example: {@code
     *     only(Runnable.class)}.
     * @param methodMatcher matches methods the interceptor should apply to. For example: {@code
     *     annotatedWith(Transactional.class)}.
     * @param interceptors to apply
     */
    MethodAspect(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, List<MethodInterceptor> interceptors) {
        this.classMatcher = checkNotNull(classMatcher, "class matcher");
        this.methodMatcher = checkNotNull(methodMatcher, "method matcher");
        this.interceptors = checkNotNull(interceptors, "interceptors");
    }

    MethodAspect(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {
        this(classMatcher, methodMatcher, Arrays.asList(interceptors));
    }

    boolean matches(Class<?> clazz) {
        return classMatcher.matches(clazz);
    }

    boolean matches(Method method) {
        return methodMatcher.matches(method);
    }

    List<MethodInterceptor> interceptors() {
        return interceptors;
    }
}
