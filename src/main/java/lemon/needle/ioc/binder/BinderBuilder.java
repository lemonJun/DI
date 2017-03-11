package lemon.needle.ioc.binder;

import static com.google.common.base.Preconditions.checkNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import javax.inject.Provider;

import lemon.needle.ioc.Key;
import lemon.needle.ioc.TypeLiteral;
import lemon.needle.ioc.scope.Scope;

/**
 * 实现绑定功能时   所面的内部对象
 * TODO:
 * 1.为一个BIND指定一个注解
 * 2.
 *
 * @author jessewilson@google.com (Jesse Wilson)
 */
public class BinderBuilder<T> implements AnnotatedBindingBuilder<T> {

    private Key<?> key;//原始key
    private Key<?> targetKey;//绑定到其它key
    private Provider<?> provider;

    public BinderBuilder(Key<?> key) {
        this.key = key;
        this.targetKey = null;
        this.provider = null;
    }

    @Override
    public BinderBuilder<T> to(Class<? extends T> implementation) {
        return to(Key.of(implementation));
    }

    @Override
    public BinderBuilder<T> to(Key<? extends T> linkedKey) {
        checkNotNull(linkedKey, "linkedKey");
        this.targetKey = linkedKey;
        return this;
    }

    @Override
    public void toInstance(final T instance) {
        Provider<?> provider = new Provider<T>() {
            @Override
            public T get() {
                return instance;
            }
        };
        this.provider = provider;
    }

    @Override
    public BinderBuilder<T> toProvider(Provider<? extends T> provider) {
        checkNotNull(provider, "provider");
        this.provider = provider;
        return this;
    }

    @Override
    public BinderBuilder<T> toProvider(Class<? extends javax.inject.Provider<? extends T>> providerType) {
        return toProvider(Key.of(providerType));
    }

    @Override
    public BinderBuilder<T> toProvider(Key<? extends javax.inject.Provider<? extends T>> providerKey) {
        checkNotNull(providerKey, "providerKey");
        return this;
    }

    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor) {
        return toConstructor(constructor, TypeLiteral.get(constructor.getDeclaringClass()));
    }

    @Override
    public BinderBuilder<T> annotatedWith(Class<? extends Annotation> qualifier) {
        this.key = Key.of(key, qualifier);
        return this;
    }

    @Override
    public BinderBuilder<T> annotatedWith(Annotation annotation) {
        return this;
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

    //第三方类库 不能加入@Provides注解  
    @Override
    public <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor, TypeLiteral<? extends S> type) {
        return null;
    }

    public Key<?> getKey() {
        return key;
    }

    public Key<?> getTargetKey() {
        return targetKey;
    }

    public Provider<?> getProvider() {
        return provider;
    }

}
