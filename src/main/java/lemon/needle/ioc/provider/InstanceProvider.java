package lemon.needle.ioc.provider;

import javax.inject.Provider;

/**
 * 对象的{@link Provider}封装形式。
 */
public class InstanceProvider<T> implements Provider<T> {
    private T instance = null;

    public InstanceProvider(final T instance) {
        this.instance = instance;
    }

    public T get() {
        return this.instance;
    }

    //
    public void set(T instance) {
        this.instance = instance;
    }
}