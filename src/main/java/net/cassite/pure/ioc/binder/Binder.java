package net.cassite.pure.ioc.binder;

import net.cassite.pure.ioc.Injector;
import net.cassite.pure.ioc.Provider;

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
