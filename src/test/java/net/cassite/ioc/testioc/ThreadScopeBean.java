package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Force;
import lemon.ioc.di.annotations.Inject;
import lemon.ioc.di.annotations.ScopeAttr;

/**
 * tests thread scope
 */
public class ThreadScopeBean {
    @Inject
    @ScopeAttr(value = "test", thread = true)
    @Force("a")
    String string;
}

class ThreadScopeBean2 {
    @Inject

    @ScopeAttr(value = "test", thread = true)
    String string;
}