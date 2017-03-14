package lemon.needle.ioc.scope;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import lemon.needle.ioc.Key;
import lemon.needle.ioc.scope.ScopeCache.SingletonScope;

public class SingletonScopeImpl<T> implements SingletonScope {

    private final ConcurrentMap<Key<T>, Object> beans = new ConcurrentHashMap<Key<T>, Object>();

    @Override
    public T get(Key clazz) {
        return (T) beans.getClass();
    }

    @Override
    public <T> void put(Key spec, T bean) {
        beans.putIfAbsent(spec, bean);
    }

}
