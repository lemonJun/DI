package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.ScopeAttr;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * tests thread scope
 */
public class ThreadScopeBean {
    @Wire
    @ScopeAttr(value = "test", thread = true)
    @Force("a")
    String string;
}

class ThreadScopeBean2 {
    @Wire
    @ScopeAttr(value = "test", thread = true)
    String string;
}