package lemon.needle.aop;

import com.google.common.collect.Lists;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.sf.cglib.proxy.MethodProxy;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Intercepts a method with a stack of interceptors.
 *
 * @author crazybob@google.com (Bob Lee)
 */
final class InterceptorStackCallback implements net.sf.cglib.proxy.MethodInterceptor {
    private static final Set<String> AOP_INTERNAL_CLASSES = new HashSet<String>(Arrays.asList(InterceptorStackCallback.class.getName(), InterceptedMethodInvocation.class.getName(), MethodProxy.class.getName()));

    final MethodInterceptor[] interceptors;
    final Method method;
    
    public InterceptorStackCallback(Method method, List<MethodInterceptor> interceptors) {
        this.method = method;
        this.interceptors = interceptors.toArray(new MethodInterceptor[interceptors.size()]);
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] arguments, MethodProxy methodProxy) throws Throwable {
        return new InterceptedMethodInvocation(proxy, methodProxy, arguments, 0).proceed();
    }

    private class InterceptedMethodInvocation implements MethodInvocation {

        final Object proxy;
        final Object[] arguments;
        final MethodProxy methodProxy;
        final int index;

        public InterceptedMethodInvocation(Object proxy, MethodProxy methodProxy, Object[] arguments, int index) {
            this.proxy = proxy;
            this.methodProxy = methodProxy;
            this.arguments = arguments;
            this.index = index;
        }

        @Override
        public Object proceed() throws Throwable {
            try {
                return index == interceptors.length ? methodProxy.invokeSuper(proxy, arguments) : interceptors[index].invoke(new InterceptedMethodInvocation(proxy, methodProxy, arguments, index + 1));
            } catch (Throwable t) {
                pruneStacktrace(t);
                throw t;
            }
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return arguments;
        }

        @Override
        public Object getThis() {
            return proxy;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return getMethod();
        }
    }

    /**
     * Removes stacktrace elements related to AOP internal mechanics from the throwable's stack trace
     * and any causes it may have.
     */
    private void pruneStacktrace(Throwable throwable) {
        for (Throwable t = throwable; t != null; t = t.getCause()) {
            StackTraceElement[] stackTrace = t.getStackTrace();
            List<StackTraceElement> pruned = Lists.newArrayList();
            for (StackTraceElement element : stackTrace) {
                String className = element.getClassName();
                if (!AOP_INTERNAL_CLASSES.contains(className) && !className.contains("$EnhancerByGuice$")) {
                    pruned.add(element);
                }
            }
            t.setStackTrace(pruned.toArray(new StackTraceElement[pruned.size()]));
        }
    }
}
