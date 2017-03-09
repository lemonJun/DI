package net.cassite.ioc.testioc;

import lemon.ioc.di.Scope;
import lemon.ioc.di.annotations.Inject;

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
