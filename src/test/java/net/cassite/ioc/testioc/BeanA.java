package net.cassite.ioc.testioc;

import lemon.ioc.dinjection.annotations.Force;
import lemon.ioc.dinjection.annotations.Ignore;

public class BeanA {
    private BeanB b;
    private BeanD d;
    private String x;

    public BeanB getB() {
        return b;
    }

    public void setB(BeanB b) {
        this.b = b;
    }

    public String getX() {
        return x;
    }

    @Force("x")
    public void setX(String x) {
        this.x = x;
    }

    public BeanD getD() {
        return d;
    }

    @Ignore
    public void setD(BeanD d) {
        this.d = d;
    }
}
