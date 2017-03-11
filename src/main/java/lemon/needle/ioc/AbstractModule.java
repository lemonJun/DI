package lemon.needle.ioc;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import javax.inject.Provider;

import lemon.needle.ioc.binder.AnnotatedBindingBuilder;
import lemon.needle.ioc.binder.Binder;
import lemon.needle.ioc.binder.LinkedBindingBuilder;

/**
 * A support class for {@link Module}s which reduces repetition and results in
 * a more readable configuration. Simply extend this class, implement {@link
 * #configure()}, and call the inherited methods which mirror those found in
 * {@link Binder}. For example:
 *
 * <pre>
 * public class MyModule extends AbstractModule {
 *   protected void configure() {
 *     bind(Service.class).to(ServiceImpl.class).in(Singleton.class);
 *     bind(CreditCardPaymentService.class);
 *     bind(PaymentService.class).to(CreditCardPaymentService.class);
 *     bindConstant().annotatedWith(Names.named("port")).to(8080);
 *   }
 * }
 * </pre>
 *
 * @author crazybob@google.com (Bob Lee)
 */
public abstract class AbstractModule implements Module {

    Binder binder;
    
    public final synchronized void configure(Binder builder) {
        checkState(this.binder == null, "Re-entry is not allowed.");

        this.binder = checkNotNull(builder, "builder");
        try {
            configure();
        } finally {
            this.binder = null;
        }
    }

    //Configures a {@link Binder} via the exposed methods.
    protected abstract void configure();

    //Gets direct access to the underlying {@code Binder}.
    protected Binder binder() {
        checkState(binder != null, "The binder can only be used inside configure()");
        return binder;
    }

    //
    protected <T> AnnotatedBindingBuilder<T> bind(Class<T> clazz) {
        return binder.bind(clazz);
    }

    protected <T> LinkedBindingBuilder<T> bind(Key<T> key) {
        return binder.bind(key);
    }

    //
    protected void requireBinding(Class<?> type) {
        binder.getProvider(type);
    }

    //
    protected <T> Provider<T> getProvider(Key<T> key) {
        return binder.getProvider(key);
    }

    //
    protected <T> Provider<T> getProvider(Class<T> type) {
        return binder.getProvider(type);
    }

}
