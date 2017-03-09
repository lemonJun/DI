package net.cassite.ioc.testioc;

import lemon.ioc.di.annotations.Force;

public class BeanB {
    private BeanC c;
    private double d;

    public BeanB(@Force("2.0") Double d) {
        this.d = d;
    }

    public BeanC getC() {
        return c;
    }

    public void setC(BeanC c) {
        this.c = c;
    }

    public double getD() {
        return d;
    }
}
