package lemon.ioc.di.binder;

import lemon.ioc.di.Injector;
import lemon.ioc.di.Provider;

public interface Binder {

    /**
     * Returns the provider used to obtain instances for the given injection type.
     * The returned provider will not be valid until the {@link Injector} has been
     * created. The provider will throw an {@code IllegalStateException} if you
     * try to use it beforehand.
     *
     * @since 2.0
     */
    <T> Provider<T> getProvider(Class<T> type);

}
