package lemon.ioc.dinjection.binder;

import java.lang.annotation.Annotation;
import java.util.Set;

import lemon.ioc.dinjection.Key;

/**
 * Allows extensions to scan modules for annotated methods and bind those methods
 * as providers, similar to {@code @Provides} methods.
 *
 * @since 4.0
 */
public abstract class ModuleAnnotatedMethodScanner {

    /**
     * Returns the annotations this should scan for. Every method in the module that has one of these
     * annotations will create a Provider binding, with the return value of the binding being what's
     * provided and the parameters of the method being dependencies of the provider.
     */
    public abstract Set<? extends Class<? extends Annotation>> annotationClasses();

    /**
     * Prepares a method for binding. This {@code key} parameter is the key discovered from looking at
     * the binding annotation and return value of the method. Implementations can modify the key to
     * instead bind to another key. For example, Multibinder may want to change
     * {@code @SetProvides String provideFoo()} to bind into a unique Key within the multibinder
     * instead of binding {@code String}.
     *
     * <p>The injection point and annotation are provided in case the implementation wants to set the
     * key based on the property of the annotation or if any additional preparation is needed for any
     * of the dependencies. The annotation is guaranteed to be an instance of one the classes returned
     * by {@link #annotationClasses}.
     */
    //    public abstract <T> Key<T> prepareMethod(Binder binder, Annotation annotation, Key<T> key, InjectionPoint injectionPoint);

}
