package lemon.needle.ioc.binder;

import java.util.Map;

import javax.inject.Provider;

import com.google.common.collect.Maps;

import lemon.needle.ioc.Key;
import lemon.needle.ioc.Module;

public class PrivateBinder<T> implements Binder {

    private Map<Key<?>, BindingBuilder<?>> binders = Maps.newConcurrentMap();

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
        BindingBuilder<T> oneBinder = new BindingBuilder<T>(key);
        binders.put(key, oneBinder);
        return oneBinder;
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    @Override
    public void requestInjection(Object instance) {

    }

    @Override
    public void requestStaticInjection(Class<?>... types) {

    }

    @Override
    public void install(Module module) {

    }

    @Override
    public <T> Provider<T> getProvider(Key<T> key) {
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return null;
    }

    @Override
    public Binder withSource(Object source) {
        return null;
    }

    @Override
    public Binder skipSources(Class... classesToSkip) {
        return null;
    }

    @Override
    public void requireExplicitBindings() {

    }

    @Override
    public void disableCircularProxies() {

    }

    @Override
    public void requireAtInjectOnConstructors() {

    }

    @Override
    public void requireExactBindingAnnotations() {

    }

    @Override
    public void scanModulesForAnnotatedMethods(ModuleAnnotatedMethodScanner scanner) {

    }

}
