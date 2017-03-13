package org.osgl.inject.provider;

import javax.inject.Provider;

public class WeightedProvider<T> implements Provider<T>, Comparable<WeightedProvider<T>> {
    private Provider<T> realProvider;
    private int affinity;

    WeightedProvider(Provider<T> provider, ThreadLocal<Integer> AFFINITY) {
        realProvider = provider;
        affinity = AFFINITY.get();
    }

    @Override
    public T get() {
        return realProvider.get();
    }

    @Override
    public int compareTo(WeightedProvider<T> o) {
        return this.affinity - o.affinity;
    }

    public static <T> WeightedProvider<T> decorate(Provider<T> provider, ThreadLocal<Integer> AFFINITY) {
        return provider instanceof WeightedProvider ? (WeightedProvider<T>) provider : new WeightedProvider<T>(provider, AFFINITY);
    }

    public Provider<T> getRealProvider() {
        return realProvider;
    }

    public void setRealProvider(Provider<T> realProvider) {
        this.realProvider = realProvider;
    }

    public int getAffinity() {
        return affinity;
    }

    public void setAffinity(int affinity) {
        this.affinity = affinity;
    }

}
