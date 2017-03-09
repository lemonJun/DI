package net.cassite.ioc.testioc;

import net.cassite.pure.ioc.annotations.DefaultBy;
import net.cassite.pure.ioc.annotations.Force;
import net.cassite.pure.ioc.annotations.Singleton;
import net.cassite.pure.ioc.annotations.Wire;

@Singleton
@Wire
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
