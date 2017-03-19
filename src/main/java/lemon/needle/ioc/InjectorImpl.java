package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

import lemon.needle.exception.NeedleException;
import lemon.needle.ioc.binder.FieldInjector;
import lemon.needle.ioc.binder.MethodInjector;

public class InjectorImpl implements Injector {

    private static final Logger logger = LoggerFactory.getLogger(InjectorImpl.class);

    private final InjectorInner innerProvider = new InjectorInner();
    private static final AtomicBoolean initOnce = new AtomicBoolean(false);
    private static final Stopwatch stopwatch = Stopwatch.createStarted();

    @Override
    public <T> T instance(Class<T> type) {
        return providerRecursion(Key.of(type), null).get();
    }

    public <T> T instance(Key<T> key) {
        return providerRecursion(key, null).get();
    }

    public <T> Provider<T> provider(Class<T> type) {
        return providerRecursion(Key.of(type), null);
    }

    /**
     * @return provider of key (type, qualifier)
     */
    public <T> Provider<T> provider(Key<T> key) {
        return providerRecursion(key, null);
    }

    //最重要的一个方法 获取其Provider
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> Provider<T> providerRecursion(final Key<T> key, Set<Key> chain) {
        if (!innerProvider.containsKey(key)) {
            final Constructor<?> constructor = innerProvider.getConstructor(key);
            if (constructor != null) {
                //                final Provider<?>[] paramProviders = paramProviders(key, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), constructor.getParameterAnnotations(), chain);
                buildConstructor(key, constructor, chain);
            }
        }
        return (Provider<T>) innerProvider.get(key);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void buildFieldMethodInjector(final Object bean, final Key key, Set<Key> chain) {
        final Set<Key> newChain = append(chain, key);
        final List<FieldInjector> fieldInjectors = fieldInjectors(key.type, newChain);
        final List<MethodInjector> methodInjectors = methodInjectors(key.type, newChain);
        try {
            for (FieldInjector fj : fieldInjectors) {
                try {
                    fj.applyTo(bean);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            for (MethodInjector mj : methodInjectors) {
                try {
                    mj.applyTo(bean);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        } catch (Exception e) {
            throw new NeedleException(e, "method inject %s", key);
        }
    }

    @SuppressWarnings("rawtypes")
    private List<FieldInjector> fieldInjectors(Class type, Set<Key> chain) {
        Class<?> current = type;
        List<FieldInjector> fieldInjectors = Lists.newArrayList();
        while (null != current && !current.equals(Object.class)) {
            for (Field field : current.getDeclaredFields()) {
                if (subjectToInject(field)) {//@Inject
                    field.setAccessible(true);
                    fieldInjectors.add(fieldInjector(field, chain));
                }
            }
            current = current.getSuperclass();
        }
        return fieldInjectors;
    }

    @SuppressWarnings("rawtypes")
    private List<MethodInjector> methodInjectors(Class type, Set<Key> chain) {
        Class<?> current = type;
        List<MethodInjector> methodInjectors = Lists.newArrayList();
        while (null != current && !current.equals(Object.class)) {
            for (Method method : current.getDeclaredMethods()) {
                if (subjectToInject(method)) {//@Inject
                    method.setAccessible(true);
                    methodInjectors.add(methodInjector(method, chain));
                }
            }
            current = current.getSuperclass();
        }
        return methodInjectors;
    }

    //方法
    @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
    private MethodInjector methodInjector(Method method, Set<Key> chain) {
        Type[] paramTypes = method.getGenericParameterTypes();
        Class<?>[] classes = method.getParameterTypes();
        int len = paramTypes.length;
        Provider[] paramProviders = new Provider[len];
        Annotation[][] aaa = method.getParameterAnnotations();
        for (int i = 0; i < len; ++i) {
            Type paramType = paramTypes[i];
            Class cls = classes[i];
            Key key = Key.of(cls, innerProvider.qualifier(aaa[i]));
            if (chain.contains(key)) {
                throw new NeedleException(String.format("Circular dependency: %s", invokechain(chain, key)));
            }
            paramProviders[i] = providerRecursion(key, chain);
        }
        return new MethodInjector(method, paramProviders);
    }

    public boolean subjectToInject(AccessibleObject ao) {
        if (ao.isAnnotationPresent(Inject.class)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private FieldInjector fieldInjector(Field field, Set<Key> chain) {
        Key key = Key.of((Class<?>) field.getGenericType());
        if (chain.contains(key)) {
            throw new NeedleException(String.format("Circular dependency: %s", invokechain(chain, key)));
        }
        return new FieldInjector(field, key, providerRecursion(key, chain));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void injectOfProvides(final Object module, final Method m) {
        final Key<?> key = Key.of(m.getReturnType(), innerProvider.qualifier(m.getAnnotations()));
        if (innerProvider.containsKey(key)) {//不能重复
            throw new NeedleException(String.format("%s has multiple providers, module %s", key.toString(), module.getClass()));
        }
        Singleton singleton = m.getAnnotation(Singleton.class) != null ? m.getAnnotation(Singleton.class) : m.getReturnType().getAnnotation(Singleton.class);
        final Provider<?>[] paramProviders = paramProviders(key, m.getParameterTypes(), m.getGenericParameterTypes(), m.getParameterAnnotations(), Collections.singleton(key));

        innerProvider.put(key, innerProvider.singletonProvider(key, singleton, new Provider() {
            @Override
            public Object get() {
                try {
                    Object obj = m.invoke(module, params(paramProviders));
                    return obj;
                } catch (Exception e) {
                    throw new NeedleException(String.format("Can't instantiate %s with provider", key.toString()), e);
                }
            }
        }));
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

    @SuppressWarnings("unused")
    private Set<Field> fields(Class<?> type) {
        Class<?> current = type;
        Set<Field> fields = new HashSet<>();
        while (!current.equals(Object.class)) {
            for (Field field : current.getDeclaredFields()) {
                if (subjectToInject(field)) {
                    field.setAccessible(true);
                    fields.add(field);
                }
            }
            current = current.getSuperclass();
        }
        return fields;
    }

    @SuppressWarnings({ "rawtypes" })
    private String invokechain(Set<Key> chain, Key lastKey) {
        StringBuilder chainString = new StringBuilder();
        for (Key key : chain) {
            chainString.append(key.toString()).append(" -> ");
        }
        return chainString.append(lastKey.toString()).toString();
    }

    //
    @SuppressWarnings("rawtypes")
    Provider<?>[] paramProviders(final Key<?> key, Class<?>[] parameterClasses, Type[] parameterTypes, Annotation[][] annotations, final Set<Key> chain) {
        Provider<?>[] providers = new Provider<?>[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            Class<?> parameterClass = parameterClasses[i];
            Annotation qualifier = innerProvider.qualifier(annotations[i]);
            Class<?> providerType = Provider.class.equals(parameterClass) ? (Class<?>) ((ParameterizedType) parameterTypes[i]).getActualTypeArguments()[0] : null;
            if (providerType == null) {
                final Key<?> newKey = Key.of(parameterClass, qualifier);
                final Set<Key> newChain = append(chain, key);
                if (newChain.contains(newKey)) {
                    throw new NeedleException(String.format("Circular dependency: %s", invokechain(newChain, newKey)));
                }
                providers[i] = new Provider() {
                    @Override
                    public Object get() {
                        return providerRecursion(newKey, newChain).get();
                    }
                };
            } else {
                final Key<?> newKey = Key.of(providerType, qualifier);
                providers[i] = new Provider() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Object get() {
                        return providerRecursion(newKey, null);
                    }

                };
            }
        }
        return providers;
    }

    @SuppressWarnings("unchecked")
    public <T> Provider<T> buildConstructor(final Key key, Constructor<?> constructor, final Set<Key> chain) {
        Type[] ta = constructor.getGenericParameterTypes();
        Annotation[][] aaa = constructor.getParameterAnnotations();
        final Provider[] pp = paramProviders(key, constructor.getParameterTypes(), ta, aaa, chain);
        innerProvider.put(key, innerProvider.singletonProvider(key, key.type.getAnnotation(Singleton.class), new Provider() {
            @Override
            public Object get() {
                try {
                    Object obj = constructor.newInstance(params(pp));
                    buildFieldMethodInjector(obj, key, null);
                    return obj;
                } catch (Exception e) {
                    throw new NeedleException(String.format("Can't instantiate %s", key.toString()), e);
                }
            }
        }));
        return (Provider<T>) innerProvider.get(key);
    }

    @Override
    public <T> Provider<T> getProvider(Class<T> type) {
        return providerRecursion(Key.of(type), null);
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
        innerProvider.put(Key.of(InjectorImpl.class), new Provider() {
            @Override
            public Object get() {
                return this;
            }
        });
        //绑定provider提供的方式
        for (final Module module : modules) {
            if (module instanceof AbsModule) {
                ((AbsModule) module).applyTo(this);
            }

            for (Method prodesM : innerProvider.providers(module.getClass())) {
                injectOfProvides(module, prodesM);
            }
        }
    }
}
