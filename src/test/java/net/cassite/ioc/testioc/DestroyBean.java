package net.cassite.ioc.testioc;

import lemon.ioc.aop.AOP;
import lemon.ioc.dinjection.annotations.Force;
import lemon.ioc.dinjection.annotations.Inject;

/**
 * bean for testing destroy weaver
 */
@AOP(value = DestroyWeaver.class, timeoutMillis = 100)
public class DestroyBean {
    @Inject
    @Force("a")
    private String s;

    public String getS() {
        return s;
    }

    public void destroy() {
        this.s = null;
    }
}
