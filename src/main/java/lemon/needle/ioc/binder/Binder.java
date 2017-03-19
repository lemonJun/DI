package lemon.needle.ioc.binder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;

import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Scope;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.common.base.Preconditions;

import lemon.needle.exception.NeedleException;
import lemon.needle.ioc.InjectorImpl;
import lemon.needle.ioc.Key;
import lemon.needle.ioc.Matcher;
import lemon.needle.ioc.provider.LazyProvider;
import lemon.needle.util.CommonUtil;

public class Binder<T> {
    private Class<T> type;
    private String name;
    private Provider<? extends T> provider;

    //要求注解归属于Qualifier注解  如Named
    private Class<? extends Annotation> qualifier;
    //
    private Class<? extends Annotation> scope;
    @SuppressWarnings("unused")
    private boolean forceFireEvent;
    @SuppressWarnings("unused")
    private boolean fireEvent;
    private Constructor<? extends T> constructor;
    private Class<? extends T> impl;

    public Binder(Class<T> type) {
        this.type = type;
        this.fireEvent = true;
    }

    public Binder<T> named(String name) {
        Preconditions.checkState(null != this.name, "name has already been specified");
        this.name = name;
        this.fireEvent = false;
        return this;
    }

    public Binder<T> to(final Class<? extends T> impl) {
        ensureNoBinding();
        Preconditions.checkNotNull(impl);
        this.impl = impl;
        return this;
    }

    public Binder<T> to(final T instance) {
        ensureNoBinding();
        this.provider = new Provider<T>() {
            @Override
            public T get() {
                return instance;
            }
        };
        return this;
    }

    public Binder<T> to(Provider<? extends T> provider) {
        ensureNoBinding();
        this.provider = provider;
        return this;
    }

    public Binder<T> to(final Constructor<? extends T> constructor) {
        ensureNoBinding();
        this.constructor = constructor;
        return this;
    }

    public Binder<T> toConstructor(Class<? extends T> implement, Class<?>... args) {
        ensureNoBinding();
        try {
            return to(implement.getConstructor(args));
        } catch (NoSuchMethodException e) {
            throw new NeedleException(e, "cannot find constructor for %s with arguments: %s", implement.getName(), CommonUtil.toString2(args));
        }
    }

    private void ensureNoBinding() {
        Preconditions.checkState(!bound(), "binding has already been specified");
    }

    /**
     * Specify the bind belongs to a certain scope
     * @param scope the scope annotation class
     * @return this binder instance
     */
    public Binder<T> in(Class<? extends Annotation> scope) {
        if (!scope.isAnnotationPresent(Scope.class)) {
            throw new NeedleException("Annotation class passed to \"in\" method must have @Scope annotation presented");
        }
        Preconditions.checkState(null != this.scope, "Scope has already been specified");
        this.scope = scope;
        this.fireEvent = false;
        return this;
    }

    /**
     * Specify the bind that should attach to bean that has been annotated with annotation(s). Usually
     * the annotation specified in the parameter should be {@link Qualifier qualifiers}
     *
     * @param annotations an array of annotation classes
     * @return this binder instance
     * @see Qualifier
     */
    public Binder<T> withAnnotation(Class<? extends Annotation> annotation) {
        this.qualifier = annotation;
        this.fireEvent = false;
        return this;
    }

    public Binder<T> forceFireEvent() {
        this.forceFireEvent = true;
        this.fireEvent = true;
        return this;
    }

    public Binder<T> doNotFireEvent() {
        this.forceFireEvent = false;
        this.fireEvent = false;
        return this;
    }

    /**
     * 
     */
    public void in(Scope scope) {

    }

    /**
     * 
     * Instructs the {@link com.google.inject.Injector} to eagerly initialize this
     * singleton-scoped binding upon creation. Useful for application
     * initialization logic.  See the EDSL examples at
     * {@link com.google.inject.Binder}.
     */
    public void asEagerSingleton() {

    }

    boolean bound() {
        return null != provider || null != constructor || impl != null;
    }

    public void register(InjectorImpl injector) {
        Key<?> key = Key.of(type, qualifier, name);
        if (null == provider) {
            if (null != constructor) {
                provider = injector.buildConstructor(key, constructor, new HashSet<Key>());
            } else if (null != impl) {
                provider = new LazyProvider<>(impl, injector);
            }
        }
        if (!bound()) {
            throw new NeedleException("Cannot register without binding specified");
        }
    }

    public Key<?> key(InjectorImpl injector) {
        Key<?> key = Key.of(type, qualifier, name);
        key.setScope(scope);
        return key;
    }
}
