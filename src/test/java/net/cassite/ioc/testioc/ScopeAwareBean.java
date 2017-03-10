package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.Inject;
import lemon.needle.ioc.binder.Scope;

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
