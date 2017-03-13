package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.osgl.inject.InjectException;
import org.osgl.inject.Injector;

import com.google.common.base.Stopwatch;

import lemon.needle.ioc.annotations.ImplementedBy;
import lemon.needle.ioc.annotations.Provides;
import lemon.needle.ioc.exception.NeedleException;
import lemon.needle.ioc.provider.InnerProvider;

public class InjectorImpl implements Injector {

    private final Map<Key<?>, Object> singletons = new ConcurrentHashMap<>();
    private final Map<Class<?>, Object[][]> injectFields = new ConcurrentHashMap<>(0);

    private final InnerProvider providers = new InnerProvider();
    private static final AtomicBoolean initOnce = new AtomicBoolean(false);
    private static final Stopwatch stopwatch = Stopwatch.createStarted();

    /**
     * Constructs Feather with configuration modules
     */
    static InjectorImpl with(Module... modules) {
        if (initOnce.compareAndSet(false, true)) {
            InjectorImpl injector = new InjectorImpl(Arrays.asList(modules));
            System.out.println(stopwatch.toString());
            return injector;
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private InjectorImpl(Iterable<Module> modules) {
        providers.put(Key.of(InjectorImpl.class), new Provider() {
            @Override
            public Object get() {
                return this;
            }
        });
        //        PrivateBinder binder = new PrivateBinder(this);
        //            module.configure(binder);//生成绑定信息
        //绑定provider提供的方式
        for (final Module module : modules) {
            if (module instanceof AbsModule) {
                ((AbsModule) module).applyTo(this);
            }
            
            for (Method providerMethod : providers(module.getClass())) {
                providerMethod(module, providerMethod);
            }
        }
        //        binder.initAllBinders();
    }

    /**
     * @return an instance of type
     */
    public <T> T instance(Class<T> type) {
        return provider(Key.of(type), null).get();
    }

    /**
     * @return instance specified by key (type and qualifier)
     */
    public <T> T instance(Key<T> key) {
        return provider(key, null).get();
    }

    /**
     * 
     * @return provider of type
     */
    public <T> Provider<T> provider(Class<T> type) {
        return provider(Key.of(type), null);
    }

    /**
     * @return provider of key (type, qualifier)
     */
    public <T> Provider<T> provider(Key<T> key) {
        return provider(key, null);
    }

    /**
     * Injects fields to the target object
     */
    public void injectFields(Object target) {
        if (!injectFields.containsKey(target.getClass())) {
            injectFields.put(target.getClass(), injectFields(target.getClass()));
        }
        for (Object[] f : injectFields.get(target.getClass())) {
            Field field = (Field) f[0];
            Key<?> key = (Key<?>) f[2];
            try {
                field.set(target, (boolean) f[1] ? provider(key) : instance(key));
            } catch (Exception e) {
                throw new NeedleException(String.format("Can't inject field %s in %s", field.getName(), target.getClass().getName()));
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> Provider<T> provider(final Key<T> key, Set<Key> chain) {
        if (!providers.containsKey(key)) {
            final Constructor<?> constructor = constructor(key);
            final Provider<?>[] paramProviders = paramProviders(key, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations(), chain);

            providers.put(key, singletonProvider(key, key.type.getAnnotation(Singleton.class), new Provider() {
                @Override
                public Object get() {
                    try {
                        return constructor.newInstance(params(paramProviders));
                    } catch (Exception e) {
                        throw new NeedleException(String.format("Can't instantiate %s", key.toString()), e);
                    }
                }
            }));
        }
        return (Provider<T>) providers.get(key);
    }

    //提出其中的provider方法
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void providerMethod(final Object module, final Method m) {
        final Key<?> key = Key.of(m.getReturnType(), qualifier(m.getAnnotations()));
        if (providers.containsKey(key)) {//不能重复
            throw new NeedleException(String.format("%s has multiple providers, module %s", key.toString(), module.getClass()));
        }
        Singleton singleton = m.getAnnotation(Singleton.class) != null ? m.getAnnotation(Singleton.class) : m.getReturnType().getAnnotation(Singleton.class);
        final Provider<?>[] paramProviders = paramProviders(key, m.getParameterTypes(), m.getGenericParameterTypes(), m.getParameterAnnotations(), Collections.singleton(key));

        providers.put(key, singletonProvider(key, singleton, new Provider() {
            @Override
            public Object get() {
                try {
                    //直接生成一个实例
                    return m.invoke(module, params(paramProviders));
                } catch (Exception e) {
                    throw new NeedleException(String.format("Can't instantiate %s with provider", key.toString()), e);
                }
            }
        }));
    }

    //生成一个单例
    @SuppressWarnings("unchecked")
    private <T> Provider<T> singletonProvider(final Key<?> key, Singleton singleton, final Provider<T> provider) {
        return singleton != null ? new Provider<T>() {
            @Override
            public T get() {
                if (!singletons.containsKey(key)) {
                    synchronized (singletons) {
                        if (!singletons.containsKey(key)) {
                            singletons.put(key, provider.get());
                        }
                    }
                }
                return (T) singletons.get(key);
            }
        } : provider;
    }

    //获取参数
    @SuppressWarnings("rawtypes")
    private Provider<?>[] paramProviders(final Key<?> key, Class<?>[] parameterClasses, Type[] parameterTypes, Annotation[][] annotations, final Set<Key> chain) {
        Provider<?>[] providers = new Provider<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            Class<?> parameterClass = parameterClasses[i];
            Annotation qualifier = qualifier(annotations[i]);
            Class<?> providerType = Provider.class.equals(parameterClass) ? (Class<?>) ((ParameterizedType) parameterTypes[i]).getActualTypeArguments()[0] : null;
            if (providerType == null) {
                final Key<?> newKey = Key.of(parameterClass, qualifier);
                final Set<Key> newChain = append(chain, key);
                if (newChain.contains(newKey)) {
                    throw new NeedleException(String.format("Circular dependency: %s", chain(newChain, newKey)));
                }
                providers[i] = new Provider() {
                    @Override
                    public Object get() {
                        return provider(newKey, newChain).get();
                    }
                };
            } else {
                final Key newKey = Key.of(providerType, qualifier);
                providers[i] = new Provider() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Object get() {
                        return provider(newKey, null);
                    }
                };
            }
        }
        return providers;
    }

    private static Object[] params(Provider<?>[] paramProviders) {
        Object[] params = new Object[paramProviders.length];
        for (int i = 0; i < paramProviders.length; ++i) {
            params[i] = paramProviders[i].get();
        }
        return params;
    }

    @SuppressWarnings("rawtypes")
    private static Set<Key> append(Set<Key> set, Key newKey) {
        if (set != null && !set.isEmpty()) {
            Set<Key> appended = new LinkedHashSet<>(set);
            appended.add(newKey);
            return appended;
        } else {
            return Collections.singleton(newKey);
        }
    }

    private static Object[][] injectFields(Class<?> target) {
        Set<Field> fields = fields(target);
        Object[][] fs = new Object[fields.size()][];
        int i = 0;
        for (Field f : fields) {
            Class<?> providerType = f.getType().equals(Provider.class) ? (Class<?>) ((ParameterizedType) f.getGenericType()).getActualTypeArguments()[0] : null;

            Class<?> type = providerType != null ? providerType : f.getType();
            Key kt = Key.of(type, qualifier(f.getAnnotations()));

            fs[i++] = new Object[] { f, providerType != null, kt };
        }
        return fs;
    }

    private static Set<Field> fields(Class<?> type) {
        Class<?> current = type;
        Set<Field> fields = new HashSet<>();
        while (!current.equals(Object.class)) {
            for (Field field : current.getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    @SuppressWarnings("rawtypes")
    private static String chain(Set<Key> chain, Key lastKey) {
        StringBuilder chainString = new StringBuilder();
        for (Key key : chain) {
            chainString.append(key.toString()).append(" -> ");
        }
        return chainString.append(lastKey.toString()).toString();
    }

    //获取其构造方法
    private static Constructor<?> constructor(Key<?> key) {
        Constructor<?> inject = null;
        Constructor<?> noarg = null;
        Class<?> targetclass = key.type;
        if (key.type.isAnnotationPresent(ImplementedBy.class)) {
            targetclass = key.type.getAnnotation(ImplementedBy.class).value();
        }
        for (Constructor<?> c : targetclass.getDeclaredConstructors()) {
            if (c.isAnnotationPresent(Inject.class)) {
                if (inject == null) {//只能有一个构造函数
                    inject = c;
                } else {
                    throw new NeedleException(String.format("%s has multiple @Inject constructors", key.type));
                }
            } else if (c.getParameterTypes().length == 0) {
                noarg = c;
            }
        }
        Constructor<?> constructor = inject != null ? inject : noarg;
        if (constructor != null) {
            constructor.setAccessible(true);
            return constructor;
        } else {
            throw new NeedleException(String.format("%s doesn't have an @Inject or no-arg constructor, or a module provider", key.type.getName()));
        }
    }

    private static Set<Method> providers(Class<?> type) {
        Class<?> current = type;
        Set<Method> providers = new HashSet<>();
        while (!current.equals(Object.class)) {
            for (Method method : current.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Provides.class) && (type.equals(current) || !providerInSubClass(method, providers))) {
                    method.setAccessible(true);
                    providers.add(method);
                }
            }
            current = current.getSuperclass();
        }
        return providers;
    }

    public Provider buildConstructor(final Constructor constructor, final Key<?> key, final Set<Key> chain) {
        Type[] ta = constructor.getGenericParameterTypes();
        Annotation[][] aaa = constructor.getParameterAnnotations();
        final Provider[] pp = paramProviders(key, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations(), chain);
        return new Provider() {
            @Override
            public Object get() {
                try {
                    return constructor.newInstance(params(pp));
                } catch (Exception e) {
                    throw new InjectException(e, "cannot instantiate %s", key);
                }
            }
        };
    }

    private static Annotation qualifier(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                return annotation;
            }
        }
        return null;
    }

    private static boolean providerInSubClass(Method method, Set<Method> discoveredMethods) {
        for (Method discovered : discoveredMethods) {
            if (discovered.getName().equals(method.getName()) && Arrays.equals(method.getParameterTypes(), discovered.getParameterTypes())) {
                return true;
            }
        }
        return false;
    }

    public InnerProvider getProviders() {
        return providers;
    }

    @Override
    public <T> T get(Class<T> type) {
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return null;
    }

    @Override
    public boolean isQualifier(Class<? extends Annotation> annoClass) {
        return false;
    }

    @Override
    public boolean isPostConstructProcessor(Class<? extends Annotation> annoClass) {
        return false;
    }

    @Override
    public boolean isScope(Class<? extends Annotation> annoClass) {
        return false;
    }

}
