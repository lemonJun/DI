package net.cassite.ioc.testioc;

import lemon.needle.aop.AOP;
import lemon.needle.ioc.annotations.Force;
import lemon.needle.ioc.annotations.Inject;

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
