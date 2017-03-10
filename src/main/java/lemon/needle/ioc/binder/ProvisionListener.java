package lemon.needle.ioc.binder;
//package lemon.ioc.di.binder;
//
//import java.util.List;
//
//import lemon.ioc.di.Provider;
//
///**
// * Listens for provisioning of objects. Useful for gathering timing information
// * about provisioning, post-provision initialization, and more.
// * 
// * @author sameb@google.com (Sam Berlin)
// * @since 4.0
// */
//public interface ProvisionListener {
//
//    /**
//     * Invoked by Guice when an object requires provisioning. Provisioning occurs
//     * when Guice locates and injects the dependencies for a binding. For types
//     * bound to a Provider, provisioning encapsulates the {@link Provider#get}
//     * method. For toInstance or constant bindings, provisioning encapsulates
//     * the injecting of {@literal @}{@code Inject}ed fields or methods.
//     * For other types, provisioning encapsulates the construction of the
//     * object. If a type is bound within a {@link Scope}, provisioning depends on
//     * the scope. Types bound in Singleton scope will only be provisioned once.
//     * Types bound in no scope will be provisioned every time they are injected.
//     * Other scopes define their own behavior for provisioning.
//     * <p>
//     * To perform the provision, call {@link ProvisionInvocation#provision()}.
//     * If you do not explicitly call provision, it will be automatically done after
//     * this method returns.  It is an error to call provision more than once.
//     */
//    <T> void onProvision(ProvisionInvocation<T> provision);
//
//    /**
//     * Encapsulates a single act of provisioning.
//     *
//     * @since 4.0
//     */
//    public abstract static class ProvisionInvocation<T> {
//
//        /**
//         * Returns the Binding this is provisioning.
//         * <p>
//         * You must not call {@link Provider#get()} on the provider returned by
//         * {@link Binding#getProvider}, otherwise you will get confusing error messages.
//         */
//        public abstract Binding<T> getBinding();
//
//        /** Performs the provision, returning the object provisioned. */
//        public abstract T provision();
//
//        /** Returns the dependency chain that led to this object being provisioned. */
//        public abstract List<DependencyAndSource> getDependencyChain();
//
//    }
//}
