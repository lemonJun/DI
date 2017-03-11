package lemon.needle.ioc.binder;

import java.lang.reflect.Method;
import java.util.Map;

import javax.inject.Provider;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.common.collect.Maps;

import lemon.needle.ioc.Injector;
import lemon.needle.ioc.Key;
import lemon.needle.ioc.Matcher;
import lemon.needle.ioc.Module;
import lemon.needle.ioc.util.CollectionUtil;

public class PrivateBinder<T> implements Binder {

    private final Map<Key<?>, BinderBuilder<?>> binders = Maps.newConcurrentMap();

    private final Injector injector;

    public PrivateBinder(Injector injector) {
        this.injector = injector;
    }

    public void initAllBinders() {
        if (CollectionUtil.isEmpty(binders)) {
            return;
        }
        binders.forEach((key, pro) -> {
            if (pro.getProvider() != null) {
                System.out.println("put" + pro.getProvider().toString());
                injector.getProviders().put(key, pro.getProvider());
            } else if (pro.getTargetKey() != null) {
                //此步会同时为bind 和  to生成绑定
                injector.getProviders().put(key, injector.provider(pro.getTargetKey()));
                System.out.println("put" + pro.getTargetKey());
            } else {

            }
        });
    }

    @Override
    public <T> AnnotatedBindingBuilder<T> bind(Key<T> key) {
        BinderBuilder<T> oneBinder = new BinderBuilder<T>(key);
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

    @Override
    public void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {

    }

}
