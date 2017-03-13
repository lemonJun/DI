package org.osgl.inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Scope;

import org.osgl.inject.provider.LazyProvider;
import org.osgl.inject.util.AnnotationUtil;
import org.osgl.inject.util.CommonUtil;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import lemon.needle.ioc.util.StringUtil;

public class Binder<T> {
    private Class<T> type;
    private String name;
    private Provider<? extends T> provider;
    private List<Annotation> annotations = Lists.newArrayList();
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
            throw new InjectException(e, "cannot find constructor for %s with arguments: %s", implement.getName(), CommonUtil.toString2(args));
        }
    }

    private void ensureNoBinding() {
        Preconditions.checkState(bound(), "binding has already been specified");
    }

    /**
     * Specify the bind belongs to a certain scope
     * @param scope the scope annotation class
     * @return this binder instance
     */
    public Binder<T> in(Class<? extends Annotation> scope) {
        if (!scope.isAnnotationPresent(Scope.class)) {
            throw new InjectException("Annotation class passed to \"in\" method must have @Scope annotation presented");
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
    public Binder<T> withAnnotation(Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations) {
            this.annotations.add(AnnotationUtil.createAnnotation(annotation));
        }
        this.fireEvent = false;
        return this;
    }

    public Binder<T> withAnnotation(Annotation... annotations) {
        this.annotations.addAll(Arrays.asList(annotations));
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
        return null != provider || null != constructor;
    }

    public void register(Genie genie) {
        if (null == provider) {
            if (null != constructor) {
                provider = genie.buildConstructor(constructor, BeanSpec.of(constructor.getDeclaringClass(), null, genie), new HashSet<BeanSpec>());
            } else if (null != impl) {
                provider = new LazyProvider<>(impl, genie);
            }
        }
        if (!bound()) {
            throw new InjectException("Cannot register without binding specified");
        }
        BeanSpec spec = beanSpec(genie);
        genie.addIntoRegistry(spec, genie.decorate(spec, provider, true), annotations.isEmpty() && StringUtil.isBlank(name));
        if (fireEvent || forceFireEvent) {
            genie.fireProviderRegisteredEvent(type);
        }
    }

    BeanSpec beanSpec(Genie genie) {
        BeanSpec spec = BeanSpec.of(type, annotations.toArray(new Annotation[annotations.size()]), name, genie);
        if (scope != null) {
            spec.scope(scope);
        }
        return spec;
    }
}
