package net.cassite.ioc.testioc;

import lemon.ioc.dinjection.annotations.Force;
import lemon.ioc.dinjection.annotations.Inject;
import lemon.ioc.dinjection.annotations.ScopeAttr;

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