package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.Scope;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * scope aware
 */
public class ScopeAwareBean {
    @Wire
    private Scope scope;

    public Scope getScope() {
        return scope;
    }
}
