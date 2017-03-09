package net.cassite.ioc.testioc;

import net.cassite.pure.aop.AOP;
import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Wire;

/**
 * bean for testing destroy weaver
 */
@AOP(value = DestroyWeaver.class, timeoutMillis = 100)
public class DestroyBean {
    @Wire
    @Force("a")
    private String s;

    public String getS() {
        return s;
    }

    public void destroy() {
        this.s = null;
    }
}
