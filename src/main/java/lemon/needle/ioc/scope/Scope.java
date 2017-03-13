package lemon.needle.ioc.scope;

import javax.inject.Provider;

import lemon.needle.ioc.InjectorImpl;
import lemon.needle.ioc.Key;

/**
 * 
 * A scope is a level of visibility that instances provided by Guice may have. By default, an
 * instance created by the {@link InjectorImpl} has <i>no scope</i>, meaning it has no state from the
 * framework's perspective -- the {@code Injector} creates it, injects it once into the class that
 * required it, and then immediately forgets it. Associating a scope with a particular binding
 * allows the created instance to be "remembered" and possibly used again for other injections.
 *
 * <p>An example of a scope is {@link Scopes#SINGLETON}.
 *
 * @author crazybob@google.com (Bob Lee)
 */
public interface Scope {

    /**
     * Scopes a provider. The returned provider returns objects from this scope. If an object does not
     * exist in this scope, the provider can use the given unscoped provider to retrieve one.
     *
     * <p>Scope implementations are strongly encouraged to override {@link Object#toString} in the
     * returned provider and include the backing provider's {@code toString()} output.
     *
     * @param key binding key
     * @param unscoped locates an instance when one doesn't already exist in this scope.
     * @return a new provider which only delegates to the given unscoped provider when an instance of
     *     the requested object doesn't already exist in this scope
     */
    public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped);

    /**
     * A short but useful description of this scope. For comparison, the standard scopes that ship
     * with guice use the descriptions {@code "Scopes.SINGLETON"}, {@code "ServletScopes.SESSION"} and
     * {@code "ServletScopes.REQUEST"}.
     */
    @Override
    String toString();
}
