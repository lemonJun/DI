package lemon.needle.ioc.provider;

import javax.inject.Provider;

/**
 * 单例对象的{@link Provider}封装形式。
 */
public class SingleProvider<T> implements Provider<T> {
    private Provider<T> provider = null;
    private volatile T instance = null;
    private final Object lock = new Object();

    //
    public SingleProvider(Provider<T> provider) {
        this.provider = provider;
    }

    public T get() {
        if (this.instance == null) {
            synchronized (this.lock) {
                if (this.instance == null) {
                    this.instance = this.provider.get();
                }
            }
        }
        return this.instance;
    }

    public String toString() {
        return "SingleProvider->" + provider.toString();
    }
}