package org.osgl.inject.scope;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.osgl.inject.BeanSpec;
import org.osgl.inject.scope.ScopeCache.SingletonScope;

public class SingletonScopeImpl<T> implements SingletonScope {

    private final ConcurrentMap<BeanSpec, Object> beans = new ConcurrentHashMap<BeanSpec, Object>();

    @Override
    public T get(BeanSpec clazz) {
        return (T) beans.getClass();
    }

    @Override
    public <T> void put(BeanSpec spec, T bean) {
        beans.putIfAbsent(spec, bean);
    }
    
}
