package net.cassite.ioc.testioc;

import lemon.needle.ioc.annotations.DefaultBy;
import lemon.needle.ioc.annotations.Force;
import lemon.needle.ioc.annotations.Singleton;

@Singleton
public class BeanC {
    private int i;
    private BeanA a;
    private IBeanD d;

    public BeanC() {
    }

    @DefaultBy
    public BeanC(@Force("1") int i) {
        this.i = i;
    }

    public BeanA getA() {
        return a;
    }

    public void setA(BeanA a) {
        this.a = a;
    }

    public IBeanD getD() {
        return d;
    }

    public void setD(IBeanD d) {
        this.d = d;
    }

    public int getI() {
        return i;
    }
}
