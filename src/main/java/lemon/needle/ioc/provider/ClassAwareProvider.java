package lemon.needle.ioc.provider;

import javax.inject.Provider;

import lemon.needle.ioc.AppContext;
import lemon.needle.ioc.AppContextAware;

/**
 * 注意事项：只可以在 AppContext init 期间使用。
 */
public class ClassAwareProvider<T> implements Provider<T>, AppContextAware {
    private Class<? extends T> implementation;
    private AppContext appContext;

    public ClassAwareProvider(Class<? extends T> implementation) {
        //        this.implementation = Hasor.assertIsNotNull(implementation);
    }

    @Override
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
    }

    @Override
    public T get() {
        if (this.appContext != null && this.implementation != null) {
            return this.appContext.getInstance(this.implementation);
        }
        throw new IllegalStateException("has not been initialized");
    }
}