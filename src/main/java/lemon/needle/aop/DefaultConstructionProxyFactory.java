package lemon.needle.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Produces construction proxies that invoke the class constructor.
 *
 * @author crazybob@google.com (Bob Lee)
 */
final class DefaultConstructionProxyFactory<T> implements ConstructionProxyFactory<T> {

    /** @param injectionPoint an injection point whose member is a constructor of {@code T}. */
    DefaultConstructionProxyFactory() {
    }

    @Override
    public ConstructionProxy<T> create() {
        @SuppressWarnings("unchecked") // the injection point is for a constructor of T
        //        final Constructor<T> constructor = (Constructor<T>) injectionPoint.getMember();
        final Constructor<T> constructor = null;// (Constructor<T>) injectionPoint.getMember();

        /*if[AOP]*/
        try {
            net.sf.cglib.reflect.FastClass fc = BytecodeGen.newFastClassForMember(constructor);
            if (fc != null) {
                int index = fc.getIndex(constructor.getParameterTypes());
                // We could just fall back to reflection in this case but I believe this should actually
                // be impossible.
                Preconditions.checkArgument(index >= 0, "Could not find constructor %s in fast class", constructor);
                return new FastClassProxy<T>(constructor, fc, index);
            }
        } catch (net.sf.cglib.core.CodeGenerationException e) {
            /* fall-through */
        }
        /*end[AOP]*/
        return new ReflectiveProxy<T>(constructor);
    }

    //这个是利用cglib的fastclass
    /*if[AOP]*/
    /** A {@link ConstructionProxy} that uses FastClass to invoke the constructor. */
    private static final class FastClassProxy<T> implements ConstructionProxy<T> {
        final Constructor<T> constructor;
        final net.sf.cglib.reflect.FastClass fc;
        final int index;

        private FastClassProxy(Constructor<T> constructor, net.sf.cglib.reflect.FastClass fc, int index) {
            this.constructor = constructor;
            this.fc = fc;
            this.index = index;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T newInstance(Object... arguments) throws InvocationTargetException {
            // Use this method instead of FastConstructor to save a stack frame
            return (T) fc.newInstance(index, arguments);
        }

        @Override
        public Constructor<T> getConstructor() {
            return constructor;
        }

        @Override
        public ImmutableMap<Method, List<org.aopalliance.intercept.MethodInterceptor>> getMethodInterceptors() {
            return ImmutableMap.of();
        }
    }
    /*end[AOP]*/

    //使用反射
    private static final class ReflectiveProxy<T> implements ConstructionProxy<T> {
        final Constructor<T> constructor;

        ReflectiveProxy(Constructor<T> constructor) {
            if (!Modifier.isPublic(constructor.getDeclaringClass().getModifiers()) || !Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            this.constructor = constructor;
        }

        @Override
        public T newInstance(Object... arguments) throws InvocationTargetException {
            try {
                return constructor.newInstance(arguments);
            } catch (InstantiationException e) {
                throw new AssertionError(e); // shouldn't happen, we know this is a concrete type
            } catch (IllegalAccessException e) {
                throw new AssertionError(e); // a security manager is blocking us, we're hosed
            }
        }

        @Override
        public Constructor<T> getConstructor() {
            return constructor;
        }

        /*if[AOP]*/
        @Override
        public ImmutableMap<Method, List<org.aopalliance.intercept.MethodInterceptor>> getMethodInterceptors() {
            return ImmutableMap.of();
        }
        /*end[AOP]*/
    }
}
