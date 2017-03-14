//package lemon.needle.ioc.scope;
//
//import java.lang.annotation.Annotation;
//
//import javax.inject.Provider;
//import javax.inject.Singleton;
//
//import org.osgl.inject.BeanSpec;
//import org.osgl.inject.Genie;
//import org.osgl.inject.annotation.RequestScoped;
//import org.osgl.inject.annotation.SessionScoped;
//
///**
// * Decorate on a {@link javax.inject.Provider} with scope cache
// * checking function
// */
//public class ScopedProvider<T> implements Provider<T> {
//
//    private Provider<T> realProvider;
//    private BeanSpec spec;
//    private ScopeCache cache;
//
//    private ScopedProvider(BeanSpec spec, ScopeCache cache, Provider<T> realProvider) {
//        this.spec = spec;
//        this.realProvider = realProvider;
//        this.cache = cache;
//    }
//
//    @Override
//    public T get() {
//        T bean = cache.get(spec);
//        if (null == bean) {
//            bean = realProvider.get();
//        }
//        cache.put(spec, bean);
//        return bean;
//    }
//
//    @SuppressWarnings("unchecked")
//    public static <T> Provider<T> decorate(BeanSpec spec, Provider<T> realProvider, Genie genie) {
//        if (realProvider instanceof ScopedProvider) {
//            return realProvider;
//        }
//        Class<T> targetClass = spec.rawType();
//        ScopeCache cache = resolve(spec.scope(), genie);
//
//        return null == cache ? realProvider : new ScopedProvider<T>(spec, cache, realProvider);
//    }
//
//    static ScopeCache resolve(Class<? extends Annotation> annoClass, Genie genie) {
//        ScopeCache cache = resolveBuiltIn(annoClass, genie);
//        if (null != cache) {
//            return cache;
//        }
//        Class<? extends Annotation> alias = genie.scopeByAlias(annoClass);
//        if (null != alias) {
//            cache = resolveBuiltIn(alias, genie);
//        }
//        if (null == cache) {
//            cache = genie.scopeCache(annoClass);
//        }
//        return cache;
//    }
//
//    private static ScopeCache resolveBuiltIn(Class<? extends Annotation> annoClass, Genie genie) {
//        if (Singleton.class == annoClass) {
//            return new SingletonScopeImpl();
//        } else if (RequestScoped.class == annoClass) {
//            return genie.get(ScopeCache.RequestScope.class);
//        } else if (SessionScoped.class == annoClass) {
//            return genie.get(ScopeCache.SessionScope.class);
//        }
//        return null;
//    }
//}
