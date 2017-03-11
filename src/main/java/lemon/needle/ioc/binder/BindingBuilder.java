package lemon.needle.ioc.binder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import javax.inject.Provider;

import lemon.needle.ioc.Key;
import lemon.needle.ioc.TypeLiteral;

/**
 * Bind a non-constant key.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
public class BindingBuilder<T> implements AnnotatedBindingBuilder<T> {

    private Key<?> key;//原始key
    private Key<?> targetKey;//绑定到其它key
    private Provider<?> provider;
    private T t;

    public BindingBuilder(Key<?> key) {
        this.key = key;
    }

    @Override
    public BindingBuilder<T> annotatedWith(Class<? extends Annotation> annotationType) {
        //        annotatedWithInternal(annotationType);
        return this;
    }

    @Override
    public BindingBuilder<T> annotatedWith(Annotation annotation) {
        //        annotatedWithInternal(annotation);
        return this;
    }

    @Override
    public BindingBuilder<T> to(Class<? extends T> implementation) {
        return to(Key.of(implementation));
    }

    @Override
    public BindingBuilder<T> to(Key<? extends T> linkedKey) {
        checkNotNull(linkedKey, "linkedKey");
        this.targetKey = linkedKey;
        //        checkNotTargetted();
        //        BindingImpl<T> base = getBinding();
        //        setBinding(new LinkedBindingImpl<T>(base.getSource(), base.getKey(), base.getScoping(), linkedKey));
        return this;
    }

    @Override
    public void toInstance(T instance) {
        //        checkNotTargetted();
        this.t = instance;
    }

    @Override
    public BindingBuilder<T> toProvider(Provider<? extends T> provider) {
        checkNotNull(provider, "provider");
        this.provider = provider;
        return this;
    }

    @Override
    public BindingBuilder<T> toProvider(Class<? extends javax.inject.Provider<? extends T>> providerType) {
        return toProvider(Key.of(providerType));
    }

    @Override
    public BindingBuilder<T> toProvider(Key<? extends javax.inject.Provider<? extends T>> providerKey) {
        checkNotNull(providerKey, "providerKey");
        return this;
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
        return toConstructor(constructor, TypeLiteral.get(constructor.getDeclaringClass()));
    }

    @Override
    public String toString() {
        return "BindingBuilder<" + key.toString() + ">";
    }

    @Override
    public ScopedBindingBuilder to(TypeLiteral<? extends T> implementation) {
        return null;
    }

    @Override
    public void in(Class<? extends Annotation> scopeAnnotation) {

    }

    @Override
    public void in(Scope scope) {

    }

    @Override
    public void asEagerSingleton() {

    }

    @Override
    public ScopedBindingBuilder toProvider(TypeLiteral<? extends Provider<? extends T>> providerType) {
        return null;
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> type) {
        return null;
    }

}
