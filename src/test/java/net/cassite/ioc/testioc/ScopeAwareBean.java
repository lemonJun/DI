package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Inject;
import lemon.ioc.di.binder.Scope;

/**
 * scope aware
 */
public class ScopeAwareBean {
    @Inject
    private Scope scope;

    public Scope getScope() {
        return scope;
    }
}
