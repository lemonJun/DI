package org.osgl.inject.provider;

import javax.inject.Provider;

import org.osgl.inject.Injector;

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
        return injector.get(clazz);
    }

}
