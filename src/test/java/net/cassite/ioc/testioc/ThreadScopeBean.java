package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.Force;
import lemon.needle.ioc.annotations.Inject;
import lemon.needle.ioc.annotations.ScopeAttr;

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