package org.osgl.inject;

import java.lang.reflect.Method;

import javax.inject.Provider;

public class MethodInjector {

    private final Method method;
    private final Provider[] providers;

    MethodInjector(Method method, Provider[] providers) {
        this.method = method;
        this.providers = providers;
    }

    Object applyTo(Object bean) {
        try {
            return method.invoke(bean, Genie.params(providers));
        } catch (Exception e) {
            throw new InjectException(e, "Unable to invoke method[%s] on %s", method.getName(), bean.getClass());
        }
    }

    @Override
    public String toString() {
        return String.format("MethodInjector for %s", method);
    }

    public Method getMethod() {
        return method;
    }

    public Provider[] getProviders() {
        return providers;
    }

}
