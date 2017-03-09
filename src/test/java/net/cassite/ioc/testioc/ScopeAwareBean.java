package net.cassite.ioc.testioc;

import lemon.ioc.dinjection.annotations.Inject;
import lemon.ioc.dinjection.binder.Scope;

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
