package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;

import com.google.common.collect.Maps;

import lemon.needle.exception.NeedleException;
import lemon.needle.ioc.binder.Binder;

public abstract class AbsModule implements Module {

    private List<Binder<?>> binders = new ArrayList<Binder<?>>();
    private Set<Class<? extends Annotation>> qualifiers = new HashSet<Class<? extends Annotation>>();

    //    private Map<Class<?>, GenericTypedBeanLoader<?>> genericTypedBeanLoaders = new HashMap<Class<?>, GenericTypedBeanLoader<?>>();
    //    private boolean configured;

    protected final <T> Binder<T> bind(Class<T> type) {
        Binder<T> binder = new Binder<T>(type);
        binders.add(binder);
        return binder;
    }

    /**
     * 
     * 定义Aop功能
     * @param classMatcher  过滤类 
     * @param methodMatcher 过滤方法
     * @param interceptors  方法拦截器
     */
    protected void bindInterceptor(Matcher<? super Class<?>> classMatcher, Matcher<? super Method> methodMatcher, MethodInterceptor... interceptors) {

    }

    protected final AbsModule registerQualifiers(Class<? extends Annotation>... qualifiers) {
        this.qualifiers.addAll(Arrays.asList(qualifiers));
        return this;
    }

    public abstract void configure();

    final void applyTo(InjectorImpl injector) {
        configure();
        validate(injector);
        for (Binder<?> binder : binders) {
            binder.register(injector);
        }
    }

    private void validate(InjectorImpl injector) {
        Map<Object, Binder<?>> map = Maps.newConcurrentMap();
        for (Binder<?> binder : binders) {
            Key<?> spec = binder.key(injector);
            if (map.containsKey(spec)) {
                throw new NeedleException("Duplicate bean spec found: ", spec);
            }
            map.put(spec, binder);
        }
    }

}
