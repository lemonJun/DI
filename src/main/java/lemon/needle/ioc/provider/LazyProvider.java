package lemon.needle.ioc.provider;

import javax.inject.Provider;

import lemon.needle.ioc.Injector;

/**
 * eager op
 * A lazy provider is prepared with the bean class and initialize
 * the bean upon demand
 */
public class LazyProvider<T> implements Provider<T> {

    private Class<? extends T> clazz;
    private Injector injector;

    public LazyProvider(Class<? extends T> clazz, Injector injector) {
        this.clazz = clazz;
        this.injector = injector;
    }

    @Override
    public T get() {
        return injector.instance(clazz);
    }

}
