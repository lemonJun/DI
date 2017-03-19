package lemon.needle.ioc.scope;

import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Provider;
import javax.inject.Singleton;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.Key;
import lemon.needle.ioc.provider.SingleProvider;

/**
 * One instance per {@link Injector}. Also see {@code @}{@link Singleton}.
 *
 * <p>Introduction from the author: Implementation of this class seems unreasonably complicated at
 * the first sight. I fully agree with you, that the beast below is very complex and it's hard to
 * reason on how does it work or not. Still I want to assure you that hundreds(?) of hours were
 * thrown into making this code simple, while still maintaining Singleton contract.
 *
 * <p>Anyway, why is it so complex? Singleton scope does not seem to be that unique. 1) Guice has
 * never truly expected to be used in multi threading environment with many Injectors working
 * alongside each other. There is almost no code with Guice that propagates state between threads.
 * And Singleton scope is The exception. 2) Guice supports circular dependencies and thus manages
 * proxy objects. There is no interface that allows user defined Scopes to create proxies, it is
 * expected to be done by Guice. Singleton scope needs to be able to detect circular dependencies
 * spanning several threads, therefore Singleton scope needs to be able to create these proxies. 3)
 * To make things worse, Guice has a very tricky definition for a binding resolution when Injectors
 * are in in a parent/child relationship. And Scope does not have access to this information by
 * design, the only real action that Scope can do is to call or not to call a creator. 4) There is
 * no readily available code in Guice that can detect a potential deadlock, and no code for handling
 * dependency cycles spanning several threads. This is significantly harder as all the dependencies
 * in a thread at runtime can be represented with a list, where in a multi threaded environment we
 * have more complex dependency trees. 5) Guice has a pretty strong contract regarding Garbage
 * Collection, which often prevents us from linking objects directly. So simple domain specific code
 * can not be written and intermediary id objects need to be managed. 6) Guice is relatively fast
 * and we should not make things worse. We're trying our best to optimize synchronization for speed
 * and memory. Happy path should be almost as fast as in a single threaded solution and should not
 * take much more memory. 7) Error message generation in Guice was not meant to be used like this
 * and to work around its APIs we need a lot of code. Additional complexity comes from inherent data
 * races as message is only generated when failure occurs on proxy object generation. Things get
 * ugly pretty fast.
 *
 * @see #scope(Key, Provider)
 * @see CycleDetectingLock
 * @author timofeyb (Timothy Basanov)
 */
public class SingletonScope implements Scope {

    private final ConcurrentHashMap<Object, Provider<?>> scopeMap = new ConcurrentHashMap<Object, Provider<?>>();
    
    @Override
    public String toString() {
        return "Scopes.SINGLETON";
    }

    @Override
    public <T> Provider<T> scope(Key<T> key, Provider<T> provider) {
        Provider<?> returnData = this.scopeMap.get(key);
        if (returnData == null) {
            Provider<T> newSingleProvider = new SingleProvider<T>(provider);
            returnData = this.scopeMap.putIfAbsent(key, newSingleProvider);
            if (returnData == null) {
                returnData = newSingleProvider;
            }
        }
        return (Provider<T>) returnData;
    }
}
