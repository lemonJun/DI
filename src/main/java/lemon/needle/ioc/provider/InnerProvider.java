package lemon.needle.ioc.provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.inject.Provider;

import lemon.needle.ioc.Key;

public class InnerProvider {

    private final Map<Key<?>, Provider<?>> providers = new ConcurrentHashMap<>();

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(Key<?> key, Provider<?> value) {
        final Lock wlock = lock.writeLock();
        try {
            wlock.tryLock(3, TimeUnit.MILLISECONDS);
            providers.put(key, value);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
}
