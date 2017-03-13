package org.osgl.inject;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.PostConstruct;
import javax.inject.Provider;

import org.osgl.inject.util.CommonUtil;

/**
 * Invoke bean's {@link javax.annotation.PostConstruct post construct methods}
 */
class PostConstructorInvoker<T> implements Provider<T> {

    private Provider<T> realProvider;
    private Method postConstructor;

    private PostConstructorInvoker(Provider<T> realProvider, Method postConstructor) {
        this.realProvider = realProvider;
        this.postConstructor = postConstructor;
    }

    @Override
    public T get() {
        T t = realProvider.get();
        CommonUtil.invokeVirtual(t, postConstructor);
        return t;
    }

    static <T> Provider<T> decorate(BeanSpec spec, Provider<T> realProvider, Genie genie) {
        if (realProvider instanceof PostConstructorInvoker) {
            return realProvider;
        }

        Method postConstructor = findPostConstructor(spec.rawType());
        return null == postConstructor ? realProvider : new PostConstructorInvoker<T>(realProvider, postConstructor);
    }

    private static Method findPostConstructor(Class<?> type) {
        for (Method m : type.getDeclaredMethods()) {
            if (Modifier.isStatic(m.getModifiers()) || Void.TYPE != m.getReturnType()) {
                continue;
            }
            if (m.isAnnotationPresent(PostConstruct.class)) {
                m.setAccessible(true);
                return m;
            }
        }
        Class<?> parent = type.getSuperclass();
        if (null != parent && Object.class != parent) {
            return findPostConstructor(parent);
        }
        return null;
    }

}
