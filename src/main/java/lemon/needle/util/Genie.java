package lemon.needle.util;
//package org.osgl.inject;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.AccessibleObject;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.lang.reflect.Type;
//import java.rmi.UnexpectedException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//
//import javax.inject.Inject;
//import javax.inject.Provider;
//import javax.inject.Qualifier;
//import javax.inject.Singleton;
//
//import org.osgl.inject.annotation.InjectTag;
//import org.osgl.inject.annotation.PostConstructProcess;
//import org.osgl.inject.annotation.Provides;
//import org.osgl.inject.annotation.RequestScoped;
//import org.osgl.inject.annotation.SessionScoped;
//import org.osgl.inject.provider.WeightedProvider;
//import org.osgl.inject.scope.ScopeCache;
//import org.osgl.inject.scope.ScopedProvider;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Sets;
//
//import lemon.needle.ioc.FieldInjector;
//import lemon.needle.ioc.MethodInjector;
//
//public final class Genie implements Injector {
//
//    static final Logger logger = LoggerFactory.getLogger(Genie.class);
//
//    private static final ThreadLocal<BeanSpec> TGT_SPEC = new ThreadLocal<BeanSpec>();
//    private static final ThreadLocal<Integer> AFFINITY = new ThreadLocal<Integer>();
//    private static final Provider<BeanSpec> BEAN_SPEC_PROVIDER = new Provider<BeanSpec>() {
//        @Override
//        public BeanSpec get() {
//            return TGT_SPEC.get();
//        }
//    };
//
//    private static final Provider[] NO_PROVIDER = new Provider[0];
//
//    private ConcurrentMap<BeanSpec, Provider<?>> registry = new ConcurrentHashMap<BeanSpec, Provider<?>>();
//    //    private ConcurrentMap<Class, Provider> expressRegistry = new ConcurrentHashMap<Class, Provider>();
//    private Set<Class<? extends Annotation>> qualifierRegistry = new HashSet<Class<? extends Annotation>>();
//    private Set<Class<? extends Annotation>> injectTagRegistry = new HashSet<Class<? extends Annotation>>();
//    private Map<Class<? extends Annotation>, Class<? extends Annotation>> scopeAliases = new HashMap<Class<? extends Annotation>, Class<? extends Annotation>>();
//    private Map<Class<? extends Annotation>, ScopeCache> scopeProviders = new HashMap<Class<? extends Annotation>, ScopeCache>();
//    private ConcurrentMap<Class<? extends Annotation>, PostConstructProcessor<?>> postConstructProcessors = new ConcurrentHashMap<Class<? extends Annotation>, PostConstructProcessor<?>>();
//    private ConcurrentMap<Class, BeanSpec> beanSpecLookup = new ConcurrentHashMap<Class, BeanSpec>();
//    private ConcurrentMap<Class, GenericTypedBeanLoader> genericTypedBeanLoaders = new ConcurrentHashMap<Class, GenericTypedBeanLoader>();
//    private List<InjectListener> listeners = new ArrayList<InjectListener>();
//    private boolean supportInjectionPoint = false;
//
//    Genie(AbsModule... modules) {
//        init(modules);
//    }
//
//    private Genie(InjectListener listener, AbsModule... modules) {
//        this.listeners.add(listener);
//        init(modules);
//    }
//
//    private void init(AbsModule... modules) {
//        if (modules.length > 0) {
//            List<AbsModule> list = Lists.newArrayList();
//            for (AbsModule module : list) {
//                registerModule(module);
//            }
//        }
//    }
//
//    public void supportInjectionPoint(boolean enabled) {
//        this.supportInjectionPoint = enabled;
//    }
//
//    public <T> T get(Class<T> type) {
//        return getProvider(type).get();
//    }
//
//    /**
//     * Check if a type has already been registered with a binding already
//     * @param type the class
//     * @return `true` if the type has already been registered to Genie with a binding
//     */
//    public boolean hasProvider(Class<?> type) {
//        return registry.containsKey(type);
//    }
//
//    @Override
//    public <T> Provider<T> getProvider(Class<T> type) {
//        //        Provider<?> provider = expressRegistry.get(type);
//        BeanSpec spec = BeanSpec.of(type, this);
//        Provider<?> provider = registry.get(spec);
//        if (null == provider) {
//            //            BeanSpec spec = beanSpecOf(type);
//            Set<BeanSpec> beanset = Sets.newHashSet();
//            provider = findProvider(spec, beanset);
//            registry.putIfAbsent(spec, provider);
//        }
//        return (Provider<T>) provider;
//    }
//
//    public <T> T get(BeanSpec beanSpec) {
//        Set<BeanSpec> beanset = Sets.newHashSet();
//        Provider provider = findProvider(beanSpec, beanset);
//        return (T) provider.get();
//    }
//
//    public <T> void registerProvider(Class<T> type, Provider<? extends T> provider) {
//        registerProvider(type, provider, true);
//    }
//
//    private <T> void registerProvider(Class<T> type, Provider<? extends T> provider, boolean fireEvent) {
//        AFFINITY.set(0);
//        bindProviderToClass(type, provider, fireEvent);
//    }
//
//    public void registerQualifiers(Class<? extends Annotation>... qualifiers) {
//        this.qualifierRegistry.addAll(Arrays.asList(qualifiers));
//    }
//
//    public void registerInjectTag(Class<? extends Annotation>... injectTags) {
//        this.injectTagRegistry.addAll(Arrays.asList(injectTags));
//    }
//
//    public void registerQualifiers(Collection<Class<? extends Annotation>> qualifiers) {
//        this.qualifierRegistry.addAll(qualifiers);
//    }
//
//    public void registerScopeAlias(Class<? extends Annotation> scopeAnnotation, Class<? extends Annotation> scopeAlias) {
//        scopeAliases.put(scopeAlias, scopeAnnotation);
//    }
//
//    public void registerScopeProvider(Class<? extends Annotation> scopeAnnotation, ScopeCache scopeCache) {
//        scopeProviders.put(scopeAnnotation, scopeCache);
//    }
//
//    public void registerScopeProvider(Class<? extends Annotation> scopeAnnotation, Class<? extends ScopeCache> scopeCacheClass) {
//        scopeProviders.put(scopeAnnotation, get(scopeCacheClass));
//    }
//
//    public void registerPostConstructProcessor(Class<? extends Annotation> annoClass, PostConstructProcessor<?> processor) {
//        postConstructProcessors.put(annoClass, processor);
//    }
//
//    public <T> void registerGenericTypedBeanLoader(Class<T> type, GenericTypedBeanLoader<T> loader) {
//        genericTypedBeanLoaders.put(type, loader);
//    }
//
//    public boolean isScope(Class<? extends Annotation> annoClass) {
//        if (Singleton.class == annoClass || SessionScoped.class == annoClass || RequestScoped.class == annoClass) {
//            return true;
//        }
//        return scopeAliases.containsKey(annoClass) || scopeProviders.containsKey(annoClass);
//    }
//
//    public boolean isQualifier(Class<? extends Annotation> annoClass) {
//        return qualifierRegistry.contains(annoClass) || annoClass.isAnnotationPresent(Qualifier.class);
//    }
//
//    public boolean isPostConstructProcessor(Class<? extends Annotation> annoClass) {
//        return postConstructProcessors.containsKey(annoClass) || annoClass.isAnnotationPresent(PostConstructProcess.class);
//    }
//
//    public Class<? extends Annotation> scopeByAlias(Class<? extends Annotation> alias) {
//        return scopeAliases.get(alias);
//    }
//
//    public ScopeCache scopeCache(Class<? extends Annotation> scope) {
//        return scopeProviders.get(scope);
//    }
//
//    @SuppressWarnings("rawtypes")
//    PostConstructProcessor postConstructProcessor(Annotation annotation) throws Exception {
//        Class<? extends Annotation> annoClass = annotation.annotationType();
//        PostConstructProcessor processor = postConstructProcessors.get(annoClass);
//        if (null == processor) {
//            if (!annoClass.isAnnotationPresent(PostConstructProcess.class)) {
//                throw new UnexpectedException(String.format("Cannot find PostConstructProcessor on %s", annoClass));
//            }
//            PostConstructProcess pcp = annoClass.getAnnotation(PostConstructProcess.class);
//            Class<? extends PostConstructProcessor> cls = pcp.value();
//            processor = get(cls);
//            PostConstructProcessor p2 = postConstructProcessors.putIfAbsent(annoClass, processor);
//            if (null != p2) {
//                processor = p2;
//            }
//        }
//        return processor;
//    }
//
//    @SuppressWarnings("rawtypes")
//    private void bindProviderToClass(Class<?> target, Provider<?> provider, boolean fireEvent) {
//        addIntoRegistry(target, provider);
//        AFFINITY.set(AFFINITY.get() + 1);
//        Class dad = target.getSuperclass();
//        if (null != dad && Object.class != dad) {
//            bindProviderToClass(dad, provider, fireEvent);
//        }
//        Class[] roles = target.getInterfaces();
//        if (null == roles) {
//            return;
//        }
//        for (Class role : roles) {
//            bindProviderToClass(role, provider, fireEvent);
//        }
//        if (fireEvent) {
//            fireProviderRegisteredEvent(target);
//        }
//    }
//
//    public void addIntoRegistry(BeanSpec spec, Provider<?> val, boolean addIntoExpressRegistry) {
//        WeightedProvider current = WeightedProvider.decorate(val, AFFINITY);
//        Provider<?> old = registry.get(spec);
//        if (null == old) {
//            registry.put(spec, current);
//            if (addIntoExpressRegistry) {
//                //                expressRegistry.put(spec.rawType(), current);
//            }
//            return;
//        }
//        String newName = providerName(current.getRealProvider());
//        if (old instanceof WeightedProvider) {
//            WeightedProvider weightedOld = (WeightedProvider) old;
//            String oldName = providerName(weightedOld.getRealProvider());
//            if (weightedOld.getAffinity() > current.getAffinity()) {
//                registry.put(spec, current);
//                if (addIntoExpressRegistry) {
//                    //                    expressRegistry.put(spec.rawType(), current);
//                }
//                if (logger.isTraceEnabled()) {
//                    logger.trace("Provider %s \n\tfor [%s] \n\tis replaced with: %s", oldName, spec, newName);
//                }
//            } else {
//                if (weightedOld.getAffinity() == 0 && current.getAffinity() == 0) {
//                    throw new InjectException("Provider has already registered for spec: %s", spec);
//                } else {
//                    if (logger.isTraceEnabled()) {
//                        logger.trace("Provider %s \n\t for [%s] \n\t cannot be replaced with: %s", oldName, spec, newName);
//                    }
//                }
//            }
//        } else {
//            throw new InjectException("Provider has already registered for spec: %s", spec);
//        }
//    }
//
//    private void addIntoRegistry(Class<?> type, Provider<?> val) {
//        addIntoRegistry(BeanSpec.of(type, this), val, true);
//    }
//
//    private void registerModule(AbsModule module) {
//        module.applyTo(this);
//
//        for (Method method : providers(module.getClass())) {
//            if (method.isAnnotationPresent(Provides.class)) {
//                method.setAccessible(true);
//                boolean isStatic = Modifier.isStatic(method.getModifiers());
//                registerFactoryMethod(isStatic ? null : module, method);
//            }
//        }
//    }
//
//    private static Set<Method> providers(Class<?> type) {
//        Class<?> current = type;
//        Set<Method> providers = new HashSet<>();
//        while (!current.equals(Object.class)) {
//            for (Method method : current.getDeclaredMethods()) {
//                if (method.isAnnotationPresent(Provides.class) && (type.equals(current) || !providerInSubClass(method, providers))) {
//                    method.setAccessible(true);
//                    providers.add(method);
//                }
//            }
//            current = current.getSuperclass();
//        }
//        return providers;
//    }
//
//    private static boolean providerInSubClass(Method method, Set<Method> discoveredMethods) {
//        for (Method discovered : discoveredMethods) {
//            if (discovered.getName().equals(method.getName()) && Arrays.equals(method.getParameterTypes(), discovered.getParameterTypes())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private void registerFactoryMethod(final Object instance, final Method factory) {
//        Type retType = factory.getGenericReturnType();
//        Annotation[] factoryAnnotations = factory.getAnnotations();
//        final BeanSpec spec = BeanSpec.of(retType, factoryAnnotations, this);
//        Set<BeanSpec> chain = Sets.newHashSet();
//        final MethodInjector methodInjector = methodInjector(factory, chain);
//        addIntoRegistry(spec, decorate(spec, new Provider() {
//            @Override
//            public Object get() {
//                return methodInjector.applyTo(instance);
//            }
//
//            @Override
//            public String toString() {
//                return String.format("%s::%s", instance.getClass().getName(), methodInjector.getMethod().getName());
//            }
//        }, true), factoryAnnotations.length == 0);
//        fireProviderRegisteredEvent(spec.rawType());
//    }
//
//    //
//    private Provider<?> findProvider(final BeanSpec spec, final Set<BeanSpec> chain) {
//
//        // try registry
//        Provider<?> provider = registry.get(spec);
//        if (null != provider) {
//            return provider;
//        }
//
//        // try without name
//        if (null != spec.name()) {
//            provider = registry.get(spec.withoutName());
//            if (null != provider) {
//                return provider;
//            }
//        }
//
//        // does it want to inject a Provider?
//        if (spec.isProvider() && !spec.typeParams().isEmpty()) {
//            provider = new Provider<Provider<?>>() {
//                @Override
//                public Provider<?> get() {
//                    return new Provider() {
//                        private volatile Provider realProvider;
//
//                        @Override
//                        public Object get() {
//                            if (null == realProvider) {
//                                synchronized (this) {
//                                    if (null == realProvider) {
//                                        Set<BeanSpec> chain = Sets.newHashSet();
//                                        realProvider = findProvider(spec.toProvidee(), chain);
//                                    }
//                                }
//                            }
//                            return realProvider.get();
//                        }
//                    };
//                }
//            };
//            registry.putIfAbsent(spec, provider);
//            return provider;
//        }
//
//        // does it require a value loading logic
//        if (spec.hasValueLoader()) {
//            provider = ValueLoaderFactory.create(spec, this);
//        } else {
//            // does it require an array
//            //            if (spec.isArray()) {
//            //                return ArrayProvider.of(spec, this);
//            //            }
//            // check if there is a generic typed bean loader
//            final GenericTypedBeanLoader loader = genericTypedBeanLoaders.get(spec.rawType());
//            if (null != loader) {
//                provider = new Provider<Object>() {
//                    @Override
//                    public Object get() {
//                        return loader.load(spec);
//                    }
//                };
//            }
//            if (null == provider) {
//                // build provider from constructor, field or method
//                if (spec.notConstructable()) {
//                    // does spec's bare class have provider?
//                    provider = registry.get(spec.rawTypeSpec());
//                    if (null == provider) {
//                        throw new InjectException("Cannot instantiate %s", spec);
//                    }
//                } else {
//                    if (BeanSpec.class == spec.rawType()) {
//                        return BEAN_SPEC_PROVIDER;
//                    }
//                    provider = buildProvider(spec, chain);
//                }
//            }
//        }
//
//        Provider<?> decorated = decorate(spec, provider, false);
//        registry.putIfAbsent(spec, decorated);
//        return decorated;
//    }
//
//    public Provider<?> decorate(final BeanSpec spec, Provider provider, final boolean isFactory) {
//        if (BEAN_SPEC_PROVIDER == provider) {
//            return provider;
//        }
//        final Provider postConstructed = PostConstructProcessorInvoker.decorate(spec, provider, this);
//        Provider eventFired = new Provider() {
//            @Override
//            public Object get() {
//                if (supportInjectionPoint && !isFactory) {
//                    TGT_SPEC.set(spec);
//                }
//                try {
//                    Object bean = postConstructed.get();
//                    fireInjectEvent(bean, spec);
//                    return bean;
//                } finally {
//                    if (supportInjectionPoint && !isFactory) {
//                        TGT_SPEC.remove();
//                    }
//                }
//            }
//        };
//        return ScopedProvider.decorate(spec, eventFired, this);
//    }
//
//    @SuppressWarnings("rawtypes")
//    private Provider buildProvider(final BeanSpec spec, Set<BeanSpec> chain) {
//        Class target = spec.rawType();
//        Constructor constructor = constructor(target);
//        return null != constructor ? buildConstructor(constructor, spec, chain) : buildFieldMethodInjector(target, spec, chain);
//    }
//
//    public Provider buildConstructor(final Constructor constructor, final BeanSpec spec, final Set<BeanSpec> chain) {
//        Type[] ta = constructor.getGenericParameterTypes();
//        Annotation[][] aaa = constructor.getParameterAnnotations();
//        final Provider[] pp = paramProviders(ta, aaa, chain);
//        return new Provider() {
//            @Override
//            public Object get() {
//                try {
//                    return constructor.newInstance(params(pp));
//                } catch (Exception e) {
//                    throw new InjectException(e, "cannot instantiate %s", spec);
//                }
//            }
//        };
//    }
//
//    public Provider<?> buildFieldMethodInjector(final Class target, final BeanSpec spec, Set<BeanSpec> chain) {
//        final List<FieldInjector> fieldInjectors = fieldInjectors(target, chain);
//        final List<MethodInjector> methodInjectors = methodInjectors(target, chain);
//        try {
//            final Constructor constructor = target.getDeclaredConstructor();
//            if (null == constructor) {
//                throw new InjectException("cannot instantiate %s: %s", spec, "no default constructor found");
//            }
//            constructor.setAccessible(true);
//            return new Provider() {
//                @Override
//                public Object get() {
//                    try {
//                        Object bean = constructor.newInstance();
//                        for (FieldInjector fj : fieldInjectors) {
//                            if (supportInjectionPoint) {
//                                TGT_SPEC.set(fj.getFieldSpec());
//                            }
//                            try {
//                                fj.applyTo(bean);
//                            } finally {
//                                if (supportInjectionPoint) {
//                                    TGT_SPEC.remove();
//                                }
//                            }
//                        }
//                        for (MethodInjector mj : methodInjectors) {
//                            mj.applyTo(bean);
//                        }
//                        return bean;
//                    } catch (RuntimeException e) {
//                        throw e;
//                    } catch (Exception e) {
//                        throw new InjectException(e, "cannot instantiate %s", spec);
//                    }
//                }
//            };
//        } catch (NoSuchMethodException e) {
//            throw new InjectException(e, "cannot instantiate %s", spec);
//        }
//    }
//
//    private Constructor constructor(Class target) {
//        Constructor[] ca = target.getDeclaredConstructors();
//        for (Constructor c : ca) {
//            if (subjectToInject(c)) {
//                c.setAccessible(true);
//                return c;
//            }
//        }
//        return null;
//    }
//
//    private List<FieldInjector> fieldInjectors(Class type, Set<BeanSpec> chain) {
//        Class<?> current = type;
//        List<FieldInjector> fieldInjectors = Lists.newArrayList();
//        while (null != current && !current.equals(Object.class)) {
//            for (Field field : current.getDeclaredFields()) {
//                if (subjectToInject(field)) {
//                    field.setAccessible(true);
//                    fieldInjectors.add(fieldInjector(field, chain));
//                }
//            }
//            current = current.getSuperclass();
//        }
//        return fieldInjectors;
//    }
//
//    private FieldInjector fieldInjector(Field field, Set<BeanSpec> chain) {
//        BeanSpec fieldSpec = BeanSpec.of(field, this);
//        if (chain.contains(fieldSpec)) {
//            foundCircularDependency(chain, fieldSpec);
//        }
//        return new FieldInjector(field, fieldSpec, findProvider(fieldSpec, chain(chain, fieldSpec)));
//    }
//
//    private List<MethodInjector> methodInjectors(Class type, Set<BeanSpec> chain) {
//        Class<?> current = type;
//        List<MethodInjector> methodInjectors = Lists.newArrayList();
//        while (null != current && !current.equals(Object.class)) {
//            for (Method method : current.getDeclaredMethods()) {
//                if (subjectToInject(method)) {
//                    method.setAccessible(true);
//                    methodInjectors.add(methodInjector(method, chain));
//                }
//            }
//            current = current.getSuperclass();
//        }
//        return methodInjectors;
//    }
//
//    private MethodInjector methodInjector(Method method, Set<BeanSpec> chain) {
//        Type[] paramTypes = method.getGenericParameterTypes();
//        int len = paramTypes.length;
//        Provider[] paramProviders = new Provider[len];
//        Annotation[][] aaa = method.getParameterAnnotations();
//        for (int i = 0; i < len; ++i) {
//            Type paramType = paramTypes[i];
//            BeanSpec paramSpec = BeanSpec.of(paramType, aaa[i], this);
//            if (chain.contains(paramSpec)) {
//                foundCircularDependency(chain, paramSpec);
//            }
//            paramProviders[i] = findProvider(paramSpec, chain(chain, paramSpec));
//        }
//        return new MethodInjector(method, paramProviders);
//    }
//
//    private static <T extends Annotation> T filterAnnotation(Annotation[] aa, Class<T> ac) {
//        for (Annotation a : aa) {
//            if (a.annotationType() == ac) {
//                return (T) a;
//            }
//        }
//        return null;
//    }
//
//    private Provider[] paramProviders(Type[] paramTypes, Annotation[][] aaa, Set<BeanSpec> chain) {
//        final int len = paramTypes.length;
//        Provider[] pa = new Provider[len];
//        for (int i = 0; i < len; ++i) {
//            Type type = paramTypes[i];
//            Annotation[] annotations = aaa[i];
//            final BeanSpec paramSpec = BeanSpec.of(type, annotations, this);
//            if (chain.contains(paramSpec)) {
//                foundCircularDependency(chain, paramSpec);
//            }
//            pa[i] = findProvider(paramSpec, chain(chain, paramSpec));
//        }
//        return pa;
//    }
//
//    public void fireProviderRegisteredEvent(Class targetType) {
//        for (InjectListener l : listeners) {
//            l.providerRegistered(targetType);
//        }
//    }
//
//    void fireInjectEvent(Object bean, BeanSpec beanSpec) {
//        for (InjectListener l : listeners) {
//            l.injected(bean, beanSpec);
//        }
//    }
//
//    public boolean subjectToInject(AccessibleObject ao) {
//        if (ao.isAnnotationPresent(Inject.class)) {
//            return true;
//        }
//        for (Class<? extends Annotation> tag : injectTagRegistry) {
//            if (ao.isAnnotationPresent(tag)) {
//                return true;
//            }
//        }
//        for (Annotation tag : ao.getDeclaredAnnotations()) {
//            Class<? extends Annotation> tagType = tag.annotationType();
//            if (tagType.isAnnotationPresent(InjectTag.class)) {
//                injectTagRegistry.add(tagType);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean subjectToInject(BeanSpec beanSpec) {
//        if (registry.containsKey(beanSpec)) {
//            return true;
//        }
//        if (beanSpec.hasAnnotation(Inject.class)) {
//            return true;
//        }
//        for (Class<? extends Annotation> tag : injectTagRegistry) {
//            if (beanSpec.hasAnnotation(tag)) {
//                return true;
//            }
//        }
//        for (Annotation tag : beanSpec.allAnnotations()) {
//            Class<? extends Annotation> tagType = tag.annotationType();
//            if (tagType.isAnnotationPresent(InjectTag.class)) {
//                injectTagRegistry.add(tagType);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static Genie create(InjectListener listener, AbsModule... modules) {
//        return new Genie(listener, modules);
//    }
//
//    /**
//     * Create a Genie instance with modules specified
//     *
//     * @param modules modules that provides binding or {@literal@}Provides methods
//     * @return an new Genie instance with modules
//     */
//    public static Genie create(AbsModule... modules) {
//        return new Genie(modules);
//    }
//
//    private static Set<BeanSpec> chain(Set<BeanSpec> chain, BeanSpec nextSpec) {
//        Set<BeanSpec> newChain = new HashSet<BeanSpec>(chain);
//        newChain.add(nextSpec);
//        return newChain;
//    }
//
//    private static StringBuffer debugChain(Set<BeanSpec> chain, BeanSpec last) {
//        StringBuffer sb = new StringBuffer();
//        for (BeanSpec spec : chain) {
//            sb.append(spec).append(" -> ");
//        }
//        sb.append(last);
//        return sb;
//    }
//
//    private static void foundCircularDependency(Set<BeanSpec> chain, BeanSpec last) {
//        throw InjectException.circularDependency(debugChain(chain, last));
//    }
//
//    private static String providerName(Provider provider) {
//        String name = provider.getClass().getName();
//        if (name.contains("org.osgl.inject.Genie$")) {
//            name = provider.toString();
//        }
//        return name;
//    }
//
//}
