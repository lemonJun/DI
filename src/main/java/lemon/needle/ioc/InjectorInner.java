package lemon.needle.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lemon.needle.exception.NeedleException;
import lemon.needle.ioc.annotations.ImplementedBy;
import lemon.needle.ioc.annotations.Provides;

/**
 * 
 * 
 * @author WangYazhou
 * @date  2017年3月11日 下午4:33:24
 * @see
 */
public class InjectorInner {

    private final static Logger logger = LoggerFactory.getLogger(InjectorInner.class);

    private final ConcurrentMap<Key<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    private final Map<Key<?>, Object> singletons = new ConcurrentHashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(Key<?> key, Provider<?> value) {
        final Lock wlock = lock.writeLock();
        try {
            wlock.tryLock(3, TimeUnit.MILLISECONDS);
            providers.putIfAbsent(key, value);
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            if (wlock != null) {
                wlock.unlock();
            }
        }
    }

    public boolean containsKey(Key<?> key) {
        return providers.containsKey(key);
    }

    public <T> Provider<T> get(Key<?> key) {
        return (Provider<T>) providers.get(key);
    }

    @SuppressWarnings("unchecked")
    <T> Provider<T> singletonProvider(final Key<?> key, Singleton singleton, final Provider<T> provider) {
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

    //
    Constructor<?> getConstructor(Key<?> key) {
        try {
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
                } else if (c.getParameterTypes().length == 0 && c.getModifiers() != Modifier.PRIVATE) {
                    noarg = c;
                }
            }
            Constructor<?> constructor = inject != null ? inject : noarg;
            if (constructor != null) {
                constructor.setAccessible(true);
                return constructor;
            }
            throw new NeedleException(String.format("%s doesn't have an @Inject or no-arg constructor which is not private, or a module provider", key.type.getName()));

        } catch (SecurityException e) {
            logger.error("", e);
        }
        return null;
    }

    //获取一个类中 包含Provides注解的方法
    Set<Method> providers(Class<?> type) {
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

    private boolean providerInSubClass(Method method, Set<Method> discoveredMethods) {
        for (Method discovered : discoveredMethods) {
            if (discovered.getName().equals(method.getName()) && Arrays.equals(method.getParameterTypes(), discovered.getParameterTypes())) {
                return true;
            }
        }
        return false;
    }

    //过滤归属于Qualifier的注解
    Annotation qualifier(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Qualifier.class)) {
                return annotation;
            }
        }
        return null;
    }
}
