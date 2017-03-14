package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;

import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Scope;

import com.google.common.base.Preconditions;

import lemon.needle.ioc.exception.NeedleException;
import lemon.needle.ioc.provider.LazyProvider;
import lemon.needle.ioc.util.CommonUtil;

public class Binder<T> {
    private Class<T> type;
    private String name;
    private Provider<? extends T> provider;
    private Class<? extends Annotation> qualifier;//先中实现一个名称绑定
    private Class<? extends Annotation> scope;
    private boolean forceFireEvent;
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
        //        for (Class<? extends Annotation> annotation : annotations) {
        //            this.annotations.add(AnnotationUtil.createAnnotation(annotation));
        //        }
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

    boolean bound() {
        return null != provider || null != constructor || impl != null;
    }

    public void register(InjectorImpl injector) {
        Key<?> key = Key.of(type, qualifier, name);
        if (null == provider) {
            if (null != constructor) {
                provider = injector.buildConstructor(constructor, key, new HashSet<Key>());
            } else if (null != impl) {
                provider = new LazyProvider<>(impl, injector);
            }
        }
        if (!bound()) {
            throw new NeedleException("Cannot register without binding specified");
        }
    }

    Key<?> key(InjectorImpl injector) {
        Key<?> key = Key.of(type, qualifier, name);
        key.setScope(scope);
        return key;
    }
}
